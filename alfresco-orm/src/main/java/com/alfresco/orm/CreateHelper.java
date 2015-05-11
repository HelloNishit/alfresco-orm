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

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.AlfrescoType;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.mapping.AlfrescoContent;

public class CreateHelper
{
	private BeanFactory			beanFactory;
	private ServiceRegistry		serviceRegistry;
	private List<String>		restrictedPropertiesForUpdate	= Arrays.asList("node-uuid", "creator", "created", "modifier", "modified");
	private static CreateHelper	createHelper;

	public static CreateHelper init(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		if (null == createHelper)
		{
			createHelper = new CreateHelper(beanFactory, serviceRegistry);
		}
		return createHelper;
	}

	public static CreateHelper getCreateHelper()
	{
		return createHelper;
	}

	private CreateHelper(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void save(final AlfrescoContent alfrescoContent) throws ORMException
	{
		AlfrescoType alfrescoContentType = alfrescoContent.getClass().getAnnotation(AlfrescoType.class);
		AlfrescoQName alfrescoQName = alfrescoContent.getClass().getAnnotation(AlfrescoQName.class);
		try
		{
			NodeRef nodeRef = createContent(alfrescoContentType, alfrescoQName, alfrescoContent);
			if (null != nodeRef)
			{
				AlfrescoContent alfrescoVO = (AlfrescoContent) alfrescoContent;
				alfrescoVO.setNodeUUID(nodeRef.getId());
				Map<QName, Serializable> properties = ORMUtil.getAlfrescoProperty(alfrescoContent);
				ORMUtil.saveProperties(alfrescoContent, properties, serviceRegistry.getNodeService(), restrictedPropertiesForUpdate);
				ORMUtil.executeCustomeMethodForProperty(alfrescoContent, beanFactory);
				ORMUtil.executeAssociation(alfrescoContent, beanFactory, serviceRegistry);
			}
		} catch (SecurityException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (NoSuchMethodException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (IllegalArgumentException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (IllegalAccessException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (InvocationTargetException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (InstantiationException e)
		{
			throw new ORMException(e.getMessage(), e);
		}
	}

	/**
	 * @param alfrescoContentType
	 * @param nodeRef
	 * @return
	 * @throws ORMException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private NodeRef createContent(final AlfrescoType alfrescoContentType, final AlfrescoQName alfrescoQName, final AlfrescoContent alfrescoContent)
			throws ORMException, NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		NodeRef nodeRef = null;
		if (StringUtils.isEmpty(alfrescoContentType.getParentFolderMethodName()))
		{
			String createNodeMethodName = alfrescoContentType.createNodeMethodName();
			if (StringUtils.isEmpty(createNodeMethodName))
			{
				throw new ORMException("Please spacifiy getParentFolderMethodName or  createNodeMethodName ");
			} else
			{
				nodeRef = executeCreateNodeMethod(alfrescoContentType, alfrescoContent);
			}
		} else
		{
			NodeRef parentNodeRef = executeGetParentContent(alfrescoContentType, alfrescoContent);
			if (null != parentNodeRef && getServiceRegistry().getNodeService().exists(parentNodeRef))
			{
				QName fileQName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
				FileInfo fileInfo = getServiceRegistry().getFileFolderService().create(parentNodeRef, alfrescoContent.getName(), fileQName);
				nodeRef = fileInfo.getNodeRef();
			} else
			{
				throw new ORMException("Parent Folder is not found " + parentNodeRef == null ? "" : parentNodeRef.toString());
			}
		}
		return nodeRef;
	}

	/**
	 * Method to create node by using custom implementation
	 * 
	 * @param alfrescoType
	 * @return
	 * @throws ORMException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NodeRef executeCreateNodeMethod(final AlfrescoType alfrescoType, final AlfrescoORM alfrescoORM) throws ORMException, SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Object target = ORMUtil.getTargetServiceBean(alfrescoType.springBeanID(), beanFactory);
		Method createContentMethod = target.getClass().getMethod(alfrescoType.createNodeMethodName(), AlfrescoORM.class);
		return (NodeRef) createContentMethod.invoke(target, alfrescoORM);
	}

	/**
	 * Method to get patent content
	 * 
	 * @param alfrescoType
	 * @return
	 * @throws ORMException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public NodeRef executeGetParentContent(final AlfrescoType alfrescoType, final AlfrescoORM alfrescoORM) throws ORMException, SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		Object target = ORMUtil.getTargetServiceBean(alfrescoType.springBeanID(), beanFactory);
		Method getParentContentMethod = target.getClass().getMethod(alfrescoType.getParentFolderMethodName(), AlfrescoORM.class);
		return (NodeRef) getParentContentMethod.invoke(target, alfrescoORM);
	}

	/**
	 * @return the beanFactory
	 */
	public BeanFactory getBeanFactory()
	{
		return beanFactory;
	}

	/**
	 * @param beanFactory
	 *            the beanFactory to set
	 */
	public void setBeanFactory(BeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}

	/**
	 * @return the serviceRegistry
	 */
	public ServiceRegistry getServiceRegistry()
	{
		return serviceRegistry;
	}

	/**
	 * @param serviceRegistry
	 *            the serviceRegistry to set
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry)
	{
		this.serviceRegistry = serviceRegistry;
	}

}
