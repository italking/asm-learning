package com.example.outerclass;

import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public  class  OutterTest {
	
	public static class MyClassVisitor  extends ClassVisitor {
		public MyClassVisitor() {
			super(Opcodes.ASM5);
		}

		@Override
		public void visitOuterClass(String owner, String name, String desc) {
			super.visitOuterClass(owner, name, desc);
			System.out.println("owner=" + owner +",name=" + name + ",desc=" + desc );
		}

		
	}
	
	
	
	public static void main(String[] args) throws IOException {
		ClassReader classReader   = new ClassReader("com.example.outerclass.OuterClass$1");  
		//ClassReader classReader   = new ClassReader("com.example.outerclass.OuterClass$InnerClass");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS); 
        classReader.accept(new MyClassVisitor(),ClassReader.EXPAND_FRAMES);
	}
}
