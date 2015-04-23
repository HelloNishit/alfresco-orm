/*******************************************************************************
 * Copyright 2015 Nishit C
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
package com.alfresco.orm.vo;

import org.alfresco.service.namespace.NamespaceService;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.annotation.Property;
/**
 * 
 * @author Nishit C.
 *
 */
public class AlfrescoVO implements AlfrescoORM {
	private String nodeUUID;
	@Property(localName = "name", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String name;
	@Property(localName = "title", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String title;
	@Property(localName = "description", namespaceURI = NamespaceService.CONTENT_MODEL_1_0_URI)
	private String description;

	public AlfrescoVO() {
	}

	/**
	 * @return the nodeUUID
	 */
	public String getNodeUUID() {
		return nodeUUID;
	}

	/**
	 * @param nodeUUID
	 *            the nodeUUID to set
	 */
	public void setNodeUUID(String nodeUUID) {
		this.nodeUUID = nodeUUID;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
