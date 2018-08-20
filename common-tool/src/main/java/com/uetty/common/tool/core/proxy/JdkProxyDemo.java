package com.uetty.common.tool.core.proxy;

interface WriteService {
	public void write();
}
 
interface ReadService {
	//阅读
	public void read();
}
 
class LearnServiceImpl implements ReadService,WriteService {
 
	@Override
	public void read() {
		System.out.println("read()...");
	}
 
	@Override
	public void write() {
		System.out.println("write()...");
	}
 
}
 
public class JdkProxyDemo {
	public static void main(String[] args) {
		LearnServiceImpl ro = new LearnServiceImpl();
		ReadService proxy0 = new JdkProxy<ReadService>(ro).createDynamicProxy();
		proxy0.read();
		System.out.println("代理对象：" + proxy0.getClass().getName());
		System.out.println("===========");
		WriteService proxy1 = new JdkProxy<WriteService>(ro).createDynamicProxy();
		proxy1.write();
		System.out.println("代理对象：" + proxy1.getClass().getName());
	}
}