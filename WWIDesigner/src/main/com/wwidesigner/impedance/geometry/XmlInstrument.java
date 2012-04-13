//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2-70- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.28 at 02:04:37 PM MDT 
//


package com.wwidesigner.impedance.geometry;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for Flute complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Flute">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="lengthType" type="{http://www.wwidesigner.com/Geometry}LengthType"/>
 *         &lt;element name="embouchure" type="{http://www.wwidesigner.com/Geometry}Embouchure" minOccurs="0"/>
 *         &lt;element name="borePoint" type="{http://www.wwidesigner.com/Geometry}BorePoint" maxOccurs="unbounded"/>
 *         &lt;element name="termination" type="{http://www.wwidesigner.com/Geometry}EndBoreSection"/>
 *       &lt;/sequence>
 *       &lt;attribute name="description" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Flute", propOrder = {
    "mLengthType",
    "mEmbouchure",
    "mBorePoint",
    "mTermination"
})
public class XmlInstrument {

    @XmlElement(name="lengthType", required = true)
    protected LengthType mLengthType;
    @XmlElement(name="embouchure")
    protected Embouchure mEmbouchure;
    @XmlElement(name="borePoint", required = true)
    protected List<BorePoint> mBorePoint;
    @XmlElement(name="termination", required = true)
    protected EndBoreSection mTermination;
    @XmlAttribute(name = "description")
    protected String mDescription;

    /**
     * Gets the value of the mLengthType property.
     * 
     * @return
     *     possible object is
     *     {@link LengthType }
     *     
     */
    public LengthType getLengthType() {
        return mLengthType;
    }

    /**
     * Sets the value of the mLengthType property.
     * 
     * @param value
     *     allowed object is
     *     {@link LengthType }
     *     
     */
    public void setLengthType(LengthType value) {
        this.mLengthType = value;
    }

    /**
     * Gets the value of the mEmbouchure property.
     * 
     * @return
     *     possible object is
     *     {@link Embouchure }
     *     
     */
    public Embouchure getEmbouchure() {
        return mEmbouchure;
    }

    /**
     * Sets the value of the mEmbouchure property.
     * 
     * @param value
     *     allowed object is
     *     {@link Embouchure }
     *     
     */
    public void setEmbouchure(Embouchure value) {
        this.mEmbouchure = value;
    }

    /**
     * Gets the value of the mBorePoint property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mBorePoint property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBorePoint().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BorePoint }
     * 
     * 
     */
    public List<BorePoint> getBorePoint() {
        if (mBorePoint == null) {
            mBorePoint = new ArrayList<BorePoint>();
        }
        return this.mBorePoint;
    }

    /**
     * Gets the value of the mTermination property.
     * 
     * @return
     *     possible object is
     *     {@link EndBoreSection }
     *     
     */
    public EndBoreSection getTermination() {
        return mTermination;
    }

    /**
     * Sets the value of the mTermination property.
     * 
     * @param value
     *     allowed object is
     *     {@link EndBoreSection }
     *     
     */
    public void setTermination(EndBoreSection value) {
        this.mTermination = value;
    }

    /**
     * Gets the value of the mDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return mDescription;
    }

    /**
     * Sets the value of the mDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.mDescription = value;
    }

}
