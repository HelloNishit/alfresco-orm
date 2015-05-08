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
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.alfresco.repo.nodelocator.NodeLocatorService;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.commons.lang.NotImplementedException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.SessionFactory;
import com.alfresco.orm.annotation.AlfrescoRepositoryBean;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.service.SampleTypeServiceRepo;
import com.alfresco.orm.vo.SampleTypeExample1;

/**
 * @author Nishit C.
 * 
 */
public class SampleTypeServiceImplRepo
{
	private NodeService				nodeService;
	private NodeLocatorService		nodeLocatorService;
	private SessionFactory			sessionFactory;
	@AlfrescoRepositoryBean
	private SampleTypeServiceRepo	sampleTypeServiceRepo;

	public NodeRef getParentContentForSmapleType(final AlfrescoORM alfrescoORM)
	{
		return getCompanyHome();
	}
	
	public static void main(String args[])
	{	
//		//Class<T> typeOfT = (Class<T>)((ParameterizedType)clasz.getGenericSuperclass()).getActualTypeArguments()[0];
//		System.out.println(((sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl)SampleTypeServiceRepo.class.getGenericInterfaces())[0].getClass());
		
		for(Type type : SampleTypeServiceRepo.class.getGenericInterfaces())
		{
			if (type instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) type;
                System.out.println("Return type is " + pt.getRawType() + " with the following type arguments: ");
                
                for (Type t : pt.getActualTypeArguments()) {
                    System.out.println(t + " ");
                    
                    Class c = (Class) t ; 
                    System.out.println(c.getName());
                }
            }
            else {
                System.out.println("Return type is " + type);
            }
		}
		
		
	}

	/**
	 * @return the nodeService
	 */
	public NodeService getNodeService()
	{
		return nodeService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.service.SampleTypeService#createSampleType(java.lang
	 * .String)
	 */
	public AlfrescoORM createSampleType(final String json)
	{
		try
		{
			SampleTypeExample1 retObj = new ObjectMapper().readValue(json, SampleTypeExample1.class);
			sampleTypeServiceRepo.save(retObj);
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
	public AlfrescoORM updateSampleType(final String json)
	{
		try
		{
			SampleTypeExample1 retObj = new ObjectMapper().readValue(json, SampleTypeExample1.class);
			sampleTypeServiceRepo.update(retObj);
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
	public AlfrescoORM fetchData(NodeRef nodeRef)
	{
		SampleTypeExample1 sampleTypeExample1;
		try
		{
			sampleTypeExample1 = sampleTypeServiceRepo.fillObject(nodeRef, SampleTypeExample1.class, true);
			return sampleTypeExample1;
		} catch (ORMException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

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

	/**
	 * @return the sampleTypeServiceRepo
	 */
	public SampleTypeServiceRepo getSampleTypeServiceRepo()
	{
		return sampleTypeServiceRepo;
	}

	/**
	 * @param sampleTypeServiceRepo
	 *            the sampleTypeServiceRepo to set
	 */
	public void setSampleTypeServiceRepo(SampleTypeServiceRepo sampleTypeServiceRepo)
	{
		this.sampleTypeServiceRepo = sampleTypeServiceRepo;
	}

}
