package com.example.inline;

import java.io.File;
import java.io.FileOutputStream;

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

/**
 * @author Eric Bruneton
 */
public class InlineExample2 extends ClassLoader implements Opcodes {

    public static void test(final boolean simple,final boolean inlineFrames) throws Exception
    {
        InlineExample2 loader = new InlineExample2();

       
        
        ClassNode cn = new ClassNode();
        ClassReader cr = new ClassReader("org.objectweb.asm.commons.bean.Bean2");
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        final MethodNode toInline =(MethodNode)cn.methods.get(1);
        System.out.println(toInline.name +" 11");

        ClassReader classReader   = new ClassReader("org.objectweb.asm.commons.bean.Bean1");  
        ClassWriter classWriter   = new ClassWriter(ClassWriter.COMPUTE_FRAMES);  
       
       
        // cv = new TraceClassVisitor(cv, new PrintWriter(System.out));
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM4, classWriter) {

            String owner;

            @Override
            public void visit(
                int version,
                int access,
                String name,
                String signature,
                String superName,
                String[] interfaces)
            {
                super.visit(version,
                        access,
                        name,
                        signature,
                        superName,
                        interfaces);
                owner = name;
            }

            @Override
            public MethodVisitor visitMethod(
                int access,
                String name,
                String desc,
                String signature,
                String[] exceptions)
            {
                MethodVisitor mv = super.visitMethod(access,
                        name,
                        desc,
                        signature,
                        exceptions);
                if(!name.equals("test1")){
                	return mv;
                }
                System.out.println(name);
                mv = new MethodCallInlinerAdapter(owner,
                        access,
                        name,
                        desc,
                        mv,
                        inlineFrames)
                {

                    @Override
                    protected InlinedMethod mustInline(
                        String owner,
                        String name,
                        String desc){
                    	System.out.println(name +"22");
                        if (name.equals("test")) {
                            return new InlinedMethod(toInline, new Remapper() {
                            });
                        }
                        return null;
                    }

                };
                return mv;
            }
        };
        cn.accept(classVisitor);
        classReader.accept(classVisitor,ClassReader.EXPAND_FRAMES);
        byte[] classFile = classWriter.toByteArray();
        
        FileOutputStream os = new FileOutputStream(new File("D:\\tmp\\Bean1.class"));
        os.write(classFile);
        os.close();
    }

    public static void main(final String args[]) throws Exception {
        System.out.println("Inline m_, recompute all frames from scratch");
        test(false, false);
       // System.out.println("\nInline m_, inline frames");
        //test(true, true);

       // System.out.println("\nInline m, recompute all frames from scratch");
       // test(false, false);
        //System.out.println("\nInline m, inline frames");
        //test(false, true);
    }
}