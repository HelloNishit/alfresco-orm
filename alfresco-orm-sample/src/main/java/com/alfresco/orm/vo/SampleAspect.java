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
