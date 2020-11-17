package system;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import tools.ByteCodeTool;
import user.UserConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemConfig {
    private Map<String, List<String>> fieldNameMap = new HashMap<>();
    private Map<String,String> parentClassNameMap = new HashMap<>();
    private Map<String,String> outerClassNameMap = new HashMap<>();

    private final static SystemConfig SYSTEM_CONFIG = new SystemConfig();
    public static SystemConfig getInstance(){
        return SYSTEM_CONFIG;
    }

    //用户关注的类在JVM中的internal name  例如com/Student
    private List<String> interestringClassNameList = new ArrayList<>();
    //用户关注类在用户系统中的绝对路径，
    private List<String> interestringClassFilesPathList;


    private SystemConfig(){
        this.interestringClassFilesPathList = UserConfig.getInstance().getInterestringClassFilesPathList();
        this.init();
    }

    private void init(){
        this.fieldNameMap.clear();
        this.parentClassNameMap.clear();
        this.interestringClassNameList.clear();
        Map<String,Boolean> extendFlagMap = new HashMap<>();
        //通过用户关注的类的路径，获得该class文件的字节码，解析获得internal name 和 parentName以及fields
        for(String interestringClassFilesPath : this.interestringClassFilesPathList){
            byte[] interestringClassByteCodes = ByteCodeTool.input(interestringClassFilesPath);
            ClassReader cr = new ClassReader(interestringClassByteCodes);
            ClassNode sourceNode = new ClassNode();
            cr.accept(sourceNode,0);
            //将关注类的名字记录下来
            this.interestringClassNameList.add(sourceNode.name);
            extendFlagMap.put(sourceNode.name,false);
            //将关注类的父类的名字也记录到map中
            this.parentClassNameMap.put(sourceNode.name,sourceNode.superName);
            //将关注类的outer类的名字记录到map中
            this.outerClassNameMap.put(sourceNode.name,sourceNode.outerClass);
            List<String> fieldNames = new ArrayList<>();
            for(FieldNode fieldNode : sourceNode.fields){
                fieldNames.add(fieldNode.name);
            }
            fieldNameMap.put(sourceNode.name,fieldNames);
        }
        for (Map.Entry<String, String> entry : this.parentClassNameMap.entrySet()) {
            if(!extendFlagMap.get(entry.getKey())){
                extendFieldMap(extendFlagMap,entry.getKey());
            }
        }

    }

    /**
     * 如果一个关注类的父类也是关注类，那么递归的将关注类父类的field添加到子类中
     * @param extendFlagMap 记录该类是否被展开的map
     * @param className 要展开的类的类名
     */
    public void extendFieldMap(Map<String,Boolean> extendFlagMap,String className){
        //将父类的field添加到子类中

        String parentClassName = parentClassNameMap.get(className);
        if(parentClassName != null && this.isInterestringClass(parentClassName)){
            if(!extendFlagMap.get(parentClassName)){
                //如果展开过的话直接加，不用再递归
                extendFieldMap(extendFlagMap,parentClassName);
            }
            List<String> parentFieldNameList = this.fieldNameMap.get(parentClassName);
            List<String> fieldNameList = this.fieldNameMap.get(className);
            for(String parentFieldName : parentFieldNameList){
                if(!fieldNameList.contains(parentFieldName)){
                    fieldNameList.add(parentFieldName);
                }
            }
        }
        extendFlagMap.put(className,true);
    }

    public List<String> getFieldNameList(String className){
        return this.fieldNameMap.get(className);
    }
    public String getParentClassName(String className){
        return this.parentClassNameMap.get(className);
    }
    public String getOuterClassName(String className){
        return this.outerClassNameMap.get(className);
    }
    public boolean isInterestringClass(String className){
        for(String interestringClassName : interestringClassNameList){
            if(interestringClassName.equals(className)){
                return true;
            }
        }
        return false;
    }
    public void setInterestringClassName(List<String> interestringClassNameList){
        this.interestringClassNameList = interestringClassNameList;
    }
    public List<String> getInterestringClassNameList(){
        return this.interestringClassNameList;
    }
    public List<String> getInterestringClassFilesPathList(){
        return this.interestringClassFilesPathList;
    }

    public static void main(String args[]){


    }
}