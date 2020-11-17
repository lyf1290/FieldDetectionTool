package adapters;

import models.FieldModel;
import models.MethodModel;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.tree.InsnList;
import system.SystemConfig;
import templates.*;
import user.UserConfig;

import java.util.ArrayList;
import java.util.List;

import static org.objectweb.asm.Opcodes.ASM8;

/**
 * 目前提供的功能：
 * 1、Field和Method的新增
 * 2、构造函数指令的适配器constructAdviceAdapter（覆盖constructAdviceAdapter 并重写get 和set方法）
 * 3、所有函数指令的适配器allMethodVisitorAdapter (覆盖allMethodVisitorAdapter 并重写get 和set方法)
 */
public abstract class NextAdapter extends ClassVisitor {
    //当前操作的类的internal name
    protected String owner = "";
    //当前操作的类是否是关注类
    protected boolean interestring = false;
    //用户的配置
    protected SystemConfig systemConfig = SystemConfig.getInstance();
    //TODO：templates应该在哪里构建？
    //该Adapter要使用的所有模版
    protected List<Template> templates = new ArrayList<>();
    //用于更改构造函数的AdviceAdapter
    protected ConstructAdviceAdapter constructAdviceAdapter = null;
    //用于更改所有Method的Insn的MethodVisitAdapter
    protected MethodVisitorAdapter allMethodVisitorAdapter = null;



    protected ConstructAdviceAdapter getConstructAdviceAdapter(){
        return this.constructAdviceAdapter;
    }

    protected void setConstructAdviceAdapter(int access,String descriptor,MethodVisitor mv){
        this.constructAdviceAdapter.setMv(mv);
        this.constructAdviceAdapter.setMethodDesc(descriptor);
        this.constructAdviceAdapter.setMethodAccess(access);
        this.constructAdviceAdapter.setOwner(this.owner);
    }
    protected MethodVisitorAdapter getAllMethodVisitorAdapter(){
        return this.allMethodVisitorAdapter;
    }

    protected void setAllMethodVisitorAdapter(MethodVisitor mv){
        this.allMethodVisitorAdapter.setMv(mv);
    }

    /**
     * 构造函数
     * @param api  使用的ASM的版本号
     * @param classVisitor 任务托付的下一级对象
     */
    public NextAdapter(int api, ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    /**
     * 初始化adapter要用的所有模版
     */
    protected abstract void buildTemplates();

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces){
        /*
          检查这个类是否是关注类，保证后续操作只针对关注类
         */
        this.owner = name;
        interestring = systemConfig.isInterestringClass(name);
        //只关注类
        interestring = interestring && ((access & Opcodes.ACC_INTERFACE) == 0);
        cv.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

        //先放到链中的后执行
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        if(mv != null && interestring){
            if(name.equals("<init>")){
                if(this.getConstructAdviceAdapter() != null){
                    this.setConstructAdviceAdapter(access,descriptor,mv);
                    mv = this.getConstructAdviceAdapter();
                }
            }
            if(this.getAllMethodVisitorAdapter() != null){
                this.setAllMethodVisitorAdapter(mv);
                mv = this.getAllMethodVisitorAdapter();
            }

        }
        return mv;
    }

    /**
     * 添加field
     */
    private void AddFields(){
        for(Template template : templates){
            List<FieldModel> newFieldModels = template.getNewFieldInfos(this.owner);
            for(FieldModel newFieldModel : newFieldModels){
                cv.visitField(newFieldModel.getAccess(), newFieldModel.getName(),
                        newFieldModel.getDescriptor(), newFieldModel.getSignature(), newFieldModel.getValue());
            }
        }
    }

    /**
     * 添加method
     */
    private void AddMethods(){
        for(Template template : templates){
            List<MethodModel> newMethodModels = template.getNewMethodInfos(this.owner);
            for(MethodModel newMethodModel : newMethodModels){
                MethodVisitor mv = cv.visitMethod(newMethodModel.getAccess(), newMethodModel.getName(),
                        newMethodModel.getDescriptor(), newMethodModel.getSignature(), newMethodModel.getExceptions());
                AddInsns(mv,newMethodModel.getInsnList());
            }
        }
    }

    /**
     * 完善方法的方法体，不带注解 TODO 从insnList中导出Max信息
     * @param mv 被添加方法的methodvisitor
     * @param insnList  方法体内的指令
     */
    private void AddInsns(MethodVisitor mv, InsnList insnList){
        mv.visitCode();
        insnList.accept(mv);
        mv.visitMaxs(4,2);
        mv.visitEnd();
    }

    @Override
    public void visitEnd(){
        //只针对关注类进行Field和Method的添加操作
        if(this.interestring){
            AddFields();
            AddMethods();
        }

    }
}
