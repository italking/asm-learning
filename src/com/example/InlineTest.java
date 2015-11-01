package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.MethodCallInlinerAdapter;
import org.objectweb.asm.commons.MethodCallInlinerAdapter.InlinedMethod;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class InlineTest {
	
	public static class Line extends ClassVisitor{

		@Override
		public void visit(int version, int access, String name,
				String signature, String superName, String[] interfaces) {
			// TODO Auto-generated method stub
			super.visit(version, access, name, signature, superName, interfaces);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,String signature, String[] exceptions) {
			 MethodVisitor mv =  super.visitMethod(access, name, desc, signature, exceptions);
			 if(name.equals("test1")){
				 
			 
			 mv =  new MethodCallInlinerAdapter(null,  access,  name,  desc,  mv, false) {
				@Override
				protected InlinedMethod mustInline(String owner, String name, String desc) {
					System.out.println(name);
					if(!name.equals("callOri")){
						return null;
					}
					ClassReader cr =null;
					try {
						cr = new ClassReader("com.bean.Lineb");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 	ClassNode cn = new ClassNode();
				 		      cr.accept(cn,0);	
					return new InlinedMethod((MethodNode)cn.methods.get(1), new Remapper() {}) ;
				}
			};
			 }
			 return mv;
		}

		public Line(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}
		
	}
	
	public static class MyClassLoader extends ClassLoader{
		 public Class<?> defineClass(String name, byte[] b){
			 return super.defineClass(name, b, 0, b.length);
		 }
	}
	
	public static void main(String[] args) throws Exception{
		ClassReader classReader   = new ClassReader("com.bean.Linea");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
        Line classVisitor 		  = new Line(classWriter);  
        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
        byte[] classFile = classWriter.toByteArray();
        FileOutputStream os = new FileOutputStream(new File("D://tmp//Linea.class"));
        os.write(classFile);
        os.close();
        Class<?> la =  new MyClassLoader().defineClass("com.bean.Linea", classFile);
        Object oa	= la.newInstance();
        Method ma =	   oa.getClass().getMethod("test1" );
        	   ma.invoke(oa);
        	   
	}
}
