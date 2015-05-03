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

	public static ObjectFillHelper getCreateHelper()
	{
		return objectFillHelper;
	}

	private ObjectFillHelper(final BeanFactory beanFactory, final ServiceRegistry serviceRegistry)
	{
		this.beanFactory = beanFactory;
		this.serviceRegistry = serviceRegistry;
	}

	public void getFilledObject(final NodeRef nodeRef, final AlfrescoORM alfrescoORM) throws ORMException
	{

		NodeService nodeService = serviceRegistry.getNodeService();
		if (!nodeService.exists(nodeRef))
		{
			throw new ORMException("NodeRef does not exist : " + nodeRef);
		}
		Map<QName, Serializable> properties = nodeService.getProperties(nodeRef);
		List<Field> fields = new ArrayList<Field>();
		ReflectionUtil.getFields(alfrescoORM.getClass(), fields);

		try
		{
			for (Field field : fields)
			{
				if (field.isAnnotationPresent(AlfrescoQName.class) && !field.isAnnotationPresent(AlfrescoAssociation.class))
				{
					AlfrescoQName alfrescoQName = field.getAnnotation(AlfrescoQName.class);
					QName qName = QName.createQName(alfrescoQName.namespaceURI(), alfrescoQName.localName());
					Method setterMethod = ReflectionUtil.setMethod(alfrescoORM.getClass(), field.getName());
					Class<?> classType = setterMethod.getParameterTypes()[0];
					Serializable value = properties.get(qName);
					setterMethod.invoke(alfrescoORM, getManipulatedValue(classType, value));
				} else if (field.isAnnotationPresent(AlfrescoAspect.class))
				{
					Method setterMethod = ReflectionUtil.setMethod(alfrescoORM.getClass(), field.getName());
					Class<?> classType = setterMethod.getParameterTypes()[0];
					getFilledObject(nodeRef, (AlfrescoORM) classType.newInstance());
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
							AlfrescoORM alfrescoORMForAssociation = alfrescoAssociation.type().newInstance();
							associationList.add(alfrescoORMForAssociation);
							getFilledObject(associationRef.getTargetRef(), alfrescoORMForAssociation);

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
							setterMethod.invoke(alfrescoORM, associationList);
						} else
						{
							setterMethod.invoke(alfrescoORM, associationList.get(0));
						}
					}
				}
			}
		} catch (IllegalAccessException e)
		{
			throw new ORMException(e);
		} catch (IllegalArgumentException e)
		{
			throw new ORMException(e);
		} catch (InvocationTargetException e)
		{
			throw new ORMException(e);
		} catch (InstantiationException e)
		{
			throw new ORMException(e);
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
