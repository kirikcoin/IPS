/**
 * TargetingParams.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class TargetingParams  implements java.io.Serializable {
    private java.lang.String fixedType;

    private java.lang.String sex;

    private java.lang.Integer minAge;

    private java.lang.Integer maxAge;

    private int[] homeRegions;

    private java.lang.String addSubscribersSource;

    private mobi.eyeline.ips.external.madv.FileInfo addSubscribers;

    private java.lang.String subscriberSource;

    private mobi.eyeline.ips.external.madv.FileInfo subscribers;

    private mobi.eyeline.ips.external.madv.TargetingFilterInfo[] targetingFilters;

    public TargetingParams() {
    }

    public TargetingParams(
           java.lang.String fixedType,
           java.lang.String sex,
           java.lang.Integer minAge,
           java.lang.Integer maxAge,
           int[] homeRegions,
           java.lang.String addSubscribersSource,
           mobi.eyeline.ips.external.madv.FileInfo addSubscribers,
           java.lang.String subscriberSource,
           mobi.eyeline.ips.external.madv.FileInfo subscribers,
           mobi.eyeline.ips.external.madv.TargetingFilterInfo[] targetingFilters) {
           this.fixedType = fixedType;
           this.sex = sex;
           this.minAge = minAge;
           this.maxAge = maxAge;
           this.homeRegions = homeRegions;
           this.addSubscribersSource = addSubscribersSource;
           this.addSubscribers = addSubscribers;
           this.subscriberSource = subscriberSource;
           this.subscribers = subscribers;
           this.targetingFilters = targetingFilters;
    }


    /**
     * Gets the fixedType value for this TargetingParams.
     * 
     * @return fixedType
     */
    public java.lang.String getFixedType() {
        return fixedType;
    }


    /**
     * Sets the fixedType value for this TargetingParams.
     * 
     * @param fixedType
     */
    public void setFixedType(java.lang.String fixedType) {
        this.fixedType = fixedType;
    }


    /**
     * Gets the sex value for this TargetingParams.
     * 
     * @return sex
     */
    public java.lang.String getSex() {
        return sex;
    }


    /**
     * Sets the sex value for this TargetingParams.
     * 
     * @param sex
     */
    public void setSex(java.lang.String sex) {
        this.sex = sex;
    }


    /**
     * Gets the minAge value for this TargetingParams.
     * 
     * @return minAge
     */
    public java.lang.Integer getMinAge() {
        return minAge;
    }


    /**
     * Sets the minAge value for this TargetingParams.
     * 
     * @param minAge
     */
    public void setMinAge(java.lang.Integer minAge) {
        this.minAge = minAge;
    }


    /**
     * Gets the maxAge value for this TargetingParams.
     * 
     * @return maxAge
     */
    public java.lang.Integer getMaxAge() {
        return maxAge;
    }


    /**
     * Sets the maxAge value for this TargetingParams.
     * 
     * @param maxAge
     */
    public void setMaxAge(java.lang.Integer maxAge) {
        this.maxAge = maxAge;
    }


    /**
     * Gets the homeRegions value for this TargetingParams.
     * 
     * @return homeRegions
     */
    public int[] getHomeRegions() {
        return homeRegions;
    }


    /**
     * Sets the homeRegions value for this TargetingParams.
     * 
     * @param homeRegions
     */
    public void setHomeRegions(int[] homeRegions) {
        this.homeRegions = homeRegions;
    }

    public int getHomeRegions(int i) {
        return this.homeRegions[i];
    }

    public void setHomeRegions(int i, int _value) {
        this.homeRegions[i] = _value;
    }


    /**
     * Gets the addSubscribersSource value for this TargetingParams.
     * 
     * @return addSubscribersSource
     */
    public java.lang.String getAddSubscribersSource() {
        return addSubscribersSource;
    }


    /**
     * Sets the addSubscribersSource value for this TargetingParams.
     * 
     * @param addSubscribersSource
     */
    public void setAddSubscribersSource(java.lang.String addSubscribersSource) {
        this.addSubscribersSource = addSubscribersSource;
    }


    /**
     * Gets the addSubscribers value for this TargetingParams.
     * 
     * @return addSubscribers
     */
    public mobi.eyeline.ips.external.madv.FileInfo getAddSubscribers() {
        return addSubscribers;
    }


    /**
     * Sets the addSubscribers value for this TargetingParams.
     * 
     * @param addSubscribers
     */
    public void setAddSubscribers(mobi.eyeline.ips.external.madv.FileInfo addSubscribers) {
        this.addSubscribers = addSubscribers;
    }


    /**
     * Gets the subscriberSource value for this TargetingParams.
     * 
     * @return subscriberSource
     */
    public java.lang.String getSubscriberSource() {
        return subscriberSource;
    }


    /**
     * Sets the subscriberSource value for this TargetingParams.
     * 
     * @param subscriberSource
     */
    public void setSubscriberSource(java.lang.String subscriberSource) {
        this.subscriberSource = subscriberSource;
    }


    /**
     * Gets the subscribers value for this TargetingParams.
     * 
     * @return subscribers
     */
    public mobi.eyeline.ips.external.madv.FileInfo getSubscribers() {
        return subscribers;
    }


    /**
     * Sets the subscribers value for this TargetingParams.
     * 
     * @param subscribers
     */
    public void setSubscribers(mobi.eyeline.ips.external.madv.FileInfo subscribers) {
        this.subscribers = subscribers;
    }


    /**
     * Gets the targetingFilters value for this TargetingParams.
     * 
     * @return targetingFilters
     */
    public mobi.eyeline.ips.external.madv.TargetingFilterInfo[] getTargetingFilters() {
        return targetingFilters;
    }


    /**
     * Sets the targetingFilters value for this TargetingParams.
     * 
     * @param targetingFilters
     */
    public void setTargetingFilters(mobi.eyeline.ips.external.madv.TargetingFilterInfo[] targetingFilters) {
        this.targetingFilters = targetingFilters;
    }

    public mobi.eyeline.ips.external.madv.TargetingFilterInfo getTargetingFilters(int i) {
        return this.targetingFilters[i];
    }

    public void setTargetingFilters(int i, mobi.eyeline.ips.external.madv.TargetingFilterInfo _value) {
        this.targetingFilters[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TargetingParams)) return false;
        TargetingParams other = (TargetingParams) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.fixedType==null && other.getFixedType()==null) || 
             (this.fixedType!=null &&
              this.fixedType.equals(other.getFixedType()))) &&
            ((this.sex==null && other.getSex()==null) || 
             (this.sex!=null &&
              this.sex.equals(other.getSex()))) &&
            ((this.minAge==null && other.getMinAge()==null) || 
             (this.minAge!=null &&
              this.minAge.equals(other.getMinAge()))) &&
            ((this.maxAge==null && other.getMaxAge()==null) || 
             (this.maxAge!=null &&
              this.maxAge.equals(other.getMaxAge()))) &&
            ((this.homeRegions==null && other.getHomeRegions()==null) || 
             (this.homeRegions!=null &&
              java.util.Arrays.equals(this.homeRegions, other.getHomeRegions()))) &&
            ((this.addSubscribersSource==null && other.getAddSubscribersSource()==null) || 
             (this.addSubscribersSource!=null &&
              this.addSubscribersSource.equals(other.getAddSubscribersSource()))) &&
            ((this.addSubscribers==null && other.getAddSubscribers()==null) || 
             (this.addSubscribers!=null &&
              this.addSubscribers.equals(other.getAddSubscribers()))) &&
            ((this.subscriberSource==null && other.getSubscriberSource()==null) || 
             (this.subscriberSource!=null &&
              this.subscriberSource.equals(other.getSubscriberSource()))) &&
            ((this.subscribers==null && other.getSubscribers()==null) || 
             (this.subscribers!=null &&
              this.subscribers.equals(other.getSubscribers()))) &&
            ((this.targetingFilters==null && other.getTargetingFilters()==null) || 
             (this.targetingFilters!=null &&
              java.util.Arrays.equals(this.targetingFilters, other.getTargetingFilters())));
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
        if (getFixedType() != null) {
            _hashCode += getFixedType().hashCode();
        }
        if (getSex() != null) {
            _hashCode += getSex().hashCode();
        }
        if (getMinAge() != null) {
            _hashCode += getMinAge().hashCode();
        }
        if (getMaxAge() != null) {
            _hashCode += getMaxAge().hashCode();
        }
        if (getHomeRegions() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHomeRegions());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHomeRegions(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getAddSubscribersSource() != null) {
            _hashCode += getAddSubscribersSource().hashCode();
        }
        if (getAddSubscribers() != null) {
            _hashCode += getAddSubscribers().hashCode();
        }
        if (getSubscriberSource() != null) {
            _hashCode += getSubscriberSource().hashCode();
        }
        if (getSubscribers() != null) {
            _hashCode += getSubscribers().hashCode();
        }
        if (getTargetingFilters() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTargetingFilters());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTargetingFilters(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TargetingParams.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "targetingParams"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("fixedType");
        elemField.setXmlName(new javax.xml.namespace.QName("", "fixedType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sex");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sex"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minAge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "minAge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxAge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "maxAge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("homeRegions");
        elemField.setXmlName(new javax.xml.namespace.QName("", "homeRegions"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addSubscribersSource");
        elemField.setXmlName(new javax.xml.namespace.QName("", "addSubscribersSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addSubscribers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "addSubscribers"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "fileInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscriberSource");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subscriberSource"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("subscribers");
        elemField.setXmlName(new javax.xml.namespace.QName("", "subscribers"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "fileInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetingFilters");
        elemField.setXmlName(new javax.xml.namespace.QName("", "targetingFilters"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "targetingFilterInfo"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
