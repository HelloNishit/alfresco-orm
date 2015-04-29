package com.alfresco.orm;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.namespace.QName;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;

public class UpdateHelper
{
	private BeanFactory		beanFactory;
	private ServiceRegistry	serviceRegistry;
	private List<String>	restrictedPropertiesForUpdate	= Arrays.asList("node-uuid","creator", "created", "modifier", "modified");

	public UpdateHelper(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void update(final AlfrescoORM alfrescoORM) throws ORMException
	{
		try
		{
			Map<QName, Serializable> properties = ORMUtil.getAlfrescoProperty(alfrescoORM);
			ORMUtil.saveProperties(alfrescoORM, properties, serviceRegistry.getNodeService(),restrictedPropertiesForUpdate);
			ORMUtil.executeCustomeMethodForProperty(alfrescoORM, beanFactory);
		} catch (IllegalArgumentException e)
		{
			throw new ORMException(e);
		} catch (SecurityException e)
		{
			throw new ORMException(e);
		} catch (IllegalAccessException e)
		{
			throw new ORMException(e);
		} catch (NoSuchMethodException e)
		{
			throw new ORMException(e);
		} catch (InvocationTargetException e)
		{
			throw new ORMException(e);
		}
	}

}
