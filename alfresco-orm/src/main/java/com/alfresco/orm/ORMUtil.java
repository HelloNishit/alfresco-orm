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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.annotation.AlfrescoAspect;
import com.alfresco.orm.annotation.AlfrescoAssociation;
import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.SetProperty;
import com.alfresco.orm.annotation.SpringBeanID;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.mapping.AlfrescoContent;
import com.alfresco.orm.reflection.ReflectionUtil;

public abstract class ORMUtil
{
	public static Map<QName, Serializable> getAlfrescoProperty(final AlfrescoORM alfrescoORM) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException, InstantiationException
	{
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);
		Map<QName, Serializable> retVal = new HashMap<QName, Serializable>(fields.size());
		for (Field field : fields)
		{
			if (!field.isAnnotationPresent(SetProperty.class))
			{
				if (field.isAnnotationPresent(AlfrescoQName.class) && !field.isAnnotationPresent(AlfrescoAssociation.class))
				{
					AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
					QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
					Method getterMethod = ReflectionUtil.getMethod(alfrescoORM.getClass(), field.getName());
					retVal.put(qName, (Serializable) getterMethod.invoke(alfrescoORM));
				} else if (field.isAnnotationPresent(AlfrescoAspect.class))
				{
					Method getterMethod = ReflectionUtil.getMethod(alfrescoORM.getClass(), field.getName());					
					AlfrescoORM aspect = (AlfrescoORM) getterMethod.invoke(alfrescoORM) ;
					if(null != aspect)
					{
						retVal.putAll(getAlfrescoProperty(aspect));
					}
				}
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @param alfrescoContent
	 * @param properties
	 * @param nodeService
	 * @param restrictedPropertiesForUpdate
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ORMException
	 */
	public static void saveProperties(final AlfrescoContent alfrescoContent, final Map<QName, Serializable> properties, final NodeService nodeService,
			final List<String> restrictedPropertiesForUpdate) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
			ORMException
	{
		NodeRef nodeRef = getNodeRef(alfrescoContent);
		Map<QName, Serializable> propertiesFinal = new HashMap<QName, Serializable>(properties.size());
		for (Entry<QName, Serializable> prop : properties.entrySet())
		{
			if (!restrictedPropertiesForUpdate.contains(prop.getKey().getLocalName()))
			{
				propertiesFinal.put(prop.getKey(), prop.getValue());
			}
		}

		nodeService.setProperties(nodeRef, properties);
	}

	/**
	 * 
	 * @param alfrescoContent
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ORMException
	 */
	public static void executeCustomeMethodForProperty(final AlfrescoContent alfrescoContent, final BeanFactory beanFactory) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ORMException
	{
		NodeRef nodeRef = getNodeRef(alfrescoContent);
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoContent.getClass(), fields);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(SetProperty.class))
			{
				SetProperty setProperty = field.getAnnotation(SetProperty.class);
				if (StringUtils.isNotEmpty(setProperty.setPropertMethodName()))
				{
					Object target = getTargetServiceBean(setProperty.springBeanID(), beanFactory);
					Method customeMethod = target.getClass().getMethod(setProperty.setPropertMethodName(), NodeRef.class, AlfrescoORM.class);
					customeMethod.invoke(target, nodeRef, alfrescoContent);
				} else
				{
					throw new ORMException("Please set cutome method name to set property");
				}
			}
		}
	}

	public static void executeAssociation(final AlfrescoContent alfrescoContent, final BeanFactory beanFactory, final ServiceRegistry serviceRegistry)
			throws ORMException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		NodeRef nodeRef = getNodeRef(alfrescoContent);
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoContent.getClass(), fields);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(AlfrescoAssociation.class))
			{
				AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
				if (null == alfrescoQName)
				{
					throw new ORMException("please add alfresco quname aspect to the association field: " + field );
				}
				List<AlfrescoContent> associationAlfrescoORMs = getAsscoiationObject(alfrescoContent, field);
				QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
				removeAllAssociation(serviceRegistry,nodeRef,qName) ;
				for (AlfrescoContent associationAlfrescoORM : associationAlfrescoORMs)
				{
					if (StringUtils.isNotBlank(associationAlfrescoORM.getNodeUUID()))
					{
						// TODO: understand requirement and check that do we need to update pojo or just need to update association
						//UpdateHelper.getUpdateHelper().update(associationAlfrescoORM);
						NodeRef associationNodeRef = getNodeRef(associationAlfrescoORM);						
						List<AssociationRef> associationRefs = serviceRegistry.getNodeService().getTargetAssocs(nodeRef, qName) ;
						
						if(!associationRefs.isEmpty())
						{
							boolean doAdd = true ;
							for(AssociationRef associationRef : associationRefs)
							{
								if(associationRef.getTargetRef().equals(associationNodeRef))
								{
									doAdd = false ;
								}
							}
							if(doAdd)
							{
								serviceRegistry.getNodeService().createAssociation(nodeRef, associationNodeRef, qName);
							}
						}
						else
						{
							serviceRegistry.getNodeService().createAssociation(nodeRef, associationNodeRef, qName);
						}
						
					} else
					{
						CreateHelper.getCreateHelper().save(associationAlfrescoORM);
						NodeRef associationNodeRef = getNodeRef(associationAlfrescoORM);
						serviceRegistry.getNodeService().createAssociation(nodeRef, associationNodeRef, qName);
					}
				}

			}
		}
	}

	/**
	 * Method to remove all association for QName from the noderef
	 * @param serviceRegistry
	 * @param nodeRef
	 * @param qName
	 */
	private static void removeAllAssociation(ServiceRegistry serviceRegistry,NodeRef nodeRef,QName qName)
	{
		List<AssociationRef> associationRefs = serviceRegistry.getNodeService().getTargetAssocs(nodeRef, qName) ;
		for(AssociationRef associationRef : associationRefs)
		{
			serviceRegistry.getNodeService().removeAssociation(nodeRef, associationRef.getTargetRef(), qName);
		}		
	}

	/**
	 * @param alfrescoContent
	 * @param field
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	private static List<AlfrescoContent> getAsscoiationObject(final AlfrescoContent alfrescoContent, final Field field) throws IllegalAccessException,
			InvocationTargetException
	{
		List<AlfrescoContent> retVal = new ArrayList<AlfrescoContent>();
		AlfrescoAssociation alfrescoAssociation = field.getAnnotation(AlfrescoAssociation.class);
		Method method = ReflectionUtil.getMethod(alfrescoContent.getClass(), field.getName());
		if (alfrescoAssociation.many())
		{
			Collection<? extends AlfrescoContent> temp = (Collection<? extends AlfrescoContent>) method.invoke(alfrescoContent) ;
			if(null != temp)
			{
				retVal.addAll(temp);
			}
		} else
		{
			AlfrescoContent temp = (AlfrescoContent) method.invoke(alfrescoContent) ; 
			if(null != temp)
			{
				retVal.add(temp);	
			}			
		}
		return retVal;
	}

	/**
	 * @param alfrescoContent
	 * @return
	 */
	public static NodeRef getNodeRef(final AlfrescoContent alfrescoContent)
	{
		NodeRef nodeRef = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, alfrescoContent.getNodeUUID());
		return nodeRef;
	}

	/**
	 * @param alfrescoContent
	 * @return
	 * @throws ORMException
	 */
	public static Object getTargetServiceBean(final SpringBeanID springBeanID, final BeanFactory beanFactory) throws ORMException
	{
		String springBeanId = springBeanID.value();
		if (StringUtils.isEmpty(springBeanId))
		{
			throw new ORMException("Please provide springBeanId");
		}
		Object target = beanFactory.getBean(springBeanId);
		return target;
	}
}
