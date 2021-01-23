package com.example.outerclass;

public class OuterClass {
	
	public void test(){
		new Runnable() {
			@Override
			public void run() {
				System.out.println("test");
			}
		};
	}
}
