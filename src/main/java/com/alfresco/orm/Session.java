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

package com.alfresco.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.annotation.AlfrescoType;
import com.alfresco.orm.annotation.CustomProperty;
import com.alfresco.orm.annotation.Property;
import com.alfresco.orm.annotation.SpringBeanID;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.reflection.ReflectionUtil;
import com.alfresco.orm.vo.AlfrescoVO;

/**
 * 
 * @author Nishit C.
 *
 */
public class Session {
	private BeanFactory beanFactory;
	private ServiceRegistry serviceRegistry;

	public void save(AlfrescoORM alfrescoORM) throws ORMException {
		AlfrescoType alfrescoContent = alfrescoORM.getClass().getAnnotation(
				AlfrescoType.class);
		try {
			Map<QName, Serializable> properties = getAlfrescoProperty(alfrescoORM);
			NodeRef nodeRef = createContent(alfrescoContent, properties,
					alfrescoORM);

			if (null != nodeRef) {
				AlfrescoVO alfrescoVO = (AlfrescoVO) alfrescoORM;
				alfrescoVO.setNodeUUID(nodeRef.toString());
				getServiceRegistry().getNodeService().setProperties(nodeRef,
						properties);
				executeCustomeMethodForProperty(alfrescoORM, nodeRef);
			}
		} catch (SecurityException e) {
			throw new ORMException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new ORMException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new ORMException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new ORMException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new ORMException(e.getMessage(), e);
		}
	}

	/**
	 * @param alfrescoContent
	 * @return
	 * @throws ORMException
	 */
	private Object getTargetServiceBean(SpringBeanID springBeanID)
			throws ORMException {
		String springBeanId = springBeanID.value();
		if (StringUtils.isEmpty(springBeanId)) {
			throw new ORMException("Please provide springBeanId");
		}
		Object target = beanFactory.getBean(springBeanId);
		return target;
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
	public NodeRef getParentContent(AlfrescoType alfrescoType,
			AlfrescoORM alfrescoORM) throws ORMException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Object target = getTargetServiceBean(alfrescoType.springBeanID());
		Method getParentContentMethod = target.getClass().getMethod(
				alfrescoType.getParentFolderMethodName(), AlfrescoORM.class);
		return (NodeRef) getParentContentMethod.invoke(target, alfrescoORM);
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
	public NodeRef createNodeMethodName(AlfrescoType alfrescoType,
			AlfrescoORM alfrescoORM) throws ORMException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		Object target = getTargetServiceBean(alfrescoType.springBeanID());
		Method createContentMethod = target.getClass().getMethod(
				alfrescoType.createNodeMethodName(), AlfrescoORM.class);
		return (NodeRef) createContentMethod.invoke(target, alfrescoORM);
	}

	/**
	 * @param alfrescoContent
	 * @param properties
	 * @param nodeRef
	 * @return
	 * @throws ORMException
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private NodeRef createContent(AlfrescoType alfrescoContent,
			Map<QName, Serializable> properties, AlfrescoORM alfrescoORM)
			throws ORMException, NoSuchMethodException, IllegalAccessException,
			InvocationTargetException {
		NodeRef nodeRef = null;
		if (StringUtils.isEmpty(alfrescoContent.getParentFolderMethodName())) {
			String createNodeMethodName = alfrescoContent
					.createNodeMethodName();
			if (StringUtils.isEmpty(createNodeMethodName)) {
				throw new ORMException(
						"Please spacifiy getParentFolderMethodName or  createNodeMethodName ");
			} else {
				nodeRef = createNodeMethodName(alfrescoContent, alfrescoORM);
			}
		} else {
			NodeRef parentNodeRef = getParentContent(alfrescoContent,
					alfrescoORM);
			if (null != parentNodeRef
					&& getServiceRegistry().getNodeService().exists(
							parentNodeRef)) {
				QName fileQName = QName.createQName(
						alfrescoContent.namespaceURI(),
						alfrescoContent.localName());
				FileInfo fileInfo = getServiceRegistry()
						.getFileFolderService()
						.create(parentNodeRef,
								(String) properties.get(ContentModel.PROP_NAME),
								fileQName);
				nodeRef = fileInfo.getNodeRef();
			} else {
				throw new ORMException("Parent Folder is not found "
						+ parentNodeRef == null ? "" : parentNodeRef.toString());
			}
		}
		return nodeRef;
	}

	public void executeCustomeMethodForProperty(AlfrescoORM alfrescoORM,
			NodeRef nodeRef) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ORMException {
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);
		for (Field field : fields) {
			if (field.isAnnotationPresent(CustomProperty.class)) {
				CustomProperty customProperty = field
						.getAnnotation(CustomProperty.class);
				if (StringUtils.isNotEmpty(customProperty
						.setPropertMethodName())) {
					Object target = getTargetServiceBean(customProperty
							.springBeanID());
					Method customeMethod = target.getClass().getMethod(
							customProperty.setPropertMethodName(),
							NodeRef.class, AlfrescoORM.class);
					customeMethod.invoke(target, nodeRef, alfrescoORM);
				} else {
					throw new ORMException(
							"Please set cutome method name to set property");
				}
			}
		}
	}

	public Map<QName, Serializable> getAlfrescoProperty(AlfrescoORM alfrescoORM)
			throws IllegalArgumentException, IllegalAccessException,
			SecurityException, NoSuchMethodException, InvocationTargetException {
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);
		Map<QName, Serializable> retVal = new HashMap<QName, Serializable>(
				fields.size());
		for (Field field : fields) {
			if (field.isAnnotationPresent(Property.class)) {
				Property property = field.getAnnotation(Property.class);
				QName qName = QName.createQName(property.namespaceURI(),
						property.localName());
				Method getterMethod = ReflectionUtil.getMethod(
						alfrescoORM.getClass(), field.getName());
				retVal.put(qName,
						(Serializable) getterMethod.invoke(alfrescoORM));
			}
		}
		return retVal;
	}

	/**
	 * @return the serviceRegistry
	 */
	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	private ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public void close() {

	}

}
