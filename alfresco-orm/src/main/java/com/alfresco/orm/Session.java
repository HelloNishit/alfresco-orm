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

import org.alfresco.service.ServiceRegistry;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;

/**
 * 
 * @author Nishit C.
 * 
 */
public class Session
{
	private BeanFactory		beanFactory;
	private ServiceRegistry	serviceRegistry;
	private CreateHelper	createHelper;
	private UpdateHelper	updateHelper;
	private DeleteHelper	deleteHelper;

	public Session(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.createHelper = new CreateHelper(beanFactory, serviceRegistry);
		this.updateHelper = new UpdateHelper(beanFactory, serviceRegistry) ;
		this.deleteHelper = new DeleteHelper(beanFactory, serviceRegistry) ;
		this.serviceRegistry = serviceRegistry;
		this.beanFactory = beanFactory;
		
	}

	public void save(final AlfrescoORM alfrescoORM) throws ORMException
	{
		createHelper.save(alfrescoORM);
	}

	public void update(final AlfrescoORM alfrescoORM) throws ORMException
	{
		updateHelper.update(alfrescoORM);
	}

	public void delete(final AlfrescoORM alfrescoORM) throws ORMException
	{
		deleteHelper.delete(alfrescoORM);
	}

	/**
	 * @return the serviceRegistry
	 */
	public void setServiceRegistry(final ServiceRegistry serviceRegistry)
	{
		this.serviceRegistry = serviceRegistry;
	}

	private ServiceRegistry getServiceRegistry()
	{
		return serviceRegistry;
	}

	public void setBeanFactory(final BeanFactory beanFactory)
	{
		this.beanFactory = beanFactory;
	}

	public void close()
	{

	}

}
