package mobi.eyeline.ips.external;

import mobi.eyeline.ips.external.madv.CampaignsSoapImpl;
import mobi.eyeline.ips.external.madv.CampaignsSoapImplServiceLocator;
import org.apache.axis.message.SOAPHeaderElement;


import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import java.net.MalformedURLException;
import java.net.URL;


public class MadvSoapApi {
    public static CampaignsSoapImpl get(String url, String login, String password){
        CampaignsSoapImpl result;
        try {
            result = (new CampaignsSoapImplServiceLocator()).getCampaignsSoapImplPort(new URL(url));

            QName qNameUserCredentials = new QName("https://mts-madv.eyeline.mobi", "Authentication");
            SOAPHeaderElement userCredentials = new org.apache.axis.message.SOAPHeaderElement(qNameUserCredentials);
            userCredentials.setActor(null);

            SOAPElement userLogin = userCredentials.addChildElement("User");
            userLogin.setValue(login);
            SOAPElement userPassword = userCredentials.addChildElement("Password");
            userPassword.setValue(password);

            ((org.apache.axis.client.Stub) result).setHeader(userCredentials);

        } catch (ServiceException e) {
            e.printStackTrace();
            return null;
        } catch (SOAPException e) {
            e.printStackTrace();
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}
