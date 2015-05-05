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
/**
 * 
 */
package com.alfresco.orm;

import java.util.List;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.ProxyFactory;

/**
 * @author Nishit C.
 * 
 */
public class LazyProxyFactoryBean
{
	private List<Class<? extends Advice>>	adviceList;

	private static LazyProxyFactoryBean		LAZY_PROXY_FACTORY_BEAN;

	private LazyProxyFactoryBean()
	{
	}

	public static void init(List<Class<? extends Advice>> methodInterceptors)
	{
		LAZY_PROXY_FACTORY_BEAN = new LazyProxyFactoryBean();
		LAZY_PROXY_FACTORY_BEAN.adviceList = methodInterceptors;
	}

	public static LazyProxyFactoryBean getLazyProxyFactoryBean()
	{
		return LAZY_PROXY_FACTORY_BEAN;
	}

	public <T extends AlfrescoORM> T getObject(String nodeUUID,Class<T> clasz) throws InstantiationException, IllegalAccessException
	{
		T obj = clasz.newInstance();
		obj.setNodeUUID(nodeUUID);
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTarget(obj);
		for (Class<? extends Advice> methodInterceptorClass : adviceList)
		{
			proxyFactory.addAdvice(methodInterceptorClass.newInstance());
		}
		return (T) proxyFactory.getProxy();
	}

}
