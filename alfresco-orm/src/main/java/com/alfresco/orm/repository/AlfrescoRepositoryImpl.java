/**
 * 
 */
package com.alfresco.orm.repository;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.Session;
import com.alfresco.orm.SessionFactory;
import com.alfresco.orm.exception.ORMException;

/**
 * @author Nishit C.
 *
 */
public class AlfrescoRepositoryImpl<T extends AlfrescoORM> implements AlfrescoRespository<T>
{

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
		session.save(t);
		session.close();

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
		session.update(t);
		session.close();
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
		session.delete(t);
		session.close();
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

}
