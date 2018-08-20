package com.uetty.common.tool.core.proxy;

public class CglibProxyDemo {

	public static void main(String[] args) {
		Be b = new CglibProxy().getObjectInstance(new Be());
		b.getB();
		
	}
	
}


class Be {
	
	int b = 0;
	
	public int getB() {
		return b;
	}
}