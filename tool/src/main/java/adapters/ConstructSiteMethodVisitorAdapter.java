package adapters;

import org.objectweb.asm.Opcodes;
import system.SystemConfig;

import java.util.List;

public class ConstructSiteMethodVisitorAdapter extends MethodVisitorAdapter {
    //用户的配置
    protected SystemConfig systemConfig = SystemConfig.getInstance();

    public ConstructSiteMethodVisitorAdapter(int api) {
        super(api);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if (this.mv != null) {
            //this$是内部类中外部类域的名字开头
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
                    if(this.systemConfig.getOverwriteFieldList(owner).contains(name)) {
                        this.mv.visitMethodInsn(Opcodes.INVOKESPECIAL,owner,"getField","(Ljava/lang/String;)V",false);
                    }
                    else{
                        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,owner,"getField","(Ljava/lang/String;)V",false);
                    }

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
                    if(this.systemConfig.getOverwriteFieldList(owner).contains(name)) {
                        this.mv.visitMethodInsn(Opcodes.INVOKESPECIAL,owner,"putField","(Ljava/lang/String;)V",false);
                    }
                    else{
                        this.mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,owner,"putField","(Ljava/lang/String;)V",false);
                    }
                }
            }
        }
    }


    @Override
    public void visitCode() {
        super.visitCode();
        //插入栈帧
        if(this.mv != null){
            //解决顺序问题
            if(!this.getMethodName().equals("<init>")){
                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
                this.mv.visitLdcInsn(this.getOwner());
                this.mv.visitLdcInsn(this.getMethodName());
                this.mv.visitLdcInsn(this.getMethodDesc());
                this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","pushStack","(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)[Ljava/lang/String;",false);

            }

//
//            List<String> fieldNameList = SystemConfig.getInstance().getFieldNameList(this.getOwner());
//            if(fieldNameList != null){
//                int fieldNum = fieldNameList.size();
//                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
//                this.mv.visitIntInsn(Opcodes.BIPUSH, fieldNum);
//                this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
//                this.mv.visitFieldInsn(Opcodes.PUTFIELD, this.getOwner(), "putDirtyTag", "[I");
//                this.mv.visitVarInsn(Opcodes.ALOAD, 0);
//                this.mv.visitIntInsn(Opcodes.BIPUSH, fieldNum);
//                this.mv.visitIntInsn(Opcodes.NEWARRAY, Opcodes.T_INT);
//                this.mv.visitFieldInsn(Opcodes.PUTFIELD, this.getOwner(), "getDirtyTag", "[I");
//
//                this.mv.visitLdcInsn(this.owner);
//                this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","newInstance","(Ljava/lang/String;)V",false);
//                String parentClassName = SystemConfig.getInstance().getParentClassName(this.getOwner());
//                if(parentClassName != null && SystemConfig.getInstance().isInterestringClass(parentClassName)){
//                    this.mv.visitLdcInsn(parentClassName);
//                    this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","descInstanceCount","(Ljava/lang/String;)V",false);
//                }
//            }
        }


    }

    @Override
    public void visitInsn(int opcode){
        //弹出栈帧
        if(this.mv != null){
            if((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || (opcode == Opcodes.ATHROW)){
                this.mv.visitMethodInsn(Opcodes.INVOKESTATIC,"tools/InfoCollector","popStack","()V",false);
            }
            this.mv.visitInsn(opcode);
        }

    }
}
