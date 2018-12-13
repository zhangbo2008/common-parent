package com.uetty.common.tool.core.proxy;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 * @author vince
 *
 */
public class ReflectUtil {

	private static String getterName(String fieldName) {
		return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	private static String setterName(String fieldName) {
		return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
	}
	
	public static Object getFieldValue(Object obj, String fieldName) {
		Class<? extends Object> clz = obj.getClass();
		try {
			String getterName = getterName(fieldName);
			Method method = clz.getMethod(getterName);
			if (method != null) {
				return method.invoke(obj);
			}
		} catch (Exception e) {}
		try {
			Field field = clz.getDeclaredField(fieldName);
			if (field != null) {
				field.setAccessible(true);
				return field.get(obj);
			}
		} catch (Exception e) {};
		try {
			Field field = clz.getField(fieldName);
			if (field != null) {
				return field.get(obj);
			}
		} catch (Exception e) {}
		throw new RuntimeException("field[" + fieldName + "] not found in " + obj);
	}
	
	public static void setFieldValue(Object obj, String fieldName, Object value) {
		Class<? extends Object> clz = obj.getClass();
		try {
			if (value != null) {
				String setterName = setterName(fieldName);
				Method method = clz.getMethod(setterName, value.getClass());
				if (method != null) {
					method.invoke(obj, value);
					return;
				}
			}
		} catch (Exception e) {}
		try {
			Field field = clz.getDeclaredField(fieldName);
			if (field != null) {
				field.setAccessible(true);
				field.set(obj, value);
				return;
			}
		} catch (Exception e) {}
		try {
			Field field = clz.getField(fieldName);
			if (field != null) {
				field.set(obj, value);
				return;
			}
		} catch (Exception e) {}
		throw new RuntimeException("field[" + fieldName + "] not found in " + obj);
	}
}
