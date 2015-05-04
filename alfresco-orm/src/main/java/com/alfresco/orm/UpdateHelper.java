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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.namespace.QName;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;

public class UpdateHelper
{
	private BeanFactory			beanFactory;
	private ServiceRegistry		serviceRegistry;
	private List<String>		restrictedPropertiesForUpdate	= Arrays.asList("node-uuid", "creator", "created", "modifier", "modified");

	private static UpdateHelper	updateHelper;

	public static UpdateHelper init(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		if (null == updateHelper)
		{
			updateHelper = new UpdateHelper(beanFactory, serviceRegistry);
		}
		return updateHelper;
	}

	public static UpdateHelper getUpdateHelper()
	{
		return updateHelper;
	}

	private UpdateHelper(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void update(final AlfrescoORM alfrescoORM) throws ORMException
	{
		try
		{
			Map<QName, Serializable> properties = ORMUtil.getAlfrescoProperty(alfrescoORM);
			ORMUtil.saveProperties(alfrescoORM, properties, serviceRegistry.getNodeService(), restrictedPropertiesForUpdate);
			ORMUtil.executeCustomeMethodForProperty(alfrescoORM, beanFactory);
			ORMUtil.executeAssociation(alfrescoORM, beanFactory, serviceRegistry);
		} catch (IllegalArgumentException e)
		{
			throw new ORMException(e.getMessage(),e);
		} catch (SecurityException e)
		{
			throw new ORMException(e.getMessage(),e);
		} catch (IllegalAccessException e)
		{
			throw new ORMException(e.getMessage(),e);
		} catch (NoSuchMethodException e)
		{
			throw new ORMException(e.getMessage(),e);
		} catch (InvocationTargetException e)
		{
			throw new ORMException(e.getMessage(),e);
		}
	}

}
