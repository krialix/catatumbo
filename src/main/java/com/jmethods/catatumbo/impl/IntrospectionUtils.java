/*
 * Copyright 2016 Sai Pullabhotla.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jmethods.catatumbo.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.jmethods.catatumbo.EntityManagerException;
import com.jmethods.catatumbo.Property;

/**
 * Utility methods for helping with introspection/reflection.
 * 
 * @author Sai Pullabhotla
 *
 */
public class IntrospectionUtils {

	/**
	 * Returns the meatdata for the given field.
	 * 
	 * @param field
	 *            the field whose metadata has to be prepared
	 * @return metadata of the given field.
	 */
	public static PropertyMetadata getPropertyMetadata(Field field) {
		int modifiers = field.getModifiers();
		if (Modifier.isStatic(modifiers)) {
			return null;
		}

		String fieldName = field.getName();
		String mappedName = null;
		boolean indexed = true;
		Class<?> fieldType = field.getType();

		DataType dataType = null;
		dataType = DataType.forClass(fieldType);
		if (dataType == null) {
			String message = String.format("Unknown or unsupported type, %s, for field %s in class %s. ", fieldType,
					fieldName, field.getDeclaringClass().getName());
			throw new EntityManagerException(message);
		}

		Property property = field.getAnnotation(Property.class);
		if (property != null) {
			mappedName = property.name();
			indexed = property.indexed();
		}
		if (mappedName == null || mappedName.trim().length() == 0) {
			mappedName = fieldName;
		}

		PropertyMetadata propertyMetadata = new PropertyMetadata(field, mappedName, dataType, indexed);

		// For fields that have @Property annotation, we expect both setter and
		// getter methods. For all other fields, we only treat them as
		// persistable if we find valid getter and setter methods.
		try {
			propertyMetadata.setReadMethod(getReadMethod(propertyMetadata));
			propertyMetadata.setWriteMethod(getWriteMethod(propertyMetadata));
			return propertyMetadata;
		} catch (EntityManagerException exp) {
			if (property != null) {
				throw exp;
			}
		}
		return null;
	}

	/**
	 * Returns the read method for the given property.
	 *
	 * @param propertyMetadata
	 *            the property metadata.
	 * @return the read method for the given property.
	 */
	public static Method getReadMethod(PropertyMetadata propertyMetadata) {
		Field field = propertyMetadata.getField();
		Method readMethod = null;
		switch (propertyMetadata.getDataType()) {
		case BOOLEAN:
			// case BOOLEAN_OBJECT:
			String booleanReadMethodName = getReadMethodNameForBoolean(field);
			try {
				readMethod = getReadMethod(propertyMetadata, booleanReadMethodName);
				break;
			} catch (EntityManagerException exp) {
				// Do nothing... try the default option - getXXX method.
			}
		default:
			String readMethodName = getReadMethodName(field);
			readMethod = getReadMethod(propertyMetadata, readMethodName);
			break;
		}
		return readMethod;
	}

	/**
	 * Gets the method object with the given name and return type.
	 * 
	 * @param clazz
	 *            the class that is supposed to have the specified method
	 *
	 * @param readMethodName
	 *            the method name
	 * @param expectedReturnType
	 *            the return type
	 * @return the Method object with the given name and return type.
	 */
	public static Method getReadMethod(Class<?> clazz, String readMethodName, Class<?> expectedReturnType) {
		try {
			Method readMethod = clazz.getMethod(readMethodName);
			int modifier = readMethod.getModifiers();
			if (Modifier.isStatic(modifier)) {
				throw new EntityManagerException(
						String.format("Method %s in class %s must not be static", readMethodName, clazz.getName()));
			}
			if (Modifier.isAbstract(modifier)) {
				throw new EntityManagerException(
						String.format("Method %s in class %s must not be abstract", readMethodName, clazz.getName()));
			}
			if (!Modifier.isPublic(modifier)) {
				throw new EntityManagerException(
						String.format("Method %s in class %s must  be public", readMethodName, clazz.getName()));
			}

			if (!expectedReturnType.isAssignableFrom(readMethod.getReturnType())) {
				throw new EntityManagerException(String.format("Method %s in class %s must have a return type of %s",
						readMethodName, clazz.getName(), expectedReturnType));
			}
			return readMethod;
		} catch (NoSuchMethodException exp) {
			throw new EntityManagerException(
					String.format("Method %s is required in class %s", readMethodName, clazz.getName()));
		} catch (SecurityException exp) {
			throw new EntityManagerException(exp.getMessage(), exp);
		}

	}

