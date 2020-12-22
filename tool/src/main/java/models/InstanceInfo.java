package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class InstanceInfo {
    //Stack<CallSite> constructSite = new Stack<>();
    String[] constructSite;
    Map<String,Integer> fieldPutCountMap = new HashMap<>();
    Map<String,Integer> fieldGetCountMap = new HashMap<>();
    public InstanceInfo(String[] constructSite,List<String> fieldNameList){

        this.constructSite = constructSite.clone();

        for(String fieldName : fieldNameList){
            this.fieldGetCountMap.put(fieldName,0);
            this.fieldPutCountMap.put(fieldName,0);
        }

    }
    public void incFieldPutCount(String fieldName){
        this.fieldPutCountMap.replace(fieldName,this.fieldPutCountMap.get(fieldName)+1);
    }
    public void incFieldGetCount(String fieldName){
        this.fieldGetCountMap.replace(fieldName,this.fieldGetCountMap.get(fieldName)+1);
    }
    public void show(){
        System.out.println("ConstructSite : ");
        for(Object callSite : this.constructSite) {
            System.out.println(callSite);
        }
        System.out.println("FieldInfo : ");
        this.fieldPutCountMap.forEach((key, value) -> {
            System.out.print("FieldName : " + key);
            System.out.println("  GetCount : " + this.fieldGetCountMap.get(key) + "  PutCount : "+value);

        });
    }
}
