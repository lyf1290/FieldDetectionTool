package models;

import system.SystemConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassInfo {
    //TODO 如果instance数量很多的话会超过int
    private int instanceCount = 0;
    private Map<String,FieldInfo> fieldInfoMap;

    public ClassInfo() {
        this.instanceCount = 0;
        this.fieldInfoMap = new HashMap<>();
        this.fieldInfoMap.clear();
    }
    public ClassInfo(List<String> fieldNames) {
        this.instanceCount = 0;
        this.fieldInfoMap = new HashMap<>();
        this.fieldInfoMap.clear();
        for(String fieldName : fieldNames){
            fieldInfoMap.put(fieldName,new FieldInfo());
        }
    }
    public void initFieldInfoMap(List<String> fieldNames){
        for(String fieldName : fieldNames){
            fieldInfoMap.put(fieldName,new FieldInfo());
        }
    }
    public Map<String,FieldInfo> getFieldInfoMap(){
        return this.fieldInfoMap;
    }
    public int getInstanceCount(){
        return this.instanceCount;
    }
    public void newInstance(){
        instanceCount++;
    }
    public void descInstanceCount(){
        instanceCount--;
    }
    public void getField(String fieldName,boolean first){
        fieldInfoMap.get(fieldName).getField(first);
    }
    public void putField(String fieldName,boolean first){
        fieldInfoMap.get(fieldName).putField(first);
    }

    public void show(){
        System.out.println("InstanceCount : " + this.instanceCount);
        fieldInfoMap.forEach((key, value) -> {
            System.out.println("FieldName : " + key);
            value.show();
        });
    }
}
