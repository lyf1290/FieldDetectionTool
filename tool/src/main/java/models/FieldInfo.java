package models;

import java.util.List;


public class FieldInfo {
    private int instancePutCount = 0;
    private int putCount = 0;
    private int instanceGetCount = 0;
    private int getCount = 0;

    public void getField(boolean first){
        if(first){
            this.instanceGetCount++;
        }
        getCount++;
    }
    public void putField(boolean first){
        if(first){
            this.instancePutCount++;
        }
        putCount++;
    }
    public int getInstancePutCount(){
        return this.instancePutCount;
    }
    public int getPutCount(){
        return this.putCount;
    }
    public int getInstanceGetCount(){
        return this.instanceGetCount;
    }
    public int getGetCount(){
        return this.getCount;
    }

    void show(){
        System.out.println("\tinstancePutCount : " + this.instancePutCount);
        System.out.println("\tputCount : " + this.putCount);
        System.out.println("\tinstanceGetCount : " + this.instanceGetCount);
        System.out.println("\tGetCount : " + this.getCount);
    }
}
