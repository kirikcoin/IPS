package mobi.eyeline.ips.external;

import mobi.eyeline.ips.external.esdp.EsdpServiceManager;
import mobi.eyeline.ips.external.esdp.EsdpServiceManagerService;
import mobi.eyeline.ips.properties.Config;
import mobi.eyeline.ips.util.HashUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.MessageContext;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EsdpSoapApi {

    private static final Logger logger = LoggerFactory.getLogger(EsdpSoapApi.class);

    private final EsdpServiceManager serviceManager;

    public EsdpSoapApi(Config config) {
        try {
            serviceManager = initServiceManager(config);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Configuration error", e);
        }

        setCredentials(serviceManager, config);
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

    private void setCredentials(EsdpServiceManager esdpServiceManager, Config config) {

        final String login = config.getEsdpLogin();
        final String password = config.getEsdpPassword();

        final String pwHash;
        try {
            // Note: ESDP console generates hashes in lowercase and performs case-sensitive lookup.
            pwHash = HashUtils.hash(password, "MD5", "UTF-8").toLowerCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        final Map<String, Object> reqContext =
                ((BindingProvider) esdpServiceManager).getRequestContext();

        Map<String, List<String>> headers = new HashMap<>();

        headers.put("X-API-Login", Collections.singletonList(login));
        headers.put("X-API-Password", Collections.singletonList(pwHash));
        reqContext.put(MessageContext.HTTP_REQUEST_HEADERS, headers);
    }

    public EsdpServiceManager getServiceManager() {
        return serviceManager;
    }
}
