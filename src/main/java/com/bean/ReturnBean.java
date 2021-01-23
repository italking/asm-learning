package com.bean;

import com.bean.invoke.ReturnBeanInvoke;

public class ReturnBean {

	public String getName(String name){
		return name;
	}
	
	
	public String getReName(){
		ReturnBeanInvoke.invoke(this);
		return  getName("11");
	}
	
}
