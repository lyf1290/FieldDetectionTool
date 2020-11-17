package models;

import org.objectweb.asm.tree.ClassNode;

public class FieldModel {
    //访问权限
    int access;
    //字段名
    String name;
    //字段的类型
    String descriptor;
    //字段的签名（如范型等信息），没有签名为null
    String signature;
    //static final 字段等值，如果不是的话为null
    Object value;

    public FieldModel(){

    }

    public FieldModel(int access, String name, String descriptor, String signature, Object value){
        this.access = access;
        this.name = name;
        this.descriptor = descriptor;
        this.signature = signature;
        this.value = value;
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
    public Object getValue(){
        return this.value;
    }
}
