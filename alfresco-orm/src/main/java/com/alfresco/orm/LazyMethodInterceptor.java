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

import org.alfresco.service.cmr.repository.NodeRef;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.alfresco.orm.mapping.AlfrescoContent;

/**
 * @author Nishit C.
 * 
 */
public class LazyMethodInterceptor implements MethodInterceptor
{
	private boolean	isLoaded	= false;

	public Object invoke(MethodInvocation invocation) throws Throwable
	{	
		if(isLoaded)
		{
			AlfrescoContent alfrescoContent = (AlfrescoContent) invocation.getThis();
			NodeRef nodeRef = ORMUtil.getNodeRef(alfrescoContent);
			ObjectFillHelper.getObjectFillHelper().getFilledObject(nodeRef, alfrescoContent, true);
		}
		Object retObject = invocation.proceed();
		isLoaded = true;
		return retObject;
	}

}
