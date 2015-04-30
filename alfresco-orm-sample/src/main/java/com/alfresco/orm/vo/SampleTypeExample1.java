/**
 * 
 */
package com.alfresco.orm.vo;

import java.util.List;

import com.alfresco.orm.annotation.AlfrescoAspect;
import com.alfresco.orm.annotation.AlfrescoAssociation;
import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.AlfrescoType;
import com.alfresco.orm.annotation.SpringBeanID;
import com.alfresco.orm.mapping.AlfrescoContent;
import com.alfresco.orm.vo.SampleAspect;

/**
 * @author Nishit C.
 * 
 */

@AlfrescoType(springBeanID = @SpringBeanID(value = "sampleTypeService"), getParentFolderMethodName = "getParentContentForSmapleType")
@AlfrescoQName(localName = "sampleType", namespaceURI = "http://alfresco.orm.com")
public class SampleTypeExample1 extends AlfrescoContent
{
	@AlfrescoQName(localName = "sampleString", namespaceURI = "http://alfresco.orm.com")
	private String				sampleString;
	@AlfrescoQName(localName = "sampleInt", namespaceURI = "http://alfresco.orm.com")
	private int					sampleInt;
	@AlfrescoQName(localName = "sampleFloat", namespaceURI = "http://alfresco.orm.com")
	private float				sampleFloat;
	@AlfrescoQName(localName = "language", namespaceURI = "http://alfresco.orm.com")
	private String				language;

	@AlfrescoAssociation(many=true)
	private List<SampleType1>	sampleType1;
	@AlfrescoAssociation(many=false)
	private SampleType2			sampleType2;

	@AlfrescoAspect()
	private SampleAspect		sampleAspect;

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

	/**
	 * @return the sampleType1
	 */
	public List<SampleType1> getSampleType1()
	{
		return sampleType1;
	}

	/**
	 * @param sampleType1
	 *            the sampleType1 to set
	 */
	public void setSampleType1(List<SampleType1> sampleType1)
	{
		this.sampleType1 = sampleType1;
	}

	/**
	 * @return the sampleType2
	 */
	public SampleType2 getSampleType2()
	{
		return sampleType2;
	}

	/**
	 * @param sampleType2
	 *            the sampleType2 to set
	 */
	public void setSampleType2(SampleType2 sampleType2)
	{
		this.sampleType2 = sampleType2;
	}

}
