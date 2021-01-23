package com.example.annotation;

import java.io.IOException;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

public  class  AnnotationTest {
	
	public static class MyClassVisitor  extends ClassVisitor {
		public MyClassVisitor() {
			super(Opcodes.ASM5);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
			return new MyAnnotationVisitor(super.visitAnnotation(desc, visible));
		}
		
		
		
	}
	
	public static  class MyAnnotationVisitor extends AnnotationVisitor {

		public MyAnnotationVisitor( AnnotationVisitor av) {
			super(Opcodes.ASM5,av);
		}
		/**
		 * 读取注解的值
		 */
		@Override
		public void visit(String name, Object value) {
			super.visit(name, value);
			System.out.println(name + " = " + value );
		}
		/*
		 * 注解枚举的类型的值
		 */
		@Override
		public void visitEnum(String name, String desc, String value) {
			super.visitEnum(name, desc, value);
			System.out.println("name ="+ name + ", desc="+desc +" , value=" + value);
		}

		@Override
		public AnnotationVisitor visitAnnotation(String name, String desc) {
			return super.visitAnnotation(name, desc);
		}
		/**
		 * 注解数组类型的值
		 */
		@Override
		public AnnotationVisitor visitArray(String name) {
			System.out.println("Array:" + name );
			return new MyArrayAnnotationVisitor(super.visitArray(name));
		}
		
	}
	
	public static class MyArrayAnnotationVisitor extends AnnotationVisitor{

		public MyArrayAnnotationVisitor(AnnotationVisitor av) {
			super(Opcodes.ASM5 , av);
		}
		/**
		 * 读取数组的内容
		 */
		@Override
		public void visit(String name, Object value) {
			super.visit(name, value);
			System.out.println(name + " = " + value );
		}
		
		
	}
	
	public static void main(String[] args) throws IOException {
		ClassReader classReader   = new ClassReader("com.example.annotation.MyClass");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS); 
        classReader.accept(new MyClassVisitor(),ClassReader.EXPAND_FRAMES);
	}
}
