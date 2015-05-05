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

import java.util.ArrayList;
import java.util.List;

import org.alfresco.service.ServiceRegistry;
import org.aopalliance.aop.Advice;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/**
 * 
 * @author Nishit C.
 * 
 */
public class SessionFactory implements BeanFactoryAware
{
	private ServiceRegistry	serviceRegistry;
	private BeanFactory		beanFactory;

	public void init()
	{
		initHelperClass();
	}

	public synchronized Session getSession()
	{
		// TODO: Find batter way to use Session object, check ThreadLocal if we
		// can use
		Session session = new Session(beanFactory, serviceRegistry);
		return session;
	}

	public void initHelperClass()
	{
		CreateHelper.init(beanFactory, serviceRegistry);
		UpdateHelper.init(beanFactory, serviceRegistry);
		DeleteHelper.init(beanFactory, serviceRegistry);
		ObjectFillHelper.init(beanFactory, serviceRegistry);
		List<Class<? extends Advice>> c = new ArrayList<Class<? extends Advice>>();
		c.add(LazyMethodInterceptor.class);
		LazyProxyFactoryBean.init(c);
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
	public void setServiceRegistry(final ServiceRegistry serviceRegistry)
	{
		this.serviceRegistry = serviceRegistry;
	}

	public void setBeanFactory(final BeanFactory beanFactory) throws BeansException
	{
		this.beanFactory = beanFactory;

	}

}
