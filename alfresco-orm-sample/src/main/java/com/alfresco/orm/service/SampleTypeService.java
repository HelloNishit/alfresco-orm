/**
 * 
 */
package com.alfresco.orm.service;

import com.alfresco.orm.AlfrescoORM;

/**
 * @author Nishit C.
 *
 */
public interface SampleTypeService
{
	public AlfrescoORM createSampleType(String json);

	public AlfrescoORM updateSampleType(String json);

	public void deleteSampleType(String json);

	public void searchSampleType(String json);

}
