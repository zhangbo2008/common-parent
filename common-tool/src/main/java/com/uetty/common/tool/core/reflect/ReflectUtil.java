package com.uetty.common.tool.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具类
 * @author vince
 */
public class ReflectUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);

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
			return invokeMethod(obj, getterName);
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
			String setterName = setterName(fieldName);
			invokeMethod(obj, setterName, value);
			return;
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
	
	public static List<String> getFields(Object obj) {
		Class<? extends Object> clz = obj.getClass();
		Field[] fields = clz.getDeclaredFields();
		List<String> list = new ArrayList<String>();
		for (Field field : fields) {
			String name = field.getName();
			list.add(name);
		}
		return list;
	}
	
	public static Class<?> getFieldClass(Object obj, String fieldName) {
		Class<? extends Object> clz = obj.getClass();
		try {
			Field field = clz.getDeclaredField(fieldName);
			if (field != null) {
				field.setAccessible(true);
				return field.getType();
			}
		} catch (Exception e) {};
		try {
			Field field = clz.getField(fieldName);
			if (field != null) {
				return field.getType();
			}
		} catch (Exception e) {}
		throw new RuntimeException("field[" + fieldName + "] not found in " + obj);
	}
	
	public static Object getInstance(Class<? extends Object> clz, Object... params) {
		Constructor<?>[] constructors = clz.getConstructors();
		for (Constructor<?> constructor : constructors) {
			try {
				Class<?>[] parameterTypes = constructor.getParameterTypes();
				if (parameterTypes.length != params.length) {
					continue;
				}
				constructor.setAccessible(true);
				return constructor.newInstance(params);
			} catch (Exception e) {}
		}
		if (params.length == 0) {
			try {
				return clz.newInstance();
			} catch (Exception e) {}
		}
		
		String errorMsg = "constructor(";
		for (int i = 0; i < params.length; i++) {
			if (i != 0) errorMsg += ", ";
			errorMsg += params[i] == null ? "null" : params[i].getClass().getName();
		}
		errorMsg += ") not found in class[" + clz.getCanonicalName() + "]";
		throw new RuntimeException(errorMsg);
	}
	
	public static Object invokeMethod(Object obj, String methodName, Object... params) {
		Class<? extends Object> clz = obj.getClass();
		if (params.length == 0) {
			try {
				Method method = clz.getMethod(methodName);
				return method.invoke(obj);
			} catch (Exception e) {}
		} else {
			// 绝对匹配
			boolean noNull = true;
			Class<?> pclzs[] = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++) {
				Object p = params[i];
				if (p == null) {
					noNull = false;
					break;
				}
				pclzs[i] = p.getClass();
			}
			if (noNull) {
				try {
					Method method = clz.getMethod(methodName, pclzs);
					return method.invoke(obj, params);
				} catch (Exception e) {}
			}
			
			// 子类型匹配
			Method[] methods = clz.getMethods();
			for (Method m : methods) {
				try {
					if (!m.getName().equals(methodName)) continue;
					Class<?>[] parameterTypes = m.getParameterTypes();
					if (parameterTypes.length != params.length) continue;
					return m.invoke(obj, params);
				} catch (Exception e) {}
			}
		}
		
		String errorMsg = "method(";
		for (int i = 0; i < params.length; i++) {
			if (i != 0) errorMsg += ", ";
			errorMsg += params[i] == null ? "null" : params[i].getClass().getName();
		}
		errorMsg += ") not found in class[" + clz.getCanonicalName() + "]";
		throw new RuntimeException(errorMsg);
	}
	
	public static Object invokeMethod(Class<?> clz, String methodName, Object... params) {
		if (params.length == 0) {
			try {
				Method method = clz.getMethod(methodName);
				return method.invoke(null);
			} catch (Exception e) {}
		} else {
			// 绝对匹配
			boolean noNull = true;
			Class<?> pclzs[] = new Class<?>[params.length];
			for (int i = 0; i < params.length; i++) {
				Object p = params[i];
				if (p == null) {
					noNull = false;
					break;
				}
				pclzs[i] = p.getClass();
			}
			if (noNull) {
				try {
					Method method = clz.getMethod(methodName, pclzs);
					return method.invoke(null, params);
				} catch (Exception e) {}
			}
			
			// 子类型匹配
			Method[] methods = clz.getMethods();
			for (Method m : methods) {
				try {
					if (!m.getName().equals(methodName)) continue;
					Class<?>[] parameterTypes = m.getParameterTypes();
					if (parameterTypes.length != params.length) continue;
					return m.invoke(null, params);
				} catch (Exception e) {}
			}
		}
		
		String errorMsg = "static method(";
		for (int i = 0; i < params.length; i++) {
			if (i != 0) errorMsg += ", ";
			errorMsg += params[i] == null ? "null" : params[i].getClass().getName();
		}
		errorMsg += ") not found in class[" + clz.getCanonicalName() + "]";
		throw new RuntimeException(errorMsg);
	}
	
	/**
	 * 类包含的变量名
	 */
	public static void printContainFieldNames(Class<?> clz) {
		Set<String> fieldSet = new HashSet<String>();
		try {
			Field[] fields = clz.getFields();
			for (Field field : fields) {
				fieldSet.add(field.getName());
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		try {
			Field[] fields = clz.getDeclaredFields();
			for (Field field : fields) {
				fieldSet.add(field.getName());
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		for (String string : fieldSet) {
			logger.debug(string);
		}
	}
	
	/**
	 * 打印类所在的文件路径
	 * <p> 排bug时使用
	 */
	public static void printClassFilePath(Class<?> clz) {
		try {
			URL resource = clz.getResource("/");
			if (resource == null) {
				resource = clz.getProtectionDomain().getCodeSource().getLocation();
			}
			logger.debug(resource.getFile());
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 打印classloader中的classpath路径
	 * <p> 排bug时使用
	 */
	public static void printClassloaderClasspath(ClassLoader classloader) {
		Set<String> classpaths = new HashSet<String>();
		getClassloaderClasspath(classloader, classpaths);
		for (Iterator<String> iterator = classpaths.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			logger.debug(string);
		}
	}
	
	/**
	 * 获取classloader中的classpath路径
	 */
	public static void getClassloaderClasspath(ClassLoader classloader, Set<String> classpaths) {
		if (classloader == null || !(classloader instanceof URLClassLoader)) {
			return;
		}
		try  {
			URL[] urls = ((URLClassLoader) classloader).getURLs();
			for (int i = 0; i < urls.length; i++) {
				classpaths.add(urls[i].getPath());
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
		}
		ClassLoader parent = classloader.getParent();
		getClassloaderClasspath(parent, classpaths);
		return;
	}
	
}
