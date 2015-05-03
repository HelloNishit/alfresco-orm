/**
 * 
 */
package com.alfresco.orm.repository;

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
}
