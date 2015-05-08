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
package com.alfresco.orm.repository;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.aop.framework.ProxyFactory;

import com.alfresco.orm.SessionFactory;

/**
 * @author Nishit C.
 * 
 */
public class AlfrescoRespositoryProxyFactoryBean
{
	private static AlfrescoRespositoryProxyFactoryBean	Alfresco_Respository_Proxy_Factory_Bean;
	private SessionFactory								sessionFactory;

	private AlfrescoRespositoryProxyFactoryBean()
	{
	}

	public static void init(SessionFactory sessionFactory)
	{
		Alfresco_Respository_Proxy_Factory_Bean = new AlfrescoRespositoryProxyFactoryBean();
		Alfresco_Respository_Proxy_Factory_Bean.sessionFactory = sessionFactory;
	}

	public static AlfrescoRespositoryProxyFactoryBean getAlfrescoRespositoryProxyFactoryBean()
	{
		return Alfresco_Respository_Proxy_Factory_Bean;
	}

	public <T extends AlfrescoRespository> T getObject(Class clasz) throws InstantiationException, IllegalAccessException
	{
		AlfrescoRepositoryImpl alfrescoRespository = new AlfrescoRepositoryImpl();
		Class declaredType = getTypeClass(clasz);
		if (null == declaredType)
		{
			throw new IllegalAccessException("Invalid configuration exception for class : " + clasz.getName());
		}
		alfrescoRespository.setDeclaredType(declaredType);
		alfrescoRespository.setSessionFactory(sessionFactory);
		ProxyFactory proxyFactory = new ProxyFactory(new Class[] { clasz });
		proxyFactory.setTarget(alfrescoRespository);
		proxyFactory.addAdvice(new AlfrescoRepositoryMethodInterceptor());
		return (T) proxyFactory.getProxy();
	}

	public Class getTypeClass(Class clasz)
	{
		Class retClass = null;
		for (Type type : clasz.getGenericInterfaces())
		{
			if (type instanceof ParameterizedType)
			{
				ParameterizedType pt = (ParameterizedType) type;
				// System.out.print("Return type is " + pt.getRawType() +
				// " with the following type arguments: ");
				for (Type t : pt.getActualTypeArguments())
				{
					// System.out.println(t + " ");
					retClass = (Class) t;
				}
			} else
			{
				// System.out.println("Return type is " + type);
			}
		}
		return retClass;
	}

	/**
	 * @return the sessionFactory
	 */
	public SessionFactory getSessionFactory()
	{
		return sessionFactory;
	}

	/**
	 * @param sessionFactory
	 *            the sessionFactory to set
	 */
	public void setSessionFactory(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

}
