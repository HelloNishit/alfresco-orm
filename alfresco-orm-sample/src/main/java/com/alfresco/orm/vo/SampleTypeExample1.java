/**
 * 
 */
package com.alfresco.orm.vo;

import com.alfresco.orm.annotation.AlfrescoAspect;
import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.AlfrescoType;
import com.alfresco.orm.annotation.SpringBeanID;

/**
 * @author Nishit C.
 *
 */

@AlfrescoType(springBeanID = @SpringBeanID(value = "sampleTypeService"), getParentFolderMethodName = "getParentContentForSmapleType")
@AlfrescoQName(localName = "sampleType", namespaceURI = "http://alfresco.orm.com")
public class SampleTypeExample1 extends AlfrescoContent
{
	@AlfrescoQName(localName = "sampleString", namespaceURI = "http://alfresco.orm.com")
	private String			sampleString;
	@AlfrescoQName(localName = "sampleInt", namespaceURI = "http://alfresco.orm.com")
	private int				sampleInt;
	@AlfrescoQName(localName = "sampleFloat", namespaceURI = "http://alfresco.orm.com")
	private float			sampleFloat;
	@AlfrescoQName(localName = "language", namespaceURI = "http://alfresco.orm.com")
	private String			language;

	@AlfrescoAspect()
	private SampleAspect	sampleAspect;

	/**
	 * @return the sampleString
	 */
	public String getSampleString()
	{
		return sampleString;
	}

	/**
	 * @param sampleString
	 *            the sampleString to set
	 */
	public void setSampleString(final String sampleString)
	{
		this.sampleString = sampleString;
	}

	/**
	 * @return the sampleInt
	 */
	public int getSampleInt()
	{
		return sampleInt;
	}

	/**
	 * @param sampleInt
	 *            the sampleInt to set
	 */
	public void setSampleInt(final int sampleInt)
	{
		this.sampleInt = sampleInt;
	}

	/**
	 * @return the sampleFloat
	 */
	public float getSampleFloat()
	{
		return sampleFloat;
	}

	/**
	 * @param sampleFloat
	 *            the sampleFloat to set
	 */
	public void setSampleFloat(final float sampleFloat)
	{
		this.sampleFloat = sampleFloat;
	}

	/**
	 * @return the language
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(final String language)
	{
		this.language = language;
	}

	/**
	 * @return the sampleAspect
	 */
	public SampleAspect getSampleAspect()
	{
		return sampleAspect;
	}

	/**
	 * @param sampleAspect
	 *            the sanpleAspect to set
	 */
	public void setSampleAspect(final SampleAspect sampleAspect)
	{
		this.sampleAspect = sampleAspect;
	}

}
