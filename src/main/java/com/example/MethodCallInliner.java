package com.example;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.LocalVariablesSorter;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingMethodAdapter;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class MethodCallInliner extends LocalVariablesSorter {
    private final String oldClass;
    private final String newClass;
    private final MethodNode mn;
    private List blocks = new ArrayList();
    private boolean inlining;

    private MethodCallInliner(int access,
                              String desc, MethodVisitor mv, MethodNode mn,
                              String oldClass, String newClass) {
        super(access, desc, mv);
        this.oldClass = oldClass;
        this.newClass = newClass;
        this.mn = mn;
    }

    public void visitMethodInsn(int opcode,
                                String owner, String name, String desc) {
        if (!canBeInlined(owner, name, desc)) {
            mv.visitMethodInsn(opcode, owner, name, desc);
            return;
        }
        Map map = Collections.singletonMap(
                oldClass, newClass);
        Remapper remapper = new SimpleRemapper(map);
        Label end = new Label();
        inlining = true;
        mn.instructions.resetLabels();
        mn.accept(new InliningAdapter(this,
                opcode == Opcodes.INVOKESTATIC ?
                        Opcodes.ACC_STATIC : 0,
                desc, remapper, end));
        inlining = false;
        super.visitLabel(end);
    }

    private boolean canBeInlined(final String owner, final String name, final String desc) {
        return "org/codehaus/groovy/runtime/DefaultGroovyMethods".equals(owner);
    }

    public void visitTryCatchBlock(Label start,
                                   Label end, Label handler, String type) {
        if (!inlining) {
            blocks.add(new CatchBlock(start, end,
                    handler, type));
        } else {
            super.visitTryCatchBlock(start, end,
                    handler, type);
        }
    }

    public void visitMaxs(int stack, int locals) {
        Iterator it = blocks.iterator();
        while (it.hasNext()) {
            CatchBlock b = (CatchBlock) it.next();
            super.visitTryCatchBlock(b.start, b.end,
                    b.handler, b.type);
        }
        super.visitMaxs(stack, locals);
    }

    public static class InliningAdapter
            extends RemappingMethodAdapter {
        private final LocalVariablesSorter lvs;
        private final Label end;

        public InliningAdapter(LocalVariablesSorter mv,
                               int acc, String desc,
                               Remapper remapper,
                               Label end) {
            super(acc, desc, mv, remapper);
            this.lvs = mv;
            this.end = end;
            int off = (acc & Opcodes.ACC_STATIC) != 0 ?
                    0 : 1;
            Type[] args = Type.getArgumentTypes(desc);
            for (int i = args.length - 1; i >= 0; i--) {
                super.visitVarInsn(args[i].getOpcode(
                        Opcodes.ISTORE), i + off);
            }
            if (off > 0) {
                super.visitVarInsn(Opcodes.ASTORE, 0);
            }
        }

        public void visitInsn(int opcode) {
            if (opcode == Opcodes.RETURN) {
                super.visitJumpInsn(Opcodes.GOTO, end);
            } else {
                super.visitInsn(opcode);
            }
        }

        public void visitMaxs(int stack, int locals) {
        }

        protected int newLocalMapping(Type type) {
            return lvs.newLocal(type);
        }
    }

    private static class CatchBlock {
        private final Label start;
        private final Label end;
        private final Label handler;
        private final String type;

        private CatchBlock(final Label end, final Label start, final Label handler, final String type) {
            this.end = end;
            this.start = start;
            this.handler = handler;
            this.type = type;
        }
    }


}