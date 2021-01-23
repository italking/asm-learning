package com.example.remap;

import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.util.TraceClassVisitor;

public class ClassRemapperTest {
	
	public static void main(String[] args) throws Exception{
		ClassReader classReader   = new ClassReader("com.example.remap.MyClass");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        //修改Class名称
        SimpleRemapper remapper   = new SimpleRemapper("com/example/remap/MyClass","com/example/remap/YourClass");
        
        ClassRemapper  mapper = new ClassRemapper(classWriter, remapper);
     
        classReader.accept(mapper ,ClassReader.EXPAND_FRAMES);	
        
        byte[] classFile = classWriter.toByteArray();
        ClassReader classReader1   = new ClassReader(classFile);
        classReader1.accept(new TraceClassVisitor(new PrintWriter(System.out)), ClassReader.EXPAND_FRAMES);
        
        //使用反射调用callA方法
        Class c= new  ByteClassLoader().define("com.example.remap.YourClass", classFile);
        	  c.getMethod("callA",new Class[]{}).invoke( c.newInstance() );	
        
	
	}
	static class ByteClassLoader extends ClassLoader {
	    public Class define(String name, byte[] body) {
	        return defineClass(name, body, 0, body.length);
	    }
	}
}
