/**
 * 
 */
package com.alfresco.orm.repository;

import java.util.Arrays;
import java.util.List;

import org.alfresco.service.cmr.repository.NodeRef;

import com.alfresco.orm.Session;
import com.alfresco.orm.SessionFactory;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.mapping.AlfrescoContent;

/**
 * @author Nishit C.
 * 
 */
public class AlfrescoRepositoryImpl<T extends AlfrescoContent> implements AlfrescoRespository<T>
{
	private Class<T>		declaredType;

	private SessionFactory	sessionFactory;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.repository.AlfrescoRespository#save(com.alfresco.orm
	 * .AlfrescoORM)
	 */
	public void save(final T t) throws ORMException
	{
		Session session = sessionFactory.getSession();
		try
		{
			session.save(t);
		} finally
		{
			session.close();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.repository.AlfrescoRespository#update(com.alfresco.orm
	 * .AlfrescoORM)
	 */
	public void update(final T t) throws ORMException
	{
		Session session = sessionFactory.getSession();
		try
		{
			session.update(t);
		} finally
		{
			session.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.alfresco.orm.repository.AlfrescoRespository#delete(com.alfresco.orm
	 * .AlfrescoORM)
	 */
	public void delete(final T t) throws ORMException
	{
		Session session = sessionFactory.getSession();
		try
		{
			session.delete(t);
		} finally
		{
			session.close();
		}
	}

	public T fillObject(NodeRef nodeRef, Class<T> clasz, boolean isLazy) throws ORMException
	{
		Session session = sessionFactory.getSession();
		try
		{
			List<T> retData = session.fillObject(Arrays.asList(new NodeRef[] { nodeRef }), clasz, isLazy);
			return retData.get(0);
		} finally
		{
			session.close();
		}
	}
	
	public T fillObject(NodeRef nodeRef, boolean isLazy) throws ORMException
	{
		//TODO: need to test declaredType is working or not
		Session session = sessionFactory.getSession();
		try
		{
			List<T> retData = session.fillObject(Arrays.asList(new NodeRef[] { nodeRef }), declaredType, isLazy);
			return retData.get(0);
		} finally
		{
			session.close();
		}
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
	 * @return the declaredType
	 */
	public Class<T> getDeclaredType()
	{
		return declaredType;
	}

	/**
	 * @param declaredType
	 *            the declaredType to set
	 */
	public void setDeclaredType(Class<T> declaredType)
	{
		this.declaredType = declaredType;
	}

}
