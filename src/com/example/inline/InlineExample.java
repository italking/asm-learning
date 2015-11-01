package com.example.inline;

import java.io.PrintWriter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.MethodCallInlinerAdapter;
import org.objectweb.asm.commons.MethodCallInlinerAdapter.InlinedMethod;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.util.CheckClassAdapter;

/**
 * @author Eric Bruneton
 */
public class InlineExample extends ClassLoader implements Opcodes {

    // example method containing two calls to inline
    // including one in a try catch and one inside an expression
    public static int m(int i) {
        for (int j = 0; j < 5; ++j) {
            try {
                i = m_(i, j);
            } catch (RuntimeException e) {
                i = i + m_(j, i);
            }
        }
        return i;
    }

    // example method to be inlined,
    // containing branches and a try catch
    public static int m(int i, int j) {
        try {
            if (i > j) {
                return i / j;
            } else {
                throw new RuntimeException();
            }
        } catch (ArithmeticException e) {
            return i;
        }
    }

    // simpler example method to be inlined,
    // with branches but without try catch
    public static int m_(int i, int j) {
        if (i > j) {
            return i - 2;
        } else {
            throw new RuntimeException();
        }
    }

    // ASMified version of the above code
    static void accept(String className, boolean simple, ClassVisitor cv) {
        cv.visit(V1_6,
                ACC_PUBLIC + ACC_SUPER,
                className,
                null,
                "java/lang/Object",
                null);

        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC,
                "m",
                "(I)I",
                null,
                null);
        mv.visitCode();
        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/RuntimeException");
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ISTORE, 1);
        Label l3 = new Label();
        mv.visitLabel(l3);
        mv.visitFrame(Opcodes.F_APPEND,
                1,
                new Object[] { Opcodes.INTEGER },
                0,
                null);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitInsn(ICONST_5);
        Label l4 = new Label();
        mv.visitJumpInsn(IF_ICMPGE, l4);
        mv.visitLabel(l0);
        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitMethodInsn(INVOKESTATIC, className, "m", "(II)I");
        mv.visitVarInsn(ISTORE, 0);
        mv.visitLabel(l1);
        Label l5 = new Label();
        mv.visitJumpInsn(GOTO, l5);
        mv.visitLabel(l2);
        mv.visitFrame(Opcodes.F_SAME1,
                0,
                null,
                1,
                new Object[] { "java/lang/RuntimeException" });
        mv.visitVarInsn(ASTORE, 2);
        mv.visitVarInsn(ILOAD, 0);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitVarInsn(ILOAD, 0);
        mv.visitMethodInsn(INVOKESTATIC, className, "m", "(II)I");
        mv.visitInsn(IADD);
        mv.visitVarInsn(ISTORE, 0);
        mv.visitLabel(l5);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitIincInsn(1, 1);
        mv.visitJumpInsn(GOTO, l3);
        mv.visitLabel(l4);
        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        mv.visitVarInsn(ILOAD, 0);
        mv.visitInsn(IRETURN);
        mv.visitMaxs(3, 3);
        mv.visitEnd();

        if (!simple) {
            mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    "m",
                    "(II)I",
                    null,
                    null);
            mv.visitCode();
            l0 = new Label();
            l1 = new Label();
            l2 = new Label();
            mv.visitTryCatchBlock(l0, l1, l2, "java/lang/ArithmeticException");
            l3 = new Label();
            mv.visitTryCatchBlock(l3, l2, l2, "java/lang/ArithmeticException");
            mv.visitLabel(l0);
            mv.visitVarInsn(ILOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitJumpInsn(IF_ICMPLE, l3);
            mv.visitVarInsn(ILOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            mv.visitInsn(IDIV);
            mv.visitLabel(l1);
            mv.visitInsn(IRETURN);
            mv.visitLabel(l3);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL,
                    "java/lang/RuntimeException",
                    "<init>",
                    "()V");
            mv.visitInsn(ATHROW);
            mv.visitLabel(l2);
            mv.visitFrame(Opcodes.F_SAME1,
                    0,
                    null,
                    1,
                    new Object[] { "java/lang/ArithmeticException" });
            mv.visitVarInsn(ASTORE, 2);
            mv.visitVarInsn(ILOAD, 0);
            mv.visitInsn(IRETURN);
            mv.visitMaxs(2, 3);
            mv.visitEnd();
        } else {
            mv = cv.visitMethod(ACC_PUBLIC + ACC_STATIC,
                    "m",
                    "(II)I",
                    null,
                    null);
            mv.visitCode();
            mv.visitVarInsn(ILOAD, 0);
            mv.visitVarInsn(ILOAD, 1);
            l0 = new Label();
            mv.visitJumpInsn(IF_ICMPLE, l0);
            mv.visitVarInsn(ILOAD, 0);
            mv.visitInsn(ICONST_2);
            mv.visitInsn(ISUB);
            mv.visitInsn(IRETURN);
            mv.visitLabel(l0);
            mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            mv.visitTypeInsn(NEW, "java/lang/RuntimeException");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL,
                    "java/lang/RuntimeException",
                    "<init>",
                    "()V");
            mv.visitInsn(ATHROW);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }
        cv.visitEnd();
    }

    public static void test(
        final boolean simple,
        final boolean inlineFrames) throws Exception
    {
        InlineExample loader = new InlineExample();

        ClassWriter cw = new ClassWriter(0);
        accept("Reference", simple, cw);
        byte[] code = cw.toByteArray();
        Class< ? > referenceClass = loader.defineClass("Reference",
                code,
                0,
                code.length);
        System.out.println("Original class: result="
                + referenceClass.getMethods()[0].invoke(null, 10));

        cw = new ClassWriter(0);
        accept("Inlined", simple, cw);
        ClassNode cn = new ClassNode();
        ClassReader cr = new ClassReader(cw.toByteArray());
        cr.accept(cn, ClassReader.EXPAND_FRAMES);
        final MethodNode toInline =(MethodNode)cn.methods.get(1);

        cw = new ClassWriter(inlineFrames ? 0 : ClassWriter.COMPUTE_FRAMES);
        ClassVisitor cv = cw;
        // cv = new TraceClassVisitor(cv, new PrintWriter(System.out));
        cv = new ClassVisitor(Opcodes.ASM4, cv) {

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
                        String desc)
                    {
                        if (name.equals("m") && desc.equals("(II)I")) {
                            return new InlinedMethod(toInline, new Remapper() {
                            });
                        }
                        return null;
                    }

                };
                return mv;
            }
        };
        cn.accept(cv);
        code = cw.toByteArray();

        CheckClassAdapter.verify(new ClassReader(code),
                false,
                new PrintWriter(System.err));

        Class< ? > inlinedClass = loader.defineClass("Inlined",
                code,
                0,
                code.length);
        System.out.println("Inlined class: result="
                + inlinedClass.getMethods()[0].invoke(null, 10));
    }

    public static void main(final String args[]) throws Exception {
        System.out.println("Inline m_, recompute all frames from scratch");
        test(true, false);
        System.out.println("\nInline m_, inline frames");
        test(true, true);

       // System.out.println("\nInline m, recompute all frames from scratch");
       // test(false, false);
        //System.out.println("\nInline m, inline frames");
        //test(false, true);
    }
}