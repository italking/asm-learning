package com.example.emptymethod;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class EmptyMethodTest {
	public static void main(String[] args) throws IOException {
		ClassReader classReader   = new ClassReader("com.example.emptymethod.EmptyBean");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
        EmptyMethodClassVisitor classVisitor = new EmptyMethodClassVisitor(classWriter);  
        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
        byte[] classFile = classWriter.toByteArray();
        
        FileOutputStream os = new FileOutputStream(new File("c://EmptyBean.class"));
        os.write(classFile);
        os.close();
        
        Class eb = new MyClassLoader().defineClass("com.example.emptymethod.EmptyBean", classFile);
		
	}

	public static class EmptyMethodClassVisitor extends ClassVisitor{
		private String name;
		private String signature;
		

		@Override
		public void visit(int version, int access, String name, String signature, String superName,String[] interfaces) {
			// TODO Auto-generated method stub
			super.visit(version, access, name, signature, superName, interfaces);
			this.name = name;
			this.signature = signature;
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			
			
			
			MethodVisitor m = super.visitMethod(access, name, desc, signature, exceptions);
			System.out.println(name+":"+desc+":"+signature);
			if(name.equals("test")){
				if(Type.getReturnType(desc).getSort()==Type.VOID){
					m.visitCode();
					Label start = new Label();
					Label end	= new Label();
					m.visitLabel(start);
					m.visitInsn(Opcodes.RETURN);
					m.visitLabel(end);
					m.visitMaxs(0, Type.getArgumentTypes(desc).length);
					int index = 0;
					if( (Opcodes.ACC_STATIC & access) <=0 ){
						m.visitLocalVariable(name, desc, signature, start, end, index);
						index++;
					}
				    Type[] as =	Type.getArgumentTypes(desc);
					for(Type t:as){
						m.visitLocalVariable(t.getInternalName(), t.getDescriptor(), null, start, end, index);
						index++;
					}
					m.visitEnd();
					
					return null;
				}
			}
			
			return m;
		}

		public EmptyMethodClassVisitor(ClassVisitor classVisitor) {
			super(Opcodes.ASM5,classVisitor);
		}
	}

	public static class MyClassLoader extends ClassLoader{
		public Class defineClass(String name, byte[] data){
		 return	super.defineClass(name, data, 0, data.length);
		}
	}
	
	
}
