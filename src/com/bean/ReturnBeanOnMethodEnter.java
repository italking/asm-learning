package com.bean;

import com.bean.invoke.ReturnBeanOnMethodEnterInvoke;



public class ReturnBeanOnMethodEnter {

	public String getName(String name){
		return name;
	}
	
	
	public String getReName(){
		//ReturnBeanOnMethodEnterInvoke.invoke(this);
		return  getName("11");
	}
	
}
