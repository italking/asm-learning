package com.example.remap;

public class User {
	public static  void addUser(String name){
		doAddUser(name);
	}
	
	public static void doAddUser(String name){
		System.out.println(name);
	}
	
	public static String delUser(String name){
		System.out.println(name);
		return name;
	}
}
