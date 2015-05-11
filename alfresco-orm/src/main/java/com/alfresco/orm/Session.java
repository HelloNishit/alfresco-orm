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
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.mapping.AlfrescoContent;

/**
 * 
 * @author Nishit C.
 * 
 */
public class Session
{
	private BeanFactory			beanFactory;
	private ServiceRegistry		serviceRegistry;
	private CreateHelper		createHelper;
	private UpdateHelper		updateHelper;
	private DeleteHelper		deleteHelper;
	private ObjectFillHelper	objectFillHelper;

	public Session(final BeanFactory beanFactory, final ServiceRegistry serviceRegistry)
	{
		this.createHelper = CreateHelper.getCreateHelper();
		this.updateHelper = UpdateHelper.getUpdateHelper();
		this.deleteHelper = DeleteHelper.getDeleteHelper();
		objectFillHelper = ObjectFillHelper.getObjectFillHelper();
		this.serviceRegistry = serviceRegistry;
		this.beanFactory = beanFactory;
	}

	public void save(final AlfrescoContent alfrescoORM) throws ORMException
	{
		createHelper.save(alfrescoORM);
	}

	public void update(final AlfrescoContent alfrescoORM) throws ORMException
	{
		updateHelper.update(alfrescoORM);
	}

	public void delete(final AlfrescoContent alfrescoORM) throws ORMException
	{
		deleteHelper.delete(alfrescoORM);
	}

	public <T extends AlfrescoContent> List<T> fillObject(final List<NodeRef> nodeRefs, Class<T> classType, boolean isLazy) throws ORMException
	{
		List<T> retVAl = new ArrayList<T>();
		T orm;
		try
		{
			for (NodeRef nodeRef : nodeRefs)
			{
				orm = classType.newInstance();
				objectFillHelper.getFilledObject(nodeRef, orm, isLazy);
				retVAl.add(orm);
			}
		} catch (InstantiationException e)
		{
			throw new ORMException(e.getMessage(), e);
		} catch (IllegalAccessException e)
		{
			throw new ORMException(e.getMessage(), e);
		}
		return retVAl;
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
