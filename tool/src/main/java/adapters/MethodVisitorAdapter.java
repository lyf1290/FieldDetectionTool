package adapters;

import org.objectweb.asm.MethodVisitor;
import user.UserConfig;


/*
该类纯粹用于实例化MethodVisit（抽象类）
 */
public class MethodVisitorAdapter extends MethodVisitor {
    public MethodVisitorAdapter(int api) {
        super(api);
    }
    protected void setMv(MethodVisitor mv){
        this.mv = mv;
    }

}
