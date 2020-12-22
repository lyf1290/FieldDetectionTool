package adapters;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.VarInsnNode;
import system.SystemConfig;

import java.util.List;

public class ConstructSiteConstructMethodVisitorAdapter extends MethodVisitorAdapter {
    //用户的配置
    protected SystemConfig systemConfig = SystemConfig.getInstance();

    public ConstructSiteConstructMethodVisitorAdapter(int api) {
        super(api);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        if (this.mv != null) {
            this.mv.visitLdcInsn(this.getOwner());
            this.mv.visitLdcInsn(this.getMethodName());
            this.mv.visitLdcInsn(this.getMethodDesc());
            this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","pushStack","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",false);


        }

    }


}