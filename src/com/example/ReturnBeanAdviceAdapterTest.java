package com.example;

import java.io.File;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import com.bean.ReturnBean;
import com.bean.invoke.ReturnBeanInvoke;

public class ReturnBeanAdviceAdapterTest {
	public static class WrappMehtod extends AdviceAdapter {

		@Override
		protected void onMethodExit(int opcode) {
			if(opcode!=Opcodes.ATHROW){
				
				
				System.out.println(opcode);
				
				int index = newLocal(Type.getType(String.class));
				
				
				System.out.println(index);
				
				storeLocal(index, Type.getType(String.class));
				
				System.out.printf(Type.getInternalName(ReturnBean.class));
				//Type.get
				mv.visitVarInsn(ALOAD, 0);
				mv.visitMethodInsn(INVOKESTATIC, Type.getInternalName(ReturnBeanInvoke.class), "invoke", "(Ljava/lang/Object;)V", false);
				
				mv.visitVarInsn(Opcodes.ALOAD, index);
			}
		}

		protected WrappMehtod(MethodVisitor mv, int access,String name, String desc) {
			super(Opcodes.ASM4, mv, access, name, desc);
		}
		
		@Override
		protected void onMethodEnter() {
			super.onMethodEnter();
			//mv.visitVarInsn(ALOAD, 0);
			//mv.visitMethodInsn(INVOKESTATIC, "com/bean/ReturnBeanInvoke", "invoke", "(Ljava/lang/Object;)V", false);
		}
		
		
		
	}
	
	public static class ReturnAdapter extends ClassVisitor{

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,String signature, String[] exceptions) {
		    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		    if(name.equals("getReName")){
		    	mv = new WrappMehtod(mv,access, name, desc);
		    }
		    return mv;

		}

		public ReturnAdapter(ClassVisitor cv) {
			super(Opcodes.ASM4,cv);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
    	ClassReader classReader   = new ClassReader("com.bean.ReturnBean");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS);  
        ReturnAdapter classVisitor = new ReturnAdapter(classWriter);  
        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
        byte[] classFile = classWriter.toByteArray();
        
        FileOutputStream os = new FileOutputStream(new File("D://tmp//ReturnBean.class"));
        os.write(classFile);
        os.close();
        
    }
}
