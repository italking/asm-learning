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
	public static String delUser1(String name){
		
		try {
			System.out.println(name);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name;
	}
}
