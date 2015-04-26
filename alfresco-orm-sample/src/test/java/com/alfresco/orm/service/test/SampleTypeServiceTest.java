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

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
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
import com.alfresco.orm.vo.SampleTypeExample1;
import com.tradeshift.test.remote.Remote;
import com.tradeshift.test.remote.RemoteTestRunner;

/**
 * 
 * @author Nishit C.
 *
 */
@RunWith(RemoteTestRunner.class)
@Remote(runnerClass=SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:alfresco/application-context.xml")
public class SampleTypeServiceTest {
    
    private static final String ADMIN_USER_NAME = "admin";

    static Logger log = Logger.getLogger(SampleTypeServiceTest.class);

    @Autowired
    protected SampleTypeService sampleTypeService;
    
    @Autowired
    @Qualifier("NodeService")
    protected NodeService nodeService;
    
    @Test
    public void testWiring() {
        assertNotNull(sampleTypeService);
    }
    
    @Test
    public void tesstCreateSampleType() throws JsonGenerationException, JsonMappingException, IOException
    {
    	AuthenticationUtil.setFullyAuthenticatedUser(ADMIN_USER_NAME);
    	SampleTypeExample1 sampleTypeExample1 = new SampleTypeExample1() ;
    	sampleTypeExample1.setDescription("Description");
    	sampleTypeExample1.setLanguage("English");
    	sampleTypeExample1.setName("Name");
    	sampleTypeExample1.setSampleFloat(0.11f);
    	sampleTypeExample1.setSampleInt(5);
    	sampleTypeExample1.setSampleString("String");
    	SampleAspect sanpleAspect = new SampleAspect() ;    	
    	sampleTypeExample1.setSampleAspect(sanpleAspect);
    	sanpleAspect.setSampleFloatAspect(0.55f);
    	sanpleAspect.setSampleIntAspect(1);
    	sanpleAspect.setSampleStringAspect("Test Aspect");
    	SampleTypeExample1 retObj = (SampleTypeExample1) sampleTypeService.createSampleType(new ObjectMapper().writeValueAsString(sampleTypeExample1));
    	NodeRef nodeRef = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, retObj.getNodeUUID()) ;
    	assertNotNull(nodeRef);
    }

}
