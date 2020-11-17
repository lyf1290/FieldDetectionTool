package models;

import org.objectweb.asm.tree.InsnList;

public class MethodModel {
    //访问权限
    int access;
    //函数名
    String name;
    //函数的返回值和参数
    String descriptor;
    //函数的签名（比如范型）
    String signature;
    //异常名称列表
    String[] exceptions;
    //方法的具体指令列表
    InsnList insnList;


    MethodModel(){

    }
    public MethodModel(int access, String name, String descriptor, String signature, String[] exceptions, InsnList insnList){
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.exceptions = exceptions;
        this.insnList = insnList;
    }

    public int getAccess(){
        return this.access;
    }
    public String getName(){
        return this.name;
    }
    public String getDescriptor(){
        return this.descriptor;
    }
    public String getSignature(){
        return this.signature;
    }
    public String[] getExceptions() {
        return exceptions;
    }
    public InsnList getInsnList() {
        return insnList;
    }
}
