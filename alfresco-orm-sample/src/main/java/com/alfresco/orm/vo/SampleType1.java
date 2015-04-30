/*******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
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
