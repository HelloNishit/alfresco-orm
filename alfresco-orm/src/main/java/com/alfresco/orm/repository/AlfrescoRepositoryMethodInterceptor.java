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

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Nishit C.
 * 
 */
public class AlfrescoRepositoryMethodInterceptor implements MethodInterceptor
{
	public Object invoke(MethodInvocation invocation) throws Throwable
	{
		System.out.println("--------------->" + invocation.getThis().getClass().getTypeParameters()[0].getName()) ;
		System.out.println("--------------->" + invocation.getThis().getClass().getTypeParameters()[0].getClass()) ;
		System.out.println("--------------->" + invocation.getThis().getClass().getTypeParameters()[0].getGenericDeclaration()) ;
		System.out.println("--------------->" +invocation.getMethod()) ;
		if(invocation.getMethod().getName().equals("myMethod"))
		{
			System.out.println("this is working and myMethod call");
			return null ;
		}

		Object retObject = invocation.proceed();

		return retObject;
	}

}
