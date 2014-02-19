/**
 * BannerParams.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class BannerParams  implements java.io.Serializable {
    private long maxImpressions;

    private int commChannel;

    private java.lang.String text;

    private long dailyImpressionsLimit;

    private mobi.eyeline.ips.external.madv.ClickServiceInfo clickService;

    private mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink;

    public BannerParams() {
    }

    public BannerParams(
           long maxImpressions,
           int commChannel,
           java.lang.String text,
           long dailyImpressionsLimit,
           mobi.eyeline.ips.external.madv.ClickServiceInfo clickService,
           mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink) {
           this.maxImpressions = maxImpressions;
           this.commChannel = commChannel;
           this.text = text;
           this.dailyImpressionsLimit = dailyImpressionsLimit;
           this.clickService = clickService;
           this.externalLink = externalLink;
    }


    /**
     * Gets the maxImpressions value for this BannerParams.
     * 
     * @return maxImpressions
     */
    public long getMaxImpressions() {
        return maxImpressions;
    }


    /**
     * Sets the maxImpressions value for this BannerParams.
     * 
     * @param maxImpressions
     */
    public void setMaxImpressions(long maxImpressions) {
        this.maxImpressions = maxImpressions;
    }


    /**
     * Gets the commChannel value for this BannerParams.
     * 
     * @return commChannel
     */
    public int getCommChannel() {
        return commChannel;
    }


    /**
     * Sets the commChannel value for this BannerParams.
     * 
     * @param commChannel
     */
    public void setCommChannel(int commChannel) {
        this.commChannel = commChannel;
    }


    /**
     * Gets the text value for this BannerParams.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this BannerParams.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }


    /**
     * Gets the dailyImpressionsLimit value for this BannerParams.
     * 
     * @return dailyImpressionsLimit
     */
    public long getDailyImpressionsLimit() {
        return dailyImpressionsLimit;
    }


    /**
     * Sets the dailyImpressionsLimit value for this BannerParams.
     * 
     * @param dailyImpressionsLimit
     */
    public void setDailyImpressionsLimit(long dailyImpressionsLimit) {
        this.dailyImpressionsLimit = dailyImpressionsLimit;
    }


    /**
     * Gets the clickService value for this BannerParams.
     * 
     * @return clickService
     */
    public mobi.eyeline.ips.external.madv.ClickServiceInfo getClickService() {
        return clickService;
    }


    /**
     * Sets the clickService value for this BannerParams.
     * 
     * @param clickService
     */
    public void setClickService(mobi.eyeline.ips.external.madv.ClickServiceInfo clickService) {
        this.clickService = clickService;
    }


    /**
     * Gets the externalLink value for this BannerParams.
     * 
     * @return externalLink
     */
    public mobi.eyeline.ips.external.madv.ExternalLinkInfo getExternalLink() {
        return externalLink;
    }


    /**
     * Sets the externalLink value for this BannerParams.
     * 
     * @param externalLink
     */
    public void setExternalLink(mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink) {
        this.externalLink = externalLink;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BannerParams)) return false;
        BannerParams other = (BannerParams) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.maxImpressions == other.getMaxImpressions() &&
            this.commChannel == other.getCommChannel() &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText()))) &&
            this.dailyImpressionsLimit == other.getDailyImpressionsLimit() &&
            ((this.clickService==null && other.getClickService()==null) || 
             (this.clickService!=null &&
              this.clickService.equals(other.getClickService()))) &&
            ((this.externalLink==null && other.getExternalLink()==null) || 
             (this.externalLink!=null &&
              this.externalLink.equals(other.getExternalLink())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        _hashCode += new Long(getMaxImpressions()).hashCode();
        _hashCode += getCommChannel();
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        _hashCode += new Long(getDailyImpressionsLimit()).hashCode();
        if (getClickService() != null) {
            _hashCode += getClickService().hashCode();
        }
        if (getExternalLink() != null) {
            _hashCode += getExternalLink().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BannerParams.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "bannerParams"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxImpressions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxImpressions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("commChannel");
        elemField.setXmlName(new javax.xml.namespace.QName("", "commChannel"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dailyImpressionsLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dailyImpressionsLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clickService");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clickService"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "clickServiceInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("externalLink");
        elemField.setXmlName(new javax.xml.namespace.QName("", "externalLink"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "externalLinkInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
