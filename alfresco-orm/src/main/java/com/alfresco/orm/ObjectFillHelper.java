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
/**
 * 
 */
package com.alfresco.orm;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.AssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.namespace.QName;
import org.springframework.beans.factory.BeanFactory;

import com.alfresco.orm.annotation.AlfrescoAspect;
import com.alfresco.orm.annotation.AlfrescoAssociation;
import com.alfresco.orm.annotation.AlfrescoQName;
import com.alfresco.orm.exception.ORMException;
import com.alfresco.orm.reflection.ReflectionUtil;

/**
 * @author Nishit C.
 * 
 */
public class ObjectFillHelper
{
	private BeanFactory				beanFactory;
	private ServiceRegistry			serviceRegistry;
	private static ObjectFillHelper	objectFillHelper;
	private static final Double		ZERO_DOUBLE	= 0.0;
	private static final Float		ZERO_FLOAT	= 0F;

	public static ObjectFillHelper init(final BeanFactory beanFactory, final ServiceRegistry serviceRegistry)
	{
		if (null == objectFillHelper)
		{
			objectFillHelper = new ObjectFillHelper(beanFactory, serviceRegistry);
		}
		return objectFillHelper;
	}

	public static ObjectFillHelper getObjectFillHelper()
	{
		return objectFillHelper;
	}

	private ObjectFillHelper(final BeanFactory beanFactory, final ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void getFilledObject(final NodeRef nodeRef, final AlfrescoORM alfrescoORM,boolean isLazy) throws ORMException
	{

		NodeService nodeService = serviceRegistry.getNodeService();
		if (!nodeService.exists(nodeRef))
		{
			throw new ORMException("NodeRef does not exist : " + nodeRef);
		}
		Map<QName, Serializable> properties = nodeService.getProperties(nodeRef);
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);

		for (Field field : fields)
		{
			if (field.isAnnotationPresent(AlfrescoQName.class) && !field.isAnnotationPresent(AlfrescoAssociation.class))
			{
				AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
				QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
				Method setterMethod = ReflectionUtil.setMethod(alfrescoORM.getClass(), field.getName());
				if (setterMethod.getParameterTypes().length == 0 || setterMethod.getParameterTypes().length > 1)
				{
					throw new ORMException("invalid method found for this tool : " + setterMethod.getName());
				}
				Class<?> classType = setterMethod.getParameterTypes()[0];
				Serializable value = properties.get(qName);
				try
				{
					setterMethod.invoke(alfrescoORM, getManipulatedValue(classType, value));
				} catch (IllegalAccessException e)
				{
					throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
				} catch (IllegalArgumentException e)
				{
					throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
				} catch (InvocationTargetException e)
				{
					throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
				}
			} else if (field.isAnnotationPresent(AlfrescoAspect.class))
			{
				Method setterMethod = ReflectionUtil.setMethod(alfrescoORM.getClass(), field.getName());
				if (setterMethod.getParameterTypes().length == 0 || setterMethod.getParameterTypes().length > 1)
				{
					throw new ORMException("invalid method found for this tool : " + setterMethod.getName());
				}
				Class<?> classType = setterMethod.getParameterTypes()[0];
				try
				{
					getFilledObject(nodeRef, (AlfrescoORM) classType.newInstance(),isLazy);
				} catch (InstantiationException e)
				{
					throw new ORMException(e.getMessage() + "class type: " + classType, e);
				} catch (IllegalAccessException e)
				{
					throw new ORMException(e.getMessage() + "class type: " + classType, e);
				}
			} else if (field.isAnnotationPresent(AlfrescoAssociation.class))
			{
				AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
				if (null == alfrescoQName)
				{
					throw new ORMException("please add alfresco quname aspect to the association");
				}
				QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
				AlfrescoAssociation alfrescoAssociation = field.getAnnotation(AlfrescoAssociation.class);
				List<AssociationRef> associationRefs = nodeService.getTargetAssocs(nodeRef, qName);
				List<AlfrescoORM> associationList = new ArrayList<AlfrescoORM>();
				for (AssociationRef associationRef : associationRefs)
				{
					if (nodeService.exists(associationRef.getTargetRef()))
					{
						try
						{
							AlfrescoORM alfrescoORMForAssociation = null ;
							if(!isLazy)
							{
								alfrescoORMForAssociation = alfrescoAssociation.type().newInstance();								
								getFilledObject(associationRef.getTargetRef(), alfrescoORMForAssociation,isLazy);
							}
							else
							{
								alfrescoORMForAssociation = LazyProxyFactoryBean.getLazyProxyFactoryBean().getObject(associationRef.getTargetRef().getId(),alfrescoAssociation.type()) ;
							}
							associationList.add(alfrescoORMForAssociation);
							
						} catch (InstantiationException e)
						{
							throw new ORMException(e.getMessage() + "class type: " + alfrescoAssociation.type(), e);
						} catch (IllegalAccessException e)
						{
							throw new ORMException(e.getMessage() + "class type: " + alfrescoAssociation.type(), e);
						}

					} else
					{
						// invalid association noderef found
					}
				}
				if (!associationList.isEmpty())
				{
					Method setterMethod = ReflectionUtil.setMethod(alfrescoORM.getClass(), field.getName());
					if (alfrescoAssociation.many())
					{
						try
						{
							setterMethod.invoke(alfrescoORM, associationList);
						} catch (IllegalArgumentException e)
						{
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						} catch (IllegalAccessException e)
						{
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						} catch (InvocationTargetException e)
						{
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						}
					} else
					{
						try
						{
							setterMethod.invoke(alfrescoORM, associationList.get(0));
						} catch (IllegalArgumentException e)
						{							
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						} catch (IllegalAccessException e)
						{
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						} catch (InvocationTargetException e)
						{
							throw new ORMException(e.getMessage() + " method : " + setterMethod, e);
						}
					}
				}
			}
		}

	}

	/**
	 * Method provided Type of object based on passed value return value is
	 * manipulated by method logic
	 * 
	 * @param <T>
	 * @param type
	 * @param serializableValue
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private static <T> T getManipulatedValue(final Class<T> type, final Serializable serializableValue)
	{
		T retT = null;
		if (null != serializableValue)
		{
			retT = (T) serializableValue;
		} else if (type.equals(Date.class))
		{
			retT = null;
		} else if (type.equals(String.class))
		{
			retT = (T) "";
		} else if (type.equals(Integer.class))
		{
			retT = (T) Integer.valueOf(0);
		} else if (type.equals(Long.class))
		{
			retT = (T) Long.valueOf(0);
		} else if (type.equals(Double.class))
		{
			retT = (T) ZERO_DOUBLE;
		} else if (type.equals(Float.class))
		{
			retT = (T) ZERO_FLOAT;
		} else if (type.equals(Boolean.class))
		{
			retT = (T) Boolean.FALSE;
		}
		return retT;
	}
}
