package com.example;

import com.newrelic.api.agent.weaver.Weaver;

public class WeaverTest {
	
	public void  a(){
		
	}
	
	public static Double getValue(){
		return  Weaver.callOriginal();
	}
	
	public static Object getValueObject(){
		return  Weaver.callOriginal();
	}
	
	public static int[] getValueIntArray(){
		return  Weaver.callOriginal();
	}
	
	public  int[] getValueI(){
		Weaver.callOriginal();
		a();
		return (int[])null;
	}
}
