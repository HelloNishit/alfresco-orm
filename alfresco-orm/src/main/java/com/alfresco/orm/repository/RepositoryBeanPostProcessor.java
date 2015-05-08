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
package com.alfresco.orm.repository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.alfresco.orm.annotation.AlfrescoRepositoryBean;
import com.alfresco.orm.reflection.ReflectionUtil;

public class RepositoryBeanPostProcessor implements BeanPostProcessor
{
	final Logger	logger	= LoggerFactory.getLogger(RepositoryBeanPostProcessor.class);

	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
	{
		Class clazz = bean.getClass();
		List<Method> repositoryMethod = ReflectionUtil.setterMethodForAnnotation(clazz, AlfrescoRepositoryBean.class);
		for (Method method : repositoryMethod)
		{
			try
			{
				method.invoke(bean, AlfrescoRespositoryProxyFactoryBean.getAlfrescoRespositoryProxyFactoryBean().getObject(method.getParameterTypes()[0]));
			} catch (IllegalArgumentException e)
			{
				throw new BeansException(e.getMessage(), e)
				{
				};
			} catch (IllegalAccessException e)
			{
				throw new BeansException(e.getMessage(), e)
				{
				};
			} catch (InvocationTargetException e)
			{
				throw new BeansException(e.getMessage(), e)
				{
				};
			} catch (InstantiationException e)
			{
				throw new BeansException(e.getMessage(), e)
				{
				};
			}
		}

		return bean;
	}

	public Object postProcessBeforeInitialization(Object object, String beanName) throws BeansException
	{
		return object;
	}

}
