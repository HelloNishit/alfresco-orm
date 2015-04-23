/*******************************************************************************
 * Copyright 2015 Nishit C
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
package com.alfresco.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 * @author nishit.charania
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AlfrescoType {
	SpringBeanID springBeanID();

	/**
	 * Annotation property for custom method name<br>
	 * Method signature should be public NodeRef methodName(AlfrescoORM
	 * alfrescoORM);
	 * 
	 * @return
	 */
	String createNodeMethodName() default "";

	/**
	 * Annotation property for getting parent content<br>
	 * Method should return parent noderef <br>
	 * Method signature should be public NodeRef methodName(AlfrescoORM
	 * alfrescoORM);
	 * 
	 * @return
	 */
	String getParentFolderMethodName() default "";

	/**
	 * Annotation property for alfresco namespace url
	 * 
	 * @return
	 */
	String namespaceURI() default "";

	/**
	 * Annotation property for alfresco local name
	 * 
	 * @return
	 */
	String localName() default "";

}
