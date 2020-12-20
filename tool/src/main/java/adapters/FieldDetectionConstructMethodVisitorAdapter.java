package adapters;

import org.objectweb.asm.Opcodes;
import system.SystemConfig;

import java.util.List;

public class FieldDetectionConstructMethodVisitorAdapter extends MethodVisitorAdapter {
    //用户的配置
    protected SystemConfig systemConfig = SystemConfig.getInstance();

    public FieldDetectionConstructMethodVisitorAdapter(int api) {
        super(api);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if(this.mv != null){
            List<String> fieldNameList = SystemConfig.getInstance().getFieldNameList(this.getOwner());
            if(fieldNameList != null){
                int fieldNum = fieldNameList.size();
                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
                this.mv.visitIntInsn(Opcodes.BIPUSH, fieldNum);
                this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
                this.mv.visitFieldInsn(Opcodes.PUTFIELD, this.getOwner(), "putDirtyTag", "[I");
                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
                this.mv.visitIntInsn(Opcodes.BIPUSH, fieldNum);
                this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
                this.mv.visitFieldInsn(Opcodes.PUTFIELD, this.getOwner(), "getDirtyTag", "[I");

                this.mv.visitLdcInsn(this.owner);
                this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","newInstance","(Ljava/lang/String;)V",false);
                String parentClassName = SystemConfig.getInstance().getParentClassName(this.getOwner());
                if(parentClassName != null && SystemConfig.getInstance().isInterestringClass(parentClassName)){
                    this.mv.visitLdcInsn(parentClassName);
                    this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","descInstanceCount","(Ljava/lang/String;)V",false);
                }
            }
        }


    }

}
