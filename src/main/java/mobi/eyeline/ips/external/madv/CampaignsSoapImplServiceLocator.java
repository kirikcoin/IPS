/**
 * CampaignsSoapImplServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class CampaignsSoapImplServiceLocator extends org.apache.axis.client.Service implements mobi.eyeline.ips.external.madv.CampaignsSoapImplService {

    public CampaignsSoapImplServiceLocator() {
    }


    public CampaignsSoapImplServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public CampaignsSoapImplServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for CampaignsSoapImplPort
    private java.lang.String CampaignsSoapImplPort_address = "http://212.192.169.195:9080/services/campaigns";

    public java.lang.String getCampaignsSoapImplPortAddress() {
        return CampaignsSoapImplPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String CampaignsSoapImplPortWSDDServiceName = "CampaignsSoapImplPort";

    public java.lang.String getCampaignsSoapImplPortWSDDServiceName() {
        return CampaignsSoapImplPortWSDDServiceName;
    }

    public void setCampaignsSoapImplPortWSDDServiceName(java.lang.String name) {
        CampaignsSoapImplPortWSDDServiceName = name;
    }

    public mobi.eyeline.ips.external.madv.CampaignsSoapImpl getCampaignsSoapImplPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(CampaignsSoapImplPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getCampaignsSoapImplPort(endpoint);
    }

    public mobi.eyeline.ips.external.madv.CampaignsSoapImpl getCampaignsSoapImplPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            mobi.eyeline.ips.external.madv.CampaignsSoapImplPortBindingStub _stub = new mobi.eyeline.ips.external.madv.CampaignsSoapImplPortBindingStub(portAddress, this);
            _stub.setPortName(getCampaignsSoapImplPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setCampaignsSoapImplPortEndpointAddress(java.lang.String address) {
        CampaignsSoapImplPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (mobi.eyeline.ips.external.madv.CampaignsSoapImpl.class.isAssignableFrom(serviceEndpointInterface)) {
                mobi.eyeline.ips.external.madv.CampaignsSoapImplPortBindingStub _stub = new mobi.eyeline.ips.external.madv.CampaignsSoapImplPortBindingStub(new java.net.URL(CampaignsSoapImplPort_address), this);
                _stub.setPortName(getCampaignsSoapImplPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("CampaignsSoapImplPort".equals(inputPortName)) {
            return getCampaignsSoapImplPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "CampaignsSoapImplService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "CampaignsSoapImplPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("CampaignsSoapImplPort".equals(portName)) {
            setCampaignsSoapImplPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
