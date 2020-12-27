package models;

import system.SystemConfig;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ClassInfo {
    // TODO 如果instance数量很多的话会超过int
    private int instanceCount = 0;
    @JsonIgnore
    private final Map<String, FieldInfo> fieldInfoMap = new HashMap<>();

    private final Map<Integer,InstanceInfo> instanceInfoMap = new HashMap<>();
    @JsonIgnore
    private final List<String> fieldNameList = new ArrayList<>();
    public ClassInfo() {
        this.instanceCount = 0;

    }
    public ClassInfo(List<String> fieldNames) {
        this.instanceCount = 0;

        this.fieldNameList.addAll(fieldNames);
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
    public void newInstance(String[] constructSite){
        this.instanceCount++;
        this.instanceInfoMap.put(instanceCount,new InstanceInfo(constructSite,this.fieldNameList));
    }
    public void descInstanceCount(){
        instanceCount--;
    }
    public void getField(String fieldName,boolean first){
        this.fieldInfoMap.get(fieldName).getField(first);
    }
    public void putField(String fieldName,boolean first){
        this.fieldInfoMap.get(fieldName).putField(first);
    }
    public void getField(String fieldName,int instanceId){
        this.instanceInfoMap.get(instanceId).incFieldGetCount(fieldName);

    }
    public void putField(String fieldName,int instanceId){
        this.instanceInfoMap.get(instanceId).incFieldPutCount(fieldName);
    }
    public void show(){
        System.out.println("InstanceCount : " + this.instanceCount);
        this.fieldInfoMap.forEach((key, value) -> {
            System.out.println("FieldName : " + key);
            value.show();
        });
    }
    public void showConstructSite(){

        this.instanceInfoMap.forEach((key, value) -> {
            System.out.println("InstanceId : " + key);
            value.show();

        });
    }

    public Map<Integer, InstanceInfo> getInstanceInfoMap() {
        return instanceInfoMap;
    }
}
