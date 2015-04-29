package com.alfresco.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.namespace.QName;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.annotation.AlfrescoAspect;
import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.annotation.CustomProperty;
import com.alfresco.orm.annotation.SpringBeanID;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.reflection.ReflectionUtil;

public abstract class ORMUtil
{
	public static Map<QName, Serializable> getAlfrescoProperty(final AlfrescoORM alfrescoORM) throws IllegalArgumentException,
			IllegalAccessException, SecurityException, NoSuchMethodException, InvocationTargetException
	{
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);
		Map<QName, Serializable> retVal = new HashMap<QName, Serializable>(fields.size());
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(AlfrescoQName.class))
			{
				AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
				QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
				Method getterMethod = ReflectionUtil.getMethod(alfrescoORM.getClass(), field.getName());
				retVal.put(qName, (Serializable) getterMethod.invoke(alfrescoORM));
			} else if (field.isAnnotationPresent(AlfrescoAspect.class))
			{
				Method getterMethod = ReflectionUtil.getMethod(alfrescoORM.getClass(), field.getName());
				AlfrescoORM aspect = (AlfrescoORM) getterMethod.invoke(alfrescoORM);
				retVal.putAll(getAlfrescoProperty(aspect));
			}
		}
		return retVal;
	}

	/**
	 * 
	 * @param alfrescoORM
	 * @param properties
	 * @param nodeService
	 * @param restrictedPropertiesForUpdate
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ORMException
	 */
	public static void saveProperties(final AlfrescoORM alfrescoORM, final Map<QName, Serializable> properties, NodeService nodeService,
			List<String> restrictedPropertiesForUpdate) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, ORMException
	{
		NodeRef nodeRef = getNodeRef(alfrescoORM);
		Map<QName, Serializable> propertiesFinal = new HashMap<QName, Serializable>(properties.size()) ;
		for(Entry<QName, Serializable> prop : properties.entrySet())
		{
			if(!restrictedPropertiesForUpdate.contains(prop.getKey().getLocalName()))
			{
				propertiesFinal.put(prop.getKey(), prop.getValue()) ;
			}
		}
		
		nodeService.setProperties(nodeRef, properties);
	}

	/**
	 * 
	 * @param alfrescoORM
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws ORMException
	 */
	public static void executeCustomeMethodForProperty(final AlfrescoORM alfrescoORM, BeanFactory beanFactory) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ORMException
	{
		NodeRef nodeRef = getNodeRef(alfrescoORM);
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);
		for (Field field : fields)
		{
			if (field.isAnnotationPresent(CustomProperty.class))
			{
				CustomProperty customProperty = field.getAnnotation(CustomProperty.class);
				if (StringUtils.isNotEmpty(customProperty.setPropertMethodName()))
				{
					Object target = getTargetServiceBean(customProperty.springBeanID(), beanFactory);
					Method customeMethod = target.getClass().getMethod(customProperty.setPropertMethodName(), NodeRef.class, AlfrescoORM.class);
					customeMethod.invoke(target, nodeRef, alfrescoORM);
				} else
				{
					throw new ORMException("Please set cutome method name to set property");
				}
			}
		}
	}

	/**
	 * @param alfrescoORM
	 * @return
	 */
	public static NodeRef getNodeRef(final AlfrescoORM alfrescoORM)
	{
		NodeRef nodeRef = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, alfrescoORM.getNodeUUID());
		return nodeRef;
	}

	/**
	 * @param alfrescoContent
	 * @return
	 * @throws ORMException
	 */
	public static Object getTargetServiceBean(final SpringBeanID springBeanID, BeanFactory beanFactory) throws ORMException
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
