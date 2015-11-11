package com.example.type;

import org.objectweb.asm.Type;

public class TypeTest {
	public static void main(String[] args) {
	    System.out.println(Type.getType(Object.class));
	    System.out.println(Type.getType(Object.class).getSort());
	    System.out.println(Type.getType(Object[].class));
	    System.out.println(Type.getType(Object[].class).getSort());
	}
}
