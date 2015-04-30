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
package com.alfresco.orm.service.impl;

import java.io.IOException;

import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.Session;
import com.alfresco.orm.SessionFactory;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.service.SampleTypeService;
import com.alfresco.orm.vo.SampleTypeExample1;

/**
 * @author Nishit C.
 *
 */
public class SampleTypeServiceImpl implements SampleTypeService
{
	private NodeService			nodeService;
	private NodeLocatorService	nodeLocatorService;
	private SessionFactory		sessionFactory;

	public NodeRef getParentContentForSmapleType(final AlfrescoORM alfrescoORM)
	{
		return getCompanyHome();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.service.SampleTypeService#createSampleType(java.lang
	 * .String)
	 */
	@Override
	public AlfrescoORM createSampleType(final String json)
	{
		Session session = sessionFactory.getSession();
		try
		{
			SampleTypeExample1 retObj = new ObjectMapper().readValue(json, SampleTypeExample1.class);
			session.save(retObj);
			return retObj;
		} catch (JsonParseException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			e.printStackTrace();
		} catch (ORMException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			session.close();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.service.SampleTypeService#updateSampleType(java.lang
	 * .String)
	 */
	@Override
	public AlfrescoORM updateSampleType(final String json)
	{
		Session session = sessionFactory.getSession();
		try
		{
			SampleTypeExample1 retObj = new ObjectMapper().readValue(json, SampleTypeExample1.class);
			session.update(retObj);
			return retObj;
		} catch (JsonParseException e)
		{
			e.printStackTrace();
		} catch (JsonMappingException e)
		{
			e.printStackTrace();
		} catch (ORMException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			session.close();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.service.SampleTypeService#deleteSampleType(java.lang
	 * .String)
	 */
	@Override
	public void deleteSampleType(final String json)
	{
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.service.SampleTypeService#searchSampleType(java.lang
	 * .String)
	 */
	@Override
	public void searchSampleType(final String json)
	{
		throw new NotImplementedException();
	}

	/**
	 * @return the nodeService
	 */
	public NodeService getNodeService()
	{
		return nodeService;
	}

	/**
	 * @param nodeService
	 *            the nodeService to set
	 */
	public void setNodeService(final NodeService nodeService)
	{
		this.nodeService = nodeService;
	}

	/**
	 * @return the nodeLocatorService
	 */
	public NodeLocatorService getNodeLocatorService()
	{
		return nodeLocatorService;
	}

	/**
	 * @param nodeLocatorService
	 *            the nodeLocatorService to set
	 */
	public void setNodeLocatorService(final NodeLocatorService nodeLocatorService)
	{
		this.nodeLocatorService = nodeLocatorService;
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
	public void setSessionFactory(final SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Returns the NodeRef of "Company Home"
	 * 
	 * @return
	 */
	public NodeRef getCompanyHome()

	{
		return nodeLocatorService.getNode("companyhome", null, null);
	}

}
