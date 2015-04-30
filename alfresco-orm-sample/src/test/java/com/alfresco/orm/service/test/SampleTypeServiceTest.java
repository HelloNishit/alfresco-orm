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
package com.alfresco.orm.service.test;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeService;
import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alfresco.orm.service.SampleTypeService;
import com.alfresco.orm.vo.SampleAspect;
import com.alfresco.orm.vo.SampleType1;
import com.alfresco.orm.vo.SampleType2;
import com.alfresco.orm.vo.SampleTypeExample1;
import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

/**
 * 
 * @author Nishit C.
 *
 */
@RunWith(RemoteTestRunner.class)
@Remote(runnerClass = SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class SampleTypeServiceTest
{

	private static final String	ADMIN_USER_NAME	= "admin";

	static Logger				log				= Logger.getLogger(SampleTypeServiceTest.class);

	@Autowired
	protected SampleTypeService	sampleTypeService;

	@Autowired
	@Qualifier("NodeService")
	protected NodeService		nodeService;

	@Test
	public void testWiring()
	{
		assertNotNull(sampleTypeService);
	}

	@Test
	public void tesstCreateSampleType() throws JsonGenerationException, JsonMappingException, IOException
	{
		AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
		SampleTypeExample1 sampleTypeExample1 = new SampleTypeExample1();
		sampleTypeExample1.setDescription("Description");
		sampleTypeExample1.setLanguage("English");
		sampleTypeExample1.setName("Name");
		sampleTypeExample1.setSampleFloat(0.11f);
		sampleTypeExample1.setSampleInt(5);
		sampleTypeExample1.setSampleString("String");

		// setting aspect
		SampleAspect sanpleAspect = new SampleAspect();
		sampleTypeExample1.setSampleAspect(sanpleAspect);
		sanpleAspect.setSampleFloatAspect(0.55f);
		sanpleAspect.setSampleIntAspect(1);
		sanpleAspect.setSampleStringAspect("Test Aspect");

		// setting association
		List<SampleType1> sampleType1s = new ArrayList<SampleType1>();
		SampleType1 sampleType1 = new SampleType1();
		sampleType1.setName("sampleType1" + System.currentTimeMillis());
		sampleType1.setTitle("sampletype1->title");
		sampleType1.setSampleString11("sampleType1->SampleString11");
		sampleType1.setSampleString12("sampleType1->sampleString12");
		sampleType1s.add(sampleType1);
		sampleType1 = new SampleType1();
		sampleType1.setName("sampleType11" + System.currentTimeMillis());
		sampleType1.setTitle("sampletype1->title");
		sampleType1.setSampleString11("sampleType1->SampleString11");
		sampleType1.setSampleString12("sampleType1->sampleString12");
		sampleType1s.add(sampleType1);
		sampleTypeExample1.setSampleType1(sampleType1s);

		SampleType2 sampleType2 = new SampleType2();
		sampleType2.setName("sampleType2" + System.currentTimeMillis());
		sampleType2.setTitle("sampleType2->title");
		sampleType2.setSampleString21("sampleType2->SampleString21");
		sampleType2.setSampleString22("sampleType2->sampleString22");
		sampleTypeExample1.setSampleType2(sampleType2);

		System.out.println("----------------------------------------------------");
		System.out.println(new ObjectMapper().writeValueAsString(sampleTypeExample1));
		System.out.println("----------------------------------------------------");
		SampleTypeExample1 retObj = (SampleTypeExample1) sampleTypeService
				.createSampleType(new ObjectMapper().writeValueAsString(sampleTypeExample1));
		assertNotNull(retObj.getNodeUUID());
		Assert.assertEquals(sampleTypeExample1.getSampleString(), retObj.getSampleString());
	}

}
