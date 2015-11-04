package com.example.stack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;
import org.objectweb.asm.tree.MethodNode;

public class AnalyzerAdapterTestPop {
	
	public static Map<String,MethodNode> methods = new HashMap<String,MethodNode>();  
	
	public static void main(String[] args) {
		try {
			ClassReader 	classReader   = new ClassReader("com.example.stack.TestStack");
			ClassWriter 	classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS); 
			
			MyClassVisitor  classvisitor = new MyClassVisitor(classWriter);
						
			classReader.accept(classvisitor, 0);
		
			
			for(String key:methods.keySet()){
				MethodNode node = methods.get(key);
				if(!key.equals("<init>")){
					System.out.println("----------"+node.name+"----------");	
					AnalyzerAdapter adapter = new AnalyzerAdapter("com/example/stack/TestStack", node.access,node.name, node.desc, new MethodVisitor(327680) {} );
					for(int i=0;i<node.instructions.size();i++){
						node.instructions.get(i).accept(adapter);
						if(adapter.stack!=null){
							System.out.println(node.instructions.get(i)+" : "+node.instructions.get(i).getOpcode()+" : "+ adapter.stack.size());
						}
						
					}
						
					
				}
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static class MyClassVisitor extends ClassVisitor {
		@Override
		public void visit(int version, int access, String name, String signature, String superName,String[] interfaces) {
			super.visit(version, access, name, signature, superName, interfaces);
			//System.out.println(name);
		}

		public MyClassVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}
		
		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			super.visitMethod(access, name, desc, signature, exceptions);
			MethodNode node = new MethodNode(access,name,desc,signature,exceptions); 
			methods.put(name, node);
			return node;
		}
	} 
}
