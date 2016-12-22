package com.example.remap;

import java.io.IOException;
import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.util.TraceClassVisitor;

public class RemapTest {
	public static void main(String[] args) throws Exception {
		test2();
	}
	/**
	 * 测试替换类的 名称， 包含自己调用的也进行替换
	 * @throws Exception
	 */
	public static void test1() throws Exception{
		ClassReader classReader   = new ClassReader("com.example.remap.User");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
        SimpleRemapper remapper   = new SimpleRemapper("com/example/remap/User","com/example/remap/People"){
        	 public String map(String key) {
        		 	System.out.println(key);
        	        String  value = super.map(key);
        	        System.out.println(value);
        	        return value;
        	    }
        };
        RemappingClassAdapter  remappingClassAdapter = new RemappingClassAdapter(classWriter, remapper);
     
        classReader.accept(remappingClassAdapter ,ClassReader.EXPAND_FRAMES);	
        
        byte[] classFile = classWriter.toByteArray();
        ClassReader classReader1   = new ClassReader(classFile);
        classReader1.accept(new TraceClassVisitor(new PrintWriter(System.out)), ClassReader.EXPAND_FRAMES);
	}
	/**
	 * 测试 替换方法，方法名称自动加上返回类型的后缀
	 * @throws Exception
	 */
	public static void test2() throws Exception{
		ClassReader classReader   = new ClassReader("com.example.remap.User");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
        SimpleRemapper remapper   = new SimpleRemapper("",""){
        	 public String map(String key) {
        	        String  value = super.map(key);
        	        return value;
        	    }
        	 public String mapMethodName(String owner, String name, String desc) {
        		   if(name.equals("<init>")){
        			   return name;
        		   }
        		   //不能替换JDK调用
        		   if(!owner.contains("remap")){
        			   return name;
        		   }
        		   Type returnType = Type.getReturnType(desc);
        		   String a= "";
        		   switch(returnType.getSort()){
        		     case Type.VOID:
        		       a= "void";
        		       break;
        		     case Type.OBJECT:
        		       a= returnType.getClassName().replace(".", "_");	 
        		       break;
        		   }
        		   return name+"_"+a;
        	  }

        };
        RemappingClassAdapter  remappingClassAdapter = new RemappingClassAdapter(classWriter, remapper);
     
        classReader.accept(remappingClassAdapter ,ClassReader.EXPAND_FRAMES);	
        
        byte[] classFile = classWriter.toByteArray();
        ClassReader classReader1   = new ClassReader(classFile);
        classReader1.accept(new TraceClassVisitor(new PrintWriter(System.out)), ClassReader.EXPAND_FRAMES);
        
        Class c= new  ByteClassLoader().define("com.example.remap.User", classFile);
        	  c.getMethod("addUser_void",new Class[]{String.class}).invoke(null, new Object[]{"张三"});	
        
	
	}
	static class ByteClassLoader extends ClassLoader {
	    public Class define(String name, byte[] body) {
	        return defineClass(name, body, 0, body.length);
	    }
	}
	
}
