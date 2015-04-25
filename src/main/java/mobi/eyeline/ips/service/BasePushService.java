package mobi.eyeline.ips.service;

import mobi.eyeline.ips.properties.Config;
import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.RequestClientConnControl;
import org.apache.http.client.protocol.RequestDefaultHeaders;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RequestWrapper;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;

import static org.apache.http.HttpStatus.SC_OK;

public abstract class BasePushService {

  private static final Logger logger = LoggerFactory.getLogger(BasePushService.class);

  protected final Config config;
  private final ThreadLocal<HttpClient> clientHolder;

  protected BasePushService(Config config) {
    this.config = config;
    clientHolder = initClient();
  }

  protected void doRequest(URI uri) throws IOException {
    doRequest(uri, RequestExecutionListener.EMPTY);
  }

  protected void doRequest(URI uri,
                           RequestExecutionListener listener) throws IOException {
    final HttpGet get = new HttpGetWithListener(uri, listener);
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
    connectionManager.setDefaultMaxPerRoute(config.getSadsMaxSessions());

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

    @Override
    protected HttpRequestExecutor createRequestExecutor() {
      return new NotifyingRequestExecutor();
    }

  }


  /**
   * Calls {@link RequestExecutionListener execution listener} after request is sent.
   */
  private static class NotifyingRequestExecutor extends HttpRequestExecutor {
    @Override
    protected HttpResponse doSendRequest(HttpRequest request,
                                         HttpClientConnection conn,
                                         HttpContext context)
        throws IOException, HttpException {
      try {
        return super.doSendRequest(request, conn, context);

      } finally {
        if (request instanceof RequestWrapper) {
          request = ((RequestWrapper) request).getOriginal();
        }

        if (request instanceof RequestWithListener) {
          final RequestExecutionListener listener =
              ((RequestWithListener) request).getRequestExecutionListener();
          if (listener != null) {
            listener.onAfterSendRequest();
          }
        }
      }
    }
  }


  //
  //
  //

  public static class RequestExecutionListener {
    private static final RequestExecutionListener EMPTY =
        new RequestExecutionListener() {
          @Override
          public void onAfterSendRequest() {
          }
        };

    public void onAfterSendRequest() {
    }
  }


  //
  //
  //

  private static interface RequestWithListener {
    RequestExecutionListener getRequestExecutionListener();
  }


  //
  //
  //

  private static class HttpGetWithListener
      extends HttpGet implements RequestWithListener {

    private final RequestExecutionListener listener;

    private HttpGetWithListener(URI uri, RequestExecutionListener listener) {
      super(uri);
      this.listener = listener;
    }

    @Override
    public RequestExecutionListener getRequestExecutionListener() {
      return listener;
    }
  }
}
