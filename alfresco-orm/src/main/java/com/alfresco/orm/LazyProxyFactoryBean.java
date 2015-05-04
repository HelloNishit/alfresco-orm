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

import java.util.ArrayList;
import java.util.List;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author Nishit C.
 *
 */
public class LazyProxyFactoryBean<T extends AlfrescoORM> implements FactoryBean<T>
{
	private Class<T>				objectType;
	private List<MethodInterceptor>	methodInterceptors	= new ArrayList<MethodInterceptor>();

	public LazyProxyFactoryBean(final Class<T> objectType)
	{
		this.objectType = objectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObject()
	 */
	public T getObject() throws Exception
	{
		T obj = objectType.newInstance() ;
		ProxyFactory proxyFactory = new ProxyFactory();
		proxyFactory.setTarget(obj);
		for (MethodInterceptor methodInterceptor : methodInterceptors) {
            proxyFactory.addAdvice(methodInterceptor);
        }
		return (T) proxyFactory.getProxy() ;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#getObjectType()
	 */
	public Class<?> getObjectType()
	{
		return objectType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.FactoryBean#isSingleton()
	 */
	public boolean isSingleton()
	{
		return false;
	}

	public void setMethodInterceptors(final List<MethodInterceptor> methodInterceptors)
	{
		this.methodInterceptors = methodInterceptors;
	}

}
