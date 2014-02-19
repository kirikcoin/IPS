/**
 * TargetingFilterInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class TargetingFilterInfo  implements java.io.Serializable {
    private int targetingFilterTypeId;

    private java.lang.String[] stringFilterValues;

    private int[] tagFilterValuesIds;

    private boolean includeValues;

    public TargetingFilterInfo() {
    }

    public TargetingFilterInfo(
           int targetingFilterTypeId,
           java.lang.String[] stringFilterValues,
           int[] tagFilterValuesIds,
           boolean includeValues) {
           this.targetingFilterTypeId = targetingFilterTypeId;
           this.stringFilterValues = stringFilterValues;
           this.tagFilterValuesIds = tagFilterValuesIds;
           this.includeValues = includeValues;
    }


    /**
     * Gets the targetingFilterTypeId value for this TargetingFilterInfo.
     * 
     * @return targetingFilterTypeId
     */
    public int getTargetingFilterTypeId() {
        return targetingFilterTypeId;
    }


    /**
     * Sets the targetingFilterTypeId value for this TargetingFilterInfo.
     * 
     * @param targetingFilterTypeId
     */
    public void setTargetingFilterTypeId(int targetingFilterTypeId) {
        this.targetingFilterTypeId = targetingFilterTypeId;
    }


    /**
     * Gets the stringFilterValues value for this TargetingFilterInfo.
     * 
     * @return stringFilterValues
     */
    public java.lang.String[] getStringFilterValues() {
        return stringFilterValues;
    }


    /**
     * Sets the stringFilterValues value for this TargetingFilterInfo.
     * 
     * @param stringFilterValues
     */
    public void setStringFilterValues(java.lang.String[] stringFilterValues) {
        this.stringFilterValues = stringFilterValues;
    }

    public java.lang.String getStringFilterValues(int i) {
        return this.stringFilterValues[i];
    }

    public void setStringFilterValues(int i, java.lang.String _value) {
        this.stringFilterValues[i] = _value;
    }


    /**
     * Gets the tagFilterValuesIds value for this TargetingFilterInfo.
     * 
     * @return tagFilterValuesIds
     */
    public int[] getTagFilterValuesIds() {
        return tagFilterValuesIds;
    }


    /**
     * Sets the tagFilterValuesIds value for this TargetingFilterInfo.
     * 
     * @param tagFilterValuesIds
     */
    public void setTagFilterValuesIds(int[] tagFilterValuesIds) {
        this.tagFilterValuesIds = tagFilterValuesIds;
    }

    public int getTagFilterValuesIds(int i) {
        return this.tagFilterValuesIds[i];
    }

    public void setTagFilterValuesIds(int i, int _value) {
        this.tagFilterValuesIds[i] = _value;
    }


    /**
     * Gets the includeValues value for this TargetingFilterInfo.
     * 
     * @return includeValues
     */
    public boolean isIncludeValues() {
        return includeValues;
    }


    /**
     * Sets the includeValues value for this TargetingFilterInfo.
     * 
     * @param includeValues
     */
    public void setIncludeValues(boolean includeValues) {
        this.includeValues = includeValues;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TargetingFilterInfo)) return false;
        TargetingFilterInfo other = (TargetingFilterInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            this.targetingFilterTypeId == other.getTargetingFilterTypeId() &&
            ((this.stringFilterValues==null && other.getStringFilterValues()==null) || 
             (this.stringFilterValues!=null &&
              java.util.Arrays.equals(this.stringFilterValues, other.getStringFilterValues()))) &&
            ((this.tagFilterValuesIds==null && other.getTagFilterValuesIds()==null) || 
             (this.tagFilterValuesIds!=null &&
              java.util.Arrays.equals(this.tagFilterValuesIds, other.getTagFilterValuesIds()))) &&
            this.includeValues == other.isIncludeValues();
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
        _hashCode += getTargetingFilterTypeId();
        if (getStringFilterValues() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getStringFilterValues());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getStringFilterValues(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getTagFilterValuesIds() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTagFilterValuesIds());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTagFilterValuesIds(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += (isIncludeValues() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TargetingFilterInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "targetingFilterInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targetingFilterTypeId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "targetingFilterTypeId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("stringFilterValues");
        elemField.setXmlName(new javax.xml.namespace.QName("", "stringFilterValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tagFilterValuesIds");
        elemField.setXmlName(new javax.xml.namespace.QName("", "tagFilterValuesIds"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("includeValues");
        elemField.setXmlName(new javax.xml.namespace.QName("", "includeValues"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
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
