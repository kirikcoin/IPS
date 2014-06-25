package mobi.eyeline.ips.external;

import mobi.eyeline.ips.external.esdp.EsdpServiceManager;
import mobi.eyeline.ips.external.esdp.EsdpServiceManagerService;
import mobi.eyeline.ips.properties.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsdpSoapApi {

    private static final Logger logger = LoggerFactory.getLogger(EsdpSoapApi.class);

    private final EsdpServiceManager soapApi;

    public EsdpSoapApi(Config config, String login, String passwordHash) {
        try {
            soapApi = initServiceManager(config);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Configuration error", e);
        }

        setCredentials(soapApi, login, passwordHash);
    }

    private EsdpServiceManager initServiceManager(Config config) throws MalformedURLException {
        final String wsdlLocation = config.getEsdpEndpointUrl();
        logger.info("Using service endpoint URL = [" + wsdlLocation + "]");

        final URL url = new URL(EsdpServiceManagerService.class.getResource("."), wsdlLocation);

        final QName serviceName =
                new QName("http://api.web.esdp.eyeline.mobi/", "EsdpServiceManagerService");
        final EsdpServiceManagerService esdpServiceManagerService =
                new EsdpServiceManagerService(url, serviceName);

        return esdpServiceManagerService.getEsdpServiceManagerPort();
    }

    private void setCredentials(EsdpServiceManager esdpServiceManager,
                                String login,
                                String passwordHash) {

        final Map<String, Object> reqContext =
                ((BindingProvider) esdpServiceManager).getRequestContext();

        Map<String, List<String>> headers = new HashMap<>();

        headers.put("X-API-Login", Collections.singletonList(login));
        headers.put("X-API-Password", Collections.singletonList(passwordHash));
        reqContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    public EsdpServiceManager getSoapApi() {
        return soapApi;
    }
}