	/**
	 * Returns the Method object that allows reading of the given property.
	 *
	 * @param propertyMetadata
	 *            the property metadata
	 * @param readMethodName
	 *            the method name (e.g. getXXX or isXXX).
	 * @return the read Method.
	 */
	public static Method getReadMethod(PropertyMetadata propertyMetadata, String readMethodName) {
		return getReadMethod(propertyMetadata.getField().getDeclaringClass(), readMethodName,
				propertyMetadata.getDeclaredType());
	}

	/**
	 * Returns the write method(setter method) for the given property.
	 *
	 * @param propertyMetadata
	 *            the property
	 * @return the write Method
	 */
	public static Method getWriteMethod(PropertyMetadata propertyMetadata) {
		String writeMethodName = getWriteMethodName(propertyMetadata.getField());
		return getWriteMethod(propertyMetadata.getField().getDeclaringClass(), writeMethodName,
				propertyMetadata.getField().getType());
	}

	/**
	 * Returns the write method with the given name and parameter type.
	 * 
	 * @param clazz
	 *            The class that is supposed to have the specified method.
	 *
	 * @param writeMethodName
	 *            the method name
	 * @param parameterType
	 *            the parameter type.
	 * @return the write Method.
	 */
	public static Method getWriteMethod(Class<?> clazz, String writeMethodName, Class<?> parameterType) {
		try {
			Method writeMethod = clazz.getMethod(writeMethodName, parameterType);
			int modifier = writeMethod.getModifiers();
			if (Modifier.isStatic(modifier)) {
				throw new EntityManagerException(
						String.format("Method %s in class %s must not be static", writeMethodName, clazz.getName()));
			}
			if (Modifier.isAbstract(modifier)) {
				throw new EntityManagerException(
						String.format("Method %s in class %s must not be abstract", writeMethodName, clazz.getName()));
			}
			return writeMethod;
		} catch (NoSuchMethodException ex) {
			throw new EntityManagerException(String.format("Method %s (%s) is required in class %s", writeMethodName,
					parameterType.getName(), clazz.getName()));
		}

	}

	/**
	 * Returns the name of the method that can be used to read the given field.
	 *
	 * @param field
	 *            the field
	 * @return the name of the read method,
	 */
	public static String getReadMethodName(Field field) {
		return "get" + getCapitalizedName(field.getName());

	}

	/**
	 * Returns the name of the method that can be used to read the given boolean
	 * field.
	 *
	 * @param field
	 *            the field name
	 * @return the name of the read method.
	 */
	public static String getReadMethodNameForBoolean(Field field) {
		return "is" + getCapitalizedName(field.getName());

	}

	/**
	 * Returns the name of the method that can be used to write (or set) the
	 * given field.
	 *
	 * @param field
	 *            the name of the field
	 * @return the name of the write method.
	 */
	public static String getWriteMethodName(Field field) {
		return "set" + getCapitalizedName(field.getName());

	}

	/**
	 * Capitalizes the given field name.
	 *
	 * @param fieldName
	 *            the field name
	 * @return capitalized field name.
	 */
	public static String getCapitalizedName(String fieldName) {
		return Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
	}

	/**
	 * Creates a new object of given class by invoking the class' default public
	 * constructor.
	 * 
	 * @param clazz
	 *            the class whose instance needs to be created
	 * @return a new instance of the given class
	 * @throws EntityManagerException
	 *             if any error occurs
	 */
	public static Object instantiateObject(Class<?> clazz) {
		try {
			Constructor<?> constructor = clazz.getConstructor();
			return constructor.newInstance();
		} catch (Exception exp) {
			throw new EntityManagerException(exp);
		}

	}

}
