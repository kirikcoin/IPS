package mobi.eyeline.ips.service;

import mobi.eyeline.ips.model.Survey;
import mobi.eyeline.ips.properties.Config;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static mobi.eyeline.ips.messages.UssdOption.PARAM_SKIP_VALIDATION;
import static mobi.eyeline.ips.messages.UssdOption.PARAM_SURVEY_ID;
import static org.apache.http.HttpStatus.SC_OK;

public class PushService {

    private static final Logger logger = LoggerFactory.getLogger(PushService.class);

    private final Config config;
    private final ThreadLocal<HttpClient> clientHolder;

    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public PushService(Config config) {
        this.config = config;
        clientHolder = initClient();
    }

    public void scheduleSend(final Survey survey,
                             final String msisdn) {

        executor.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    send(survey, msisdn);
                } catch (URISyntaxException | IOException e) {
                    logger.error("Error sending PUSH-request, " +
                            "survey = [" + survey + "], msisdn = [" + msisdn + "]", e);
                }
            }
        });

        logger.debug("Scheduled PUSH request:" +
                " survey = [" + survey + "], msisdn = [" + msisdn + "]");
    }

    protected void send(Survey survey,
                        String msisdn) throws URISyntaxException, IOException {

        logger.debug("Sending PUSH request:" +
                " survey = [" + survey + "], msisdn = [" + msisdn + "]");

        final URI uri = buildUri(msisdn, survey.getId());
        doRequest(uri);
    }

    private URI buildUri(String msisdn, int surveyId)
            throws URISyntaxException {

        final String pushUrl = config.getSadsPushUrl();

        final URIBuilder builder = new URIBuilder(pushUrl);
        builder.addParameter(PARAM_SURVEY_ID, String.valueOf(surveyId));
        builder.addParameter("subscriber", msisdn);
        builder.addParameter(PARAM_SKIP_VALIDATION, "true");

        return builder.build();
    }

    protected void doRequest(URI uri) throws IOException {
        final HttpGet get = new HttpGet(uri);
        final HttpResponse response = clientHolder.get().execute(get);

        try {
            final StatusLine statusLine = response.getStatusLine();
            if (statusLine.getStatusCode() != SC_OK) {
                logger.error("Server responded with [" + statusLine.getStatusCode() + "]: " +
                        statusLine.getReasonPhrase() + ": " + EntityUtils.toString(response.getEntity()));
            }
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    private ThreadLocal<HttpClient> initClient() {
        final PoolingClientConnectionManager connectionManager =
                new PoolingClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(2 * config.getSadsMaxSessions());

        return new ThreadLocal<HttpClient>() {
            @Override
            protected HttpClient initialValue() {
                return createClient(connectionManager);
            }
        };
    }

    private HttpClient createClient(PoolingClientConnectionManager connectionManager) {
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setSoKeepalive(httpParams, true);

        final DefaultHttpClient httpClient =
                new SimpleHttpClient(connectionManager, httpParams);
        httpClient.setReuseStrategy(new AlwaysConnectionReuseStrategy());

        return httpClient;
    }

    /**
     * Always reuses connections, regardless of received headers.
     */
    private static class AlwaysConnectionReuseStrategy
            implements ConnectionReuseStrategy {

        @Override
        public boolean keepAlive(HttpResponse httpResponse,
                                 HttpContext httpContext) {
            return true;
        }
    }

    private static class SimpleHttpClient extends DefaultHttpClient {

        public SimpleHttpClient(PoolingClientConnectionManager connectionManager,
                                HttpParams httpParams) {
            super(connectionManager, httpParams);
        }

        @Override
        protected BasicHttpProcessor createHttpProcessor() {
            final BasicHttpProcessor httpProcessor = new BasicHttpProcessor();
            httpProcessor.addInterceptor(new RequestDefaultHeaders());

            // Required protocol interceptors.
            httpProcessor.addInterceptor(new RequestContent());
            httpProcessor.addInterceptor(new RequestTargetHost());

            // Recommended protocol interceptors.
            httpProcessor.addInterceptor(new RequestClientConnControl());
            httpProcessor.addInterceptor(new RequestExpectContinue());

            return httpProcessor;
        }
    }
}
