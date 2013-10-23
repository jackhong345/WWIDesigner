//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.04 at 11:39:09 PM MDT 
//

package com.wwidesigner.optimization.bind;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The bounds and metadata for an optimizer
 * 
 * 
 * <p>
 * Java class for Constraints complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Constraints">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="constraintsName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="objectiveDisplayName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="objectiveFunctionName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="numberOfHoles" type="{http://www.w3.org/2001/XMLSchema}integer"/>
 *         &lt;element name="constraint" type="{http://www.wwidesigner.com/Constraints}Constraint" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Constraints", propOrder = { "constraintsName",
		"objectiveDisplayName", "objectiveFunctionName", "numberOfHoles",
		"constraint" })
public class Constraints
{

	@XmlElement(required = true)
	protected String constraintsName;
	@XmlElement(required = true)
	protected String objectiveDisplayName;
	@XmlElement(required = true)
	protected String objectiveFunctionName;
	@XmlElement(required = true)
	protected int numberOfHoles;
	@XmlElement(required = true)
	protected List<Constraint> constraint;

	/**
	 * Gets the value of the constraintsName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getConstraintsName()
	{
		return constraintsName;
	}

	/**
	 * Sets the value of the constraintsName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setConstraintsName(String value)
	{
		this.constraintsName = value;
	}

	/**
	 * Gets the value of the objectiveDisplayName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getObjectiveDisplayName()
	{
		return objectiveDisplayName;
	}

	/**
	 * Sets the value of the objectiveDisplayName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setObjectiveDisplayName(String value)
	{
		this.objectiveDisplayName = value;
	}

	/**
	 * Gets the value of the objectiveFunctionName property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getObjectiveFunctionName()
	{
		return objectiveFunctionName;
	}

	/**
	 * Sets the value of the objectiveFunctionName property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setObjectiveFunctionName(String value)
	{
		this.objectiveFunctionName = value;
	}

	/**
	 * Gets the value of the numberOfHoles property.
	 * 
	 * @return possible object is {@link BigInteger }
	 * 
	 */
	public int getNumberOfHoles()
	{
		return numberOfHoles;
	}

	/**
	 * Sets the value of the numberOfHoles property.
	 * 
	 * @param value
	 *            allowed object is {@link BigInteger }
	 * 
	 */
	public void setNumberOfHoles(int value)
	{
		this.numberOfHoles = value;
	}

	/**
	 * Gets the value of the constraint property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the constraint property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getConstraint().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link Constraint }
	 * 
	 * 
	 */
	public List<Constraint> getConstraint()
	{
		if (constraint == null)
		{
			constraint = new ArrayList<Constraint>();
		}
		return this.constraint;
	}

	public void setConstraint(List<Constraint> constraint)
	{
		this.constraint = constraint;
	}

}