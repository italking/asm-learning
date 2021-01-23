package com.example.remap;

public class MyClass {
	
	public void callA(){
		System.out.println("callA");
		this.callB();
	}
	
	public void callB(){
		System.out.println("callB");
	}
	
}
