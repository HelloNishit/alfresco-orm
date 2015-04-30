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
package com.alfresco.orm.mapping;

import java.util.Date;

import org.alfresco.service.namespace.NamespaceService;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.annotation.AlfrescoQName;

/**
 * 
 * @author Nishit C.
 * 
 */
public class AlfrescoContent implements AlfrescoORM
{
	@AlfrescoQName(localName = "node-uuid", namespaceURI = NamespaceService.SYSTEM_MODEL_1_0_URI)
	private String	nodeUUID;
	@AlfrescoQName(localName = "name", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String	name;
	@AlfrescoQName(localName = "title", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String	title;
	@AlfrescoQName(localName = "description", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String	description;
	@AlfrescoQName(localName = "creator", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String	creator;
	@AlfrescoQName(localName = "created", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private Date	createdDate;
	@AlfrescoQName(localName = "modifier", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String	modifier;
	@AlfrescoQName(localName = "modified", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private Date	modifiedDate;

	public AlfrescoContent()
	{
	}

	/**
	 * @return the nodeUUID
	 */
	public String getNodeUUID()
	{
		return nodeUUID;
	}

	/**
	 * @param nodeUUID
	 *            the nodeUUID to set
	 */
	public void setNodeUUID(final String nodeUUID)
	{
		this.nodeUUID = nodeUUID;
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(final String title)
	{
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return the creator
	 */
	public String getCreator()
	{
		return creator;
	}

	/**
	 * @param creator
	 *            the creator to set
	 */
	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate()
	{
		return createdDate;
	}

	/**
	 * @param createdDate
	 *            the createdDate to set
	 */
	public void setCreatedDate(Date createdDate)
	{
		this.createdDate = createdDate;
	}

	/**
	 * @return the modifier
	 */
	public String getModifier()
	{
		return modifier;
	}

	/**
	 * @param modifier
	 *            the modifier to set
	 */
	public void setModifier(String modifier)
	{
		this.modifier = modifier;
	}

	/**
	 * @return the modifiedDate
	 */
	public Date getModifiedDate()
	{
		return modifiedDate;
	}

	/**
	 * @param modifiedDate
	 *            the modifiedDate to set
	 */
	public void setModifiedDate(Date modifiedDate)
	{
		this.modifiedDate = modifiedDate;
	}

}
