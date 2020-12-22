package adapters;

import org.objectweb.asm.MethodVisitor;
import user.UserConfig;


/*
该类纯粹用于实例化MethodVisit（抽象类）
 */
public class MethodVisitorAdapter extends MethodVisitor {
    protected String owner;
    protected String methodName;
    protected String methodDesc;
    protected boolean isInnerClass;
    public MethodVisitorAdapter(int api) {
        super(api);
    }
    protected void setMv(MethodVisitor mv){
        this.mv = mv;
    }
    public void setOwner(String owner){
        this.owner = owner;
    }
    public String getOwner(){
        return this.owner;
    }
    public void setMethodName(String methodName){
        this.methodName = methodName;
    }
    public void setMethodDesc(String methodDesc){
        this.methodDesc = methodDesc;
    }
    public String getMethodName(){
        return this.methodName;
    }
    public String getMethodDesc(){
        return this.methodDesc;
    }

    public boolean isInnerClass() {
        return isInnerClass;
    }

    public void setInnerClass(boolean innerClass) {
        isInnerClass = innerClass;
    }
}
