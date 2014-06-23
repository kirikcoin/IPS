package mobi.eyeline.ips.external;

import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.CampaignsSoapImplService;
import mobi.eyeline.ips.properties.Config;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.PortInfo;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class MadvSoapApi {

    private final Config config;

    public MadvSoapApi(Config config) {
        this.config = config;
    }

    private CampaignsSoapImpl init(String url,
                                   final String login,
                                   final String password) {

        final CampaignsSoapImplService service;
        try {
            service = new CampaignsSoapImplService(new URL(url));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Configuration error", e);
        }

        service.setHandlerResolver(new HandlerResolver() {
            @Override
            public List<Handler> getHandlerChain(PortInfo portInfo) {
                return Collections.<Handler>singletonList(new HeaderHandler(login, password));
            }
        });

        return service.getCampaignsSoapImplPort();
    }

    public CampaignsSoapImpl getSoapApi() {
        return init(
                config.getMadvUrl(),
                config.getMadvUserLogin(),
                config.getMadvUserPassword());
    }

    private static class HeaderHandler implements SOAPHandler<SOAPMessageContext> {

        private final QName nameAuth = new QName("https://mts-madv.eyeline.mobi", "Authentication");

        private final String username;
        private final String password;

        private HeaderHandler(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public Set<QName> getHeaders() {
            return null;
        }

        @Override
        public boolean handleMessage(SOAPMessageContext context) {
            try {
                final SOAPMessage message = context.getMessage();

                SOAPHeader header = message.getSOAPHeader();
                if (header == null) {
                    SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
                    header = envelope.addHeader();
                }

                final SOAPHeaderElement userCredentials = header.addHeaderElement(nameAuth);
                userCredentials.addChildElement("User").setValue(this.username);
                userCredentials.addChildElement("Password").setValue(this.password);

                message.saveChanges();

                return true;

            } catch (SOAPException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean handleFault(SOAPMessageContext context) {
            return false;
        }

        @Override
        public void close(MessageContext context) {

        }
    }
}
