package com.bean.invoke;


public class ReturnBeanOnMethodEnterInvoke {

	public static void invoke(Object o){
		System.out.println(o.getClass().getName());
		//return null;
	}
	
	public static void invoke(){
		System.out.println("111");
	}
	
	public static void main(String[] args) {
		//invoke(new Object());
	}
}
