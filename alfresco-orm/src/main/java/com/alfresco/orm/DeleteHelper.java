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
package com.alfresco.orm;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;

public class DeleteHelper
{
	private BeanFactory			beanFactory;
	private ServiceRegistry		serviceRegistry;

	private static DeleteHelper	deleteHelper;

	public static DeleteHelper init(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		if (null == deleteHelper)
		{
			deleteHelper = new DeleteHelper(beanFactory, serviceRegistry);
		}
		return deleteHelper;
	}

	public static DeleteHelper getDeleteHelper()
	{
		return deleteHelper;
	}

	private DeleteHelper(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void delete(final AlfrescoORM alfrescoORM) throws ORMException
	{
		NodeRef nodeRef = ORMUtil.getNodeRef(alfrescoORM);
		serviceRegistry.getNodeService().deleteNode(nodeRef);
	}

}
