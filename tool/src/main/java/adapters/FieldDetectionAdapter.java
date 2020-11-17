package adapters;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import templates.*;
import user.UserConfig;

import static org.objectweb.asm.Opcodes.ASM8;

/*
* 该类是和ClassReader类直接交互的Adapter
* 功能：
* （1）插入Field和Method
* （2）将特定方法转交给AdviceAdapter和MethodAdapter
* */

public class FieldDetectionAdapter extends NextAdapter  {
    //往方法开头结尾插入指令的Adatper（在构造函数中父类的区域的初始化指令后是开头）
    protected ConstructAdviceAdapter constructAdviceAdapter = new FieldDetectionConstructAdviceAdapter(ASM8,new MethodVisitorAdapter(ASM8),0,"<init>","()V");
    //往方法的特定指令位置插入指令的Adatper
    protected MethodVisitorAdapter allMethodVisitorAdapter = new FieldDetectionMethodVisitorAdapter(ASM8);

    /** 必须写
     * 覆盖NextAdapter中的获取constructAdviceAdapter的方法，让NextAdpter中能够使用属于当前Adapter的ConstructAdviceAdapter
     * @return 属于当前Adapter的ConstructAdviceAdapter
     */
    @Override
    protected ConstructAdviceAdapter getConstructAdviceAdapter(){
        return this.constructAdviceAdapter;
    }
    /** 必须写
     * 覆盖NextAdapter中的设置constructAdviceAdapter的方法，让NextAdpter中能够使用属于当前Adapter的ConstructAdviceAdapter
     * @return 属于当前Adapter的ConstructAdviceAdapter
     */
    @Override
    protected void setConstructAdviceAdapter(int access,String descriptor,MethodVisitor mv){
        this.constructAdviceAdapter.setMv(mv);
        this.constructAdviceAdapter.setMethodDesc(descriptor);
        this.constructAdviceAdapter.setMethodAccess(access);
        this.constructAdviceAdapter.setOwner(this.owner);
    }
    @Override
    protected MethodVisitorAdapter getAllMethodVisitorAdapter(){
        return this.allMethodVisitorAdapter;
    }
    @Override
    protected void setAllMethodVisitorAdapter(MethodVisitor mv){
        this.allMethodVisitorAdapter.setMv(mv);
    }

    /**
     * 构造函数
     * @param api  使用的ASM的版本号
     * @param classVisitor 任务托付的下一级对象
     */
    public FieldDetectionAdapter(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
        this.buildTemplates();

    }


    /**
     * 初始化adapter要用的所有模版
     */
    protected void buildTemplates(){
        //FieldDetciton模版
        FieldDetectionTemplate fieldDetectionTemplate = new FieldDetectionTemplate();
        this.templates.add(fieldDetectionTemplate);
    }


}
