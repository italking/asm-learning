package com.example.stack;
/**
 * 测试虚拟机 添加POP 或POP2 指令的时机
 * @author: icewaterpp  
 *	
 * @date: 2015年11月4日 上午11:23:00
 */
public class TestStack {
	
	public static void testVoid(){
		StackMethod.returndubbleMethod();
		StackMethod.voidMethod();
	}
	
	public static void testVoid1(){
		StackMethod.returnDubbleMethod();
		StackMethod.voidMethod();
	}
	
	public static Double testReturn(){
		StackMethod.returndubbleMethod();
		return StackMethod.returnDubbleMethod();
	}
	
}
