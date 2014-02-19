/**
 * BannerInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class BannerInfo  implements java.io.Serializable {
    private java.lang.Integer id;

    private java.lang.String status;

    private long impressionsCount;

    private long clicksCount;

    private long conversionsCount;

    private mobi.eyeline.ips.external.madv.BannerParams params;

    public BannerInfo() {
    }

    public BannerInfo(
           java.lang.Integer id,
           java.lang.String status,
           long impressionsCount,
           long clicksCount,
           long conversionsCount,
           mobi.eyeline.ips.external.madv.BannerParams params) {
           this.id = id;
           this.status = status;
           this.impressionsCount = impressionsCount;
           this.clicksCount = clicksCount;
           this.conversionsCount = conversionsCount;
           this.params = params;
    }

    /**
     * Gets the id value for this BannerInfo.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this BannerInfo.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the status value for this BannerInfo.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this BannerInfo.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }


    /**
     * Gets the impressionsCount value for this BannerInfo.
     * 
     * @return impressionsCount
     */
    public long getImpressionsCount() {
        return impressionsCount;
    }


    /**
     * Sets the impressionsCount value for this BannerInfo.
     * 
     * @param impressionsCount
     */
    public void setImpressionsCount(long impressionsCount) {
        this.impressionsCount = impressionsCount;
    }


    /**
     * Gets the clicksCount value for this BannerInfo.
     * 
     * @return clicksCount
     */
    public long getClicksCount() {
        return clicksCount;
    }


    /**
     * Sets the clicksCount value for this BannerInfo.
     * 
     * @param clicksCount
     */
    public void setClicksCount(long clicksCount) {
        this.clicksCount = clicksCount;
    }


    /**
     * Gets the conversionsCount value for this BannerInfo.
     * 
     * @return conversionsCount
     */
    public long getConversionsCount() {
        return conversionsCount;
    }


    /**
     * Sets the conversionsCount value for this BannerInfo.
     * 
     * @param conversionsCount
     */
    public void setConversionsCount(long conversionsCount) {
        this.conversionsCount = conversionsCount;
    }


    /**
     * Gets the params value for this BannerInfo.
     * 
     * @return params
     */
    public mobi.eyeline.ips.external.madv.BannerParams getParams() {
        return params;
    }


    /**
     * Sets the params value for this BannerInfo.
     * 
     * @param params
     */
    public void setParams(mobi.eyeline.ips.external.madv.BannerParams params) {
        this.params = params;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof BannerInfo)) return false;
        BannerInfo other = (BannerInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus()))) &&
            this.impressionsCount == other.getImpressionsCount() &&
            this.clicksCount == other.getClicksCount() &&
            this.conversionsCount == other.getConversionsCount() &&
            ((this.params==null && other.getParams()==null) || 
             (this.params!=null &&
              this.params.equals(other.getParams())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += new Long(getImpressionsCount()).hashCode();
        _hashCode += new Long(getClicksCount()).hashCode();
        _hashCode += new Long(getConversionsCount()).hashCode();
        if (getParams() != null) {
            _hashCode += getParams().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(BannerInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "bannerInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("impressionsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "impressionsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("clicksCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "clicksCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionsCount");
        elemField.setXmlName(new javax.xml.namespace.QName("", "conversionsCount"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "long"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("params");
        elemField.setXmlName(new javax.xml.namespace.QName("", "params"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "bannerParams"));
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
