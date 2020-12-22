package adapters;

import org.objectweb.asm.Label;
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
    public void visitCode(){
        super.visitCode();
        if(this.mv != null){
            //增加实例计数
            this.mv.visitLdcInsn(this.owner);
            this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","newInstance","(Ljava/lang/String;)V",false);

        }

    }





}
