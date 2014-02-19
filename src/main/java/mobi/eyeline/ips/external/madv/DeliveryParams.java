/**
 * DeliveryParams.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class DeliveryParams  implements java.io.Serializable {
    private java.lang.String name;

    private java.lang.String text;

    private java.lang.String startDate;

    private java.lang.String endDate;

    private java.lang.String sourceAddress;

    private long maxImpressions;

    private java.lang.Long dailyImpressionsLimit;

    private mobi.eyeline.ips.external.madv.ClickServiceInfo clickService;

    private mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink;

    public DeliveryParams() {
    }

    public DeliveryParams(
           java.lang.String name,
           java.lang.String text,
           java.lang.String startDate,
           java.lang.String endDate,
           java.lang.String sourceAddress,
           long maxImpressions,
           java.lang.Long dailyImpressionsLimit,
           mobi.eyeline.ips.external.madv.ClickServiceInfo clickService,
           mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink) {
           this.name = name;
           this.text = text;
           this.startDate = startDate;
           this.endDate = endDate;
           this.sourceAddress = sourceAddress;
           this.maxImpressions = maxImpressions;
           this.dailyImpressionsLimit = dailyImpressionsLimit;
           this.clickService = clickService;
           this.externalLink = externalLink;
    }


    /**
     * Gets the name value for this DeliveryParams.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }


    /**
     * Sets the name value for this DeliveryParams.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }


    /**
     * Gets the text value for this DeliveryParams.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this DeliveryParams.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }


    /**
     * Gets the startDate value for this DeliveryParams.
     * 
     * @return startDate
     */
    public java.lang.String getStartDate() {
        return startDate;
    }


    /**
     * Sets the startDate value for this DeliveryParams.
     * 
     * @param startDate
     */
    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }


    /**
     * Gets the endDate value for this DeliveryParams.
     * 
     * @return endDate
     */
    public java.lang.String getEndDate() {
        return endDate;
    }


    /**
     * Sets the endDate value for this DeliveryParams.
     * 
     * @param endDate
     */
    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }


    /**
     * Gets the sourceAddress value for this DeliveryParams.
     * 
     * @return sourceAddress
     */
    public java.lang.String getSourceAddress() {
        return sourceAddress;
    }


    /**
     * Sets the sourceAddress value for this DeliveryParams.
     * 
     * @param sourceAddress
     */
    public void setSourceAddress(java.lang.String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }


    /**
     * Gets the maxImpressions value for this DeliveryParams.
     * 
     * @return maxImpressions
     */
    public long getMaxImpressions() {
        return maxImpressions;
    }


    /**
     * Sets the maxImpressions value for this DeliveryParams.
     * 
     * @param maxImpressions
     */
    public void setMaxImpressions(long maxImpressions) {
        this.maxImpressions = maxImpressions;
    }


    /**
     * Gets the dailyImpressionsLimit value for this DeliveryParams.
     * 
     * @return dailyImpressionsLimit
     */
    public java.lang.Long getDailyImpressionsLimit() {
        return dailyImpressionsLimit;
    }


    /**
     * Sets the dailyImpressionsLimit value for this DeliveryParams.
     * 
     * @param dailyImpressionsLimit
     */
    public void setDailyImpressionsLimit(java.lang.Long dailyImpressionsLimit) {
        this.dailyImpressionsLimit = dailyImpressionsLimit;
    }


    /**
     * Gets the clickService value for this DeliveryParams.
     * 
     * @return clickService
     */
    public mobi.eyeline.ips.external.madv.ClickServiceInfo getClickService() {
        return clickService;
    }


    /**
     * Sets the clickService value for this DeliveryParams.
     * 
     * @param clickService
     */
    public void setClickService(mobi.eyeline.ips.external.madv.ClickServiceInfo clickService) {
        this.clickService = clickService;
    }


    /**
     * Gets the externalLink value for this DeliveryParams.
     * 
     * @return externalLink
     */
    public mobi.eyeline.ips.external.madv.ExternalLinkInfo getExternalLink() {
        return externalLink;
    }


    /**
     * Sets the externalLink value for this DeliveryParams.
     * 
     * @param externalLink
     */
    public void setExternalLink(mobi.eyeline.ips.external.madv.ExternalLinkInfo externalLink) {
        this.externalLink = externalLink;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeliveryParams)) return false;
        DeliveryParams other = (DeliveryParams) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.name==null && other.getName()==null) || 
             (this.name!=null &&
              this.name.equals(other.getName()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText()))) &&
            ((this.startDate==null && other.getStartDate()==null) || 
             (this.startDate!=null &&
              this.startDate.equals(other.getStartDate()))) &&
            ((this.endDate==null && other.getEndDate()==null) || 
             (this.endDate!=null &&
              this.endDate.equals(other.getEndDate()))) &&
            ((this.sourceAddress==null && other.getSourceAddress()==null) || 
             (this.sourceAddress!=null &&
              this.sourceAddress.equals(other.getSourceAddress()))) &&
            this.maxImpressions == other.getMaxImpressions() &&
            ((this.dailyImpressionsLimit==null && other.getDailyImpressionsLimit()==null) || 
             (this.dailyImpressionsLimit!=null &&
              this.dailyImpressionsLimit.equals(other.getDailyImpressionsLimit()))) &&
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
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getStartDate() != null) {
            _hashCode += getStartDate().hashCode();
        }
        if (getEndDate() != null) {
            _hashCode += getEndDate().hashCode();
        }
        if (getSourceAddress() != null) {
            _hashCode += getSourceAddress().hashCode();
        }
        _hashCode += new Long(getMaxImpressions()).hashCode();
        if (getDailyImpressionsLimit() != null) {
            _hashCode += getDailyImpressionsLimit().hashCode();
        }
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
        new org.apache.axis.description.TypeDesc(DeliveryParams.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "deliveryParams"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("startDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "startDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("endDate");
        elemField.setXmlName(new javax.xml.namespace.QName("", "endDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sourceAddress");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sourceAddress"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxImpressions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxImpressions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("dailyImpressionsLimit");
        elemField.setXmlName(new javax.xml.namespace.QName("", "dailyImpressionsLimit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setMinOccurs(0);
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
