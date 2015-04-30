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
@AlfrescoQName(localName = "sampleType1", namespaceURI = "http://alfresco.orm.com")
public class SampleType1 extends AlfrescoContent
{
	@AlfrescoQName(localName = "sampleString11", namespaceURI = "http://alfresco.orm.com")
	private String	sampleString11;
	@AlfrescoQName(localName = "sampleString12", namespaceURI = "http://alfresco.orm.com")
	private String	sampleString12;

	/**
	 * @return the sampleString11
	 */
	public String getSampleString11()
	{
		return sampleString11;
	}

	/**
	 * @param sampleString11
	 *            the sampleString11 to set
	 */
	public void setSampleString11(String sampleString11)
	{
		this.sampleString11 = sampleString11;
	}

	/**
	 * @return the sampleString12
	 */
	public String getSampleString12()
	{
		return sampleString12;
	}

	/**
	 * @param sampleString12
	 *            the sampleString12 to set
	 */
	public void setSampleString12(String sampleString12)
	{
		this.sampleString12 = sampleString12;
	}

}
