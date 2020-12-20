package adapters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import system.SystemConfig;
import tools.InfoCollector;
import user.UserConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FieldDetectionConstructAdviceAdapter extends ConstructAdviceAdapter {

    @Override
    protected void onMethodEnter() {

        if(this.mv != null){
//            List<String> fieldNameList = SystemConfig.getInstance().getFieldNameList(this.getOwner());
//            if(fieldNameList != null){
//                int fieldNum = fieldNameList.size();
//                this.mv.visitVarInsn(ALOAD, 0);
//                this.mv.visitIntInsn(BIPUSH, fieldNum);
//                this.mv.visitIntInsn(NEWARRAY, T_INT);
//                this.mv.visitFieldInsn(PUTFIELD, this.getOwner(), "putDirtyTag", "[I");
//                this.mv.visitVarInsn(ALOAD, 0);
//                this.mv.visitIntInsn(BIPUSH, fieldNum);
//                this.mv.visitIntInsn(NEWARRAY, T_INT);
//                this.mv.visitFieldInsn(PUTFIELD, this.getOwner(), "getDirtyTag", "[I");
//            }
            this.mv.visitLdcInsn(this.owner);
            this.mv.visitMethodInsn(INVOKESTATIC,"tools/InfoCollector","newInstance","(Ljava/lang/String;)V",false);
            String parentClassName = SystemConfig.getInstance().getParentClassName(this.getOwner());
            if(parentClassName != null && SystemConfig.getInstance().isInterestringClass(parentClassName)){
                this.mv.visitLdcInsn(parentClassName);
                this.mv.visitMethodInsn(INVOKESTATIC,"tools/InfoCollector","descInstanceCount","(Ljava/lang/String;)V",false);
            }
        }

    }

    @Override
    public void visitCode() {
        super.visitCode();
        if(this.mv != null){
            List<String> fieldNameList = SystemConfig.getInstance().getFieldNameList(this.getOwner());
            if(fieldNameList != null){
                int fieldNum = fieldNameList.size();
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitIntInsn(BIPUSH, fieldNum);
                this.mv.visitIntInsn(NEWARRAY, T_INT);
                this.mv.visitFieldInsn(PUTFIELD, this.getOwner(), "putDirtyTag", "[I");
                this.mv.visitVarInsn(ALOAD, 0);
                this.mv.visitIntInsn(BIPUSH, fieldNum);
                this.mv.visitIntInsn(NEWARRAY, T_INT);
                this.mv.visitFieldInsn(PUTFIELD, this.getOwner(), "getDirtyTag", "[I");

            }
        }


    }
    
//    @Override
//    protected void onMethodExit(int opcode) {
//        if(mv != null){
//            mv.visitFieldInsn(GETSTATIC, "Test", "timer", "J"); mv.visitMethodInsn(INVOKESTATIC, "java/lang/System",
//                    "currentTimeMillis", "()J"); mv.visitInsn(LADD);
//            mv.visitFieldInsn(PUTSTATIC, "Test", "timer", "J");
//        }
//
//    }
//    @Override public void visitMaxs(int maxStack, int maxLocals) {
//        if(mv != null){
//            mv.visitMaxs(maxStack + 4, maxLocals);
//        }
//    }
    protected FieldDetectionConstructAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }
}
