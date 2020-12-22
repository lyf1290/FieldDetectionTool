package adapters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import system.SystemConfig;
import user.UserConfig;

/**
 * 下列字节码操作都是在指针压缩，指针占一个slot的前提下进行的。
 */
public class FieldDetectionMethodVisitorAdapter extends MethodVisitorAdapter {
    //用户的配置
    protected SystemConfig systemConfig = SystemConfig.getInstance();

    public FieldDetectionMethodVisitorAdapter(int api) {
        super(api);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (this.mv != null) {
            if(systemConfig.isInterestringClass(owner) && !name.startsWith("this$")){
                if(opcode == Opcodes.PUTFIELD){
                    if(descriptor.equals("J") || descriptor.equals("D")){
                        this.mv.visitInsn(Opcodes.DUP2_X1);
                        this.mv.visitInsn(Opcodes.POP2);
                        this.mv.visitInsn(Opcodes.DUP_X2);
                        this.mv.visitInsn(Opcodes.DUP_X2);
                        this.mv.visitInsn(Opcodes.POP);
                    }
                    else{
                        //this.mv.visitInsn(Opcodes.SWAP);
                        this.mv.visitInsn(Opcodes.DUP2);
                    }
                }
                else if(opcode == Opcodes.GETFIELD){
                    this.mv.visitInsn(Opcodes.DUP);
                    this.mv.visitLdcInsn(name);
                    this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,owner,"getField","(Ljava/lang/String;)V",false);

                }
            }
            this.mv.visitFieldInsn(opcode, owner, name, descriptor);

            if(systemConfig.isInterestringClass(owner) && !name.startsWith("this$")){
                if(opcode == Opcodes.PUTFIELD){
                    //对于putfield操作，
                    if(!descriptor.equals("J") && !descriptor.equals("D")){
                        this.mv.visitInsn(Opcodes.POP);
                    }
                    this.mv.visitLdcInsn(name);
                    this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,owner,"putField","(Ljava/lang/String;)V",false);
                }
            }


        }
    }


}
//Test Test

//int