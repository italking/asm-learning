/**
 * Classes.java 9:22:44 AM Apr 23, 2012
 *
 * Copyright(c) 2000-2012 HC360.COM, All Rights Reserved.
 */
package com.example;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Modifier;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * <p>
 * 基于asm的工具类
 * </p>
 * 
 * @author dixingxing
 * @date Apr 23, 2012
 */
public final class ChangeMethodParamNames {
	
	public static class  ChangeMethodParamMehtodAdapter extends MethodVisitor{
		@Override
		 public void visitLocalVariable(String name, String desc, String signature, Label start, Label end, int index) {
			System.out.println(name);
			if("name".equals(name)){
				name = "newName";
			}
			super.visitLocalVariable(name, desc, signature, start, end, index);
			
		}

		public ChangeMethodParamMehtodAdapter(MethodVisitor visitor) {
			super(Opcodes.ASM4 ,visitor);
		}
	}
	
	
	public static class ChangeMethodParamClassAdapter extends ClassVisitor{
		  
	    public ChangeMethodParamClassAdapter(ClassVisitor visitor) {  
	    	super(Opcodes.ASM4,visitor );
	    }  
	  
	    @Override  
	    public MethodVisitor visitMethod(int access, String name, String desc,  String signature, String[] exceptions) {  
	        MethodVisitor v = super.visitMethod(access, name, desc,signature, exceptions);
	        return  new ChangeMethodParamMehtodAdapter(v);
	        
	    }  
	  
	    @Override  
	    public void visitEnd() {  
	       super.visitEnd(); 
	    }  
	  
	} 
	
	
    public static void main(String[] args) throws Exception {
    	ClassReader classReader   = new ClassReader("com.bean.User");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS);  
        ClassVisitor classVisitor = new ChangeMethodParamClassAdapter(classWriter);  
        classReader.accept(classVisitor,0);
        byte[] classFile = classWriter.toByteArray();
        
        FileOutputStream os = new FileOutputStream(new File("c://User.class"));
        os.write(classFile);
        os.close();
        
    }

}