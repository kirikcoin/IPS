/**
 * ClickServiceInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package mobi.eyeline.ips.external.madv;

public class ClickServiceInfo  implements java.io.Serializable {
    private java.lang.Integer id;

    private java.lang.String additionalText;

    private java.lang.String outShortcut;

    private java.lang.String text;

    private java.lang.String inShortcut;

    public ClickServiceInfo() {
    }

    public ClickServiceInfo(
           java.lang.Integer id,
           java.lang.String additionalText,
           java.lang.String outShortcut,
           java.lang.String text,
           java.lang.String inShortcut) {
           this.id = id;
           this.additionalText = additionalText;
           this.outShortcut = outShortcut;
           this.text = text;
           this.inShortcut = inShortcut;
    }


    /**
     * Gets the id value for this ClickServiceInfo.
     * 
     * @return id
     */
    public java.lang.Integer getId() {
        return id;
    }


    /**
     * Sets the id value for this ClickServiceInfo.
     * 
     * @param id
     */
    public void setId(java.lang.Integer id) {
        this.id = id;
    }


    /**
     * Gets the additionalText value for this ClickServiceInfo.
     * 
     * @return additionalText
     */
    public java.lang.String getAdditionalText() {
        return additionalText;
    }


    /**
     * Sets the additionalText value for this ClickServiceInfo.
     * 
     * @param additionalText
     */
    public void setAdditionalText(java.lang.String additionalText) {
        this.additionalText = additionalText;
    }


    /**
     * Gets the outShortcut value for this ClickServiceInfo.
     * 
     * @return outShortcut
     */
    public java.lang.String getOutShortcut() {
        return outShortcut;
    }


    /**
     * Sets the outShortcut value for this ClickServiceInfo.
     * 
     * @param outShortcut
     */
    public void setOutShortcut(java.lang.String outShortcut) {
        this.outShortcut = outShortcut;
    }


    /**
     * Gets the text value for this ClickServiceInfo.
     * 
     * @return text
     */
    public java.lang.String getText() {
        return text;
    }


    /**
     * Sets the text value for this ClickServiceInfo.
     * 
     * @param text
     */
    public void setText(java.lang.String text) {
        this.text = text;
    }


    /**
     * Gets the inShortcut value for this ClickServiceInfo.
     * 
     * @return inShortcut
     */
    public java.lang.String getInShortcut() {
        return inShortcut;
    }


    /**
     * Sets the inShortcut value for this ClickServiceInfo.
     * 
     * @param inShortcut
     */
    public void setInShortcut(java.lang.String inShortcut) {
        this.inShortcut = inShortcut;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ClickServiceInfo)) return false;
        ClickServiceInfo other = (ClickServiceInfo) obj;
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
            ((this.additionalText==null && other.getAdditionalText()==null) || 
             (this.additionalText!=null &&
              this.additionalText.equals(other.getAdditionalText()))) &&
            ((this.outShortcut==null && other.getOutShortcut()==null) || 
             (this.outShortcut!=null &&
              this.outShortcut.equals(other.getOutShortcut()))) &&
            ((this.text==null && other.getText()==null) || 
             (this.text!=null &&
              this.text.equals(other.getText()))) &&
            ((this.inShortcut==null && other.getInShortcut()==null) || 
             (this.inShortcut!=null &&
              this.inShortcut.equals(other.getInShortcut())));
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
        if (getAdditionalText() != null) {
            _hashCode += getAdditionalText().hashCode();
        }
        if (getOutShortcut() != null) {
            _hashCode += getOutShortcut().hashCode();
        }
        if (getText() != null) {
            _hashCode += getText().hashCode();
        }
        if (getInShortcut() != null) {
            _hashCode += getInShortcut().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ClickServiceInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://mts-madv.eyeline.mobi", "clickServiceInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("additionalText");
        elemField.setXmlName(new javax.xml.namespace.QName("", "additionalText"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("outShortcut");
        elemField.setXmlName(new javax.xml.namespace.QName("", "outShortcut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("text");
        elemField.setXmlName(new javax.xml.namespace.QName("", "text"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inShortcut");
        elemField.setXmlName(new javax.xml.namespace.QName("", "inShortcut"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
