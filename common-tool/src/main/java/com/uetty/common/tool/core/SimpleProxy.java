package com.uetty.common.tool.core;

import java.lang.reflect.Method;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class SimpleProxy implements MethodInterceptor {
	
	@SuppressWarnings("unchecked")
	public <T> T getObjectInstance(T t){
		Enhancer hancer = new Enhancer();
		hancer.setSuperclass(t.getClass());
		hancer.setCallback(this);
		hancer.setClassLoader(t.getClass().getClassLoader());
		return (T) hancer.create();
	}
	
	@Override
	public Object intercept(Object arg0, Method arg1, Object[] arg2, MethodProxy arg3) throws Throwable {

		System.out.println("----欢迎光临，隔壁老王为您服务----");
		System.out.println("----您的名字是----"+arg1.getDeclaringClass().getName());
		System.out.println("----您的套餐是----"+arg1.getDeclaringClass().getName()+"."+arg1.getName());
		Object object = arg3.invokeSuper(arg0, arg2);
		System.out.println("----谢谢惠顾，欢迎下次再来----");
		
		return object;
	}

}
