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
            if(this.systemConfig.isInterestringClass(this.getOwner())){
                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
                this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","getStack","()[Ljava/lang/String;",false);
                this.mv.visitFieldInsn(Opcodes.PUTFIELD, this.getOwner(), "_constructSite$", "[Ljava/lang/String;");

            }

        }

    }


}