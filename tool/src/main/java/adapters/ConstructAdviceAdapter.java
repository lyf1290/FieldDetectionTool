package adapters;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;

public class ConstructAdviceAdapter extends AdviceAdapter {
    protected String owner;
    protected ConstructAdviceAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }
    public void setMethodAccess(int access){
        this.methodAccess = access;
    }
    public void setMethodDesc(String desc){
        this.methodDesc = desc;
    }
    public void setMv(MethodVisitor mv){
        this.mv = mv;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }
    public String getOwner(){
        return this.owner;
    }
}
