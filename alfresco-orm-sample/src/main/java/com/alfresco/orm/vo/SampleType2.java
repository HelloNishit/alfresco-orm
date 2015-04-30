/**
 * 
 */
package com.alfresco.orm.vo;

import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.AlfrescoType;
import com.alfresco.orm.annotation.SpringBeanID;
import com.alfresco.orm.mapping.AlfrescoContent;

/**
 * @author Nishit C.
 * 
 */

@AlfrescoType(springBeanID = @SpringBeanID(value = "sampleTypeService"), getParentFolderMethodName = "getParentContentForSmapleType")
@AlfrescoQName(localName = "sampleType2", namespaceURI = "http://alfresco.orm.com")
public class SampleType2 extends AlfrescoContent
{
	@AlfrescoQName(localName = "sampleString21", namespaceURI = "http://alfresco.orm.com")
	private String	sampleString21;
	@AlfrescoQName(localName = "sampleString22", namespaceURI = "http://alfresco.orm.com")
	private String	sampleString22;

	/**
	 * @return the sampleString21
	 */
	public String getSampleString21()
	{
		return sampleString21;
	}

	/**
	 * @param sampleString21
	 *            the sampleString21 to set
	 */
	public void setSampleString21(String sampleString21)
	{
		this.sampleString21 = sampleString21;
	}

	/**
	 * @return the sampleString22
	 */
	public String getSampleString22()
	{
		return sampleString22;
	}

	/**
	 * @param sampleString22
	 *            the sampleString22 to set
	 */
	public void setSampleString22(String sampleString22)
	{
		this.sampleString22 = sampleString22;
	}

}
