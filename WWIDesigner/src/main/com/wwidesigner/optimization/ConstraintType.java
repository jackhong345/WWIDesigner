//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.04.04 at 11:39:09 PM MDT 
//

package com.wwidesigner.optimization;

/**
 * <p>
 * Java class for ConstraintType.
 * 
 */
public enum ConstraintType
{

	BOOLEAN, INTEGER, DIMENSIONAL, DIMENSIONLESS;

	public String value()
	{
		return name();
	}

	public static ConstraintType fromValue(String v)
	{
		return valueOf(v);
	}

}
