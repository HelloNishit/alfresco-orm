/**
 * 
 */
package com.alfresco.orm.repository;

import org.alfresco.service.cmr.repository.NodeRef;

import com.alfresco.orm.AlfrescoORM;
import com.alfresco.orm.exception.ORMException;

/**
 * @author Nishit C.
 * 
 */
public interface AlfrescoRespository<T extends AlfrescoORM>
{
	public void save(T t) throws ORMException;

	public void update(T t) throws ORMException;

	public void delete(T t) throws ORMException;
	
	public T fillObject(NodeRef nodeRef,Class<T> clasz,boolean isLazy) throws ORMException;
	
	public T fillObject(NodeRef nodeRef, boolean isLazy) throws ORMException ;
}
