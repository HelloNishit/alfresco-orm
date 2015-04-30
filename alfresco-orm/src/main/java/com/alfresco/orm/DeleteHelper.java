package com.alfresco.orm;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.exception.ORMException;

public class DeleteHelper
{
	private BeanFactory			beanFactory;
	private ServiceRegistry		serviceRegistry;

	private static DeleteHelper	deleteHelper;

	public static DeleteHelper init(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		if (null == deleteHelper)
		{
			deleteHelper = new DeleteHelper(beanFactory, serviceRegistry);
		}
		return deleteHelper;
	}

	public static DeleteHelper getDeleteHelper()
	{
		return deleteHelper;
	}

	private DeleteHelper(BeanFactory beanFactory, ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void delete(final AlfrescoORM alfrescoORM) throws ORMException
	{
		NodeRef nodeRef = ORMUtil.getNodeRef(alfrescoORM);
		serviceRegistry.getNodeService().deleteNode(nodeRef);
	}

}
