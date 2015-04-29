/**
 * 
 */
package com.alfresco.orm.vo;

import com.alfresco.orm.annotation.AlfrescoQName;

/**
 * @author Nishit C.
 *
 */
public class SampleAspect
{
	@AlfrescoQName(localName = "sampleStringAspect", namespaceURI = "http://alfresco.orm.com")
	private String	sampleStringAspect;
	@AlfrescoQName(localName = "sampleIntAspect", namespaceURI = "http://alfresco.orm.com")
	private int		sampleIntAspect;
	@AlfrescoQName(localName = "sampleFloatAspect", namespaceURI = "http://alfresco.orm.com")
	private float	sampleFloatAspect;

	/**
	 * @return the sampleStringAspect
	 */
	public String getSampleStringAspect()
	{
		return sampleStringAspect;
	}

	/**
	 * @param sampleStringAspect
	 *            the sampleStringAspect to set
	 */
	public void setSampleStringAspect(final String sampleStringAspect)
	{
		this.sampleStringAspect = sampleStringAspect;
	}

	/**
	 * @return the sampleIntAspect
	 */
	public int getSampleIntAspect()
	{
		return sampleIntAspect;
	}

	/**
	 * @param sampleIntAspect
	 *            the sampleIntAspect to set
	 */
	public void setSampleIntAspect(final int sampleIntAspect)
	{
		this.sampleIntAspect = sampleIntAspect;
	}

	/**
	 * @return the sampleFloatAspect
	 */
	public float getSampleFloatAspect()
	{
		return sampleFloatAspect;
	}

	/**
	 * @param sampleFloatAspect
	 *            the sampleFloatAspect to set
	 */
	public void setSampleFloatAspect(final float sampleFloatAspect)
	{
		this.sampleFloatAspect = sampleFloatAspect;
	}

}
