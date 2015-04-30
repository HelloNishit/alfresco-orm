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
package com.alfresco.orm.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.alfresco.orm.AlfrescoORM;

/**
 * 
 * @author Nishit C.
 *
 */
public abstract class ReflectionUtil
{

	public static Method getMethod(final Class<? extends AlfrescoORM> type, final String fieldName)
	{
		Method methods[] = type.getDeclaredMethods();
		Method retMethod = null;
		for (Method method : methods)
		{
			if (method.getName().equalsIgnoreCase("get" + fieldName) || method.getName().equalsIgnoreCase("is" + fieldName))
			{
				retMethod = method;
				break;
			}
		}
		if (retMethod == null)
		{
			if (type.getSuperclass() != null)
			{
				if (AlfrescoORM.class.isAssignableFrom(type.getSuperclass()))
				{
					retMethod = getMethod((Class<? extends AlfrescoORM>) type.getSuperclass(), fieldName);
				}
			}
		}
		return retMethod;
	}

	public static void getFields(final Class<? extends AlfrescoORM> type, final List<Field> fields)
	{
		fields.addAll(Arrays.asList(type.getDeclaredFields()));
		if (type.getSuperclass() != null)
		{
			if (AlfrescoORM.class.isAssignableFrom(type.getSuperclass()))
			{
				getFields((Class<? extends AlfrescoORM>) type.getSuperclass(), fields);
			}
		}
	}

}
