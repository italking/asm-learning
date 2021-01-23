package com.example;

import java.io.File;
import java.io.FileOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class AdviceAdapterTest {
	public static class WrappMehtod extends AdviceAdapter {

		protected WrappMehtod(MethodVisitor mv, int access,String name, String desc) {
			super(Opcodes.ASM5, mv, access, name, desc);
		}
		
		@Override
		protected void onMethodEnter() {
			super.onMethodEnter();
			mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
			mv.visitLdcInsn("123213213");
			mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
		}
	}
	
	public static class ReturnAdapter extends ClassVisitor{

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc,String signature, String[] exceptions) {
		    MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
		    			  mv = new WrappMehtod(mv,access, name, desc);
		    return mv;			  

		}

		public ReturnAdapter(ClassVisitor cv) {
			super(Opcodes.ASM5,cv);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
    	ClassReader classReader   = new ClassReader("com.bean.User");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_MAXS);  
        ReturnAdapter classVisitor = new ReturnAdapter(classWriter);  
        classReader.accept(classVisitor,0);
        byte[] classFile = classWriter.toByteArray();
        
        FileOutputStream os = new FileOutputStream(new File("D://tmp//User.class"));
        os.write(classFile);
        os.close();
        
    }
}
