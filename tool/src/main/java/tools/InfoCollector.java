package tools;

import models.CallSite;
import models.ClassInfo;
import system.SystemConfig;
import user.UserConfig;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.common.collect.Maps;

/**
 * 暂时只考虑单线程
 */
public class InfoCollector {

    private static Map<String, ClassInfo> classInfoMap = new HashMap<>();
    //private static Map<Long, Stack<CallSite>> callStackMap = new HashMap<>();
    private static Map<String,Lock> lockMap = new HashMap<>();

    static {

        classInfoMap.clear();
        List<String> classNameList = SystemConfig.getInstance().getInterestringClassNameList();
        for (String className : classNameList) {
            classInfoMap.put(className, new ClassInfo(SystemConfig.getInstance().getFieldNameList(className)));
        }

    }

    public static void setClassInfoMap(Map<String, ClassInfo> classInfoMap) {
        InfoCollector.classInfoMap = classInfoMap;
    }

    public static Map<String, ClassInfo> getClassInfoMap() {
        return InfoCollector.classInfoMap;
    }

    public static void newInstance(String className) {
        classInfoMap.get(className).newInstance();
    }

    public static void descInstanceCount(String className) {
        classInfoMap.get(className).descInstanceCount();
    }

    public static void putField(String className, String fieldName, boolean first) {
        classInfoMap.get(className).putField(fieldName, first);
    }

    public static void getField(String className, String fieldName, boolean first) {
        classInfoMap.get(className).getField(fieldName, first);
    }
    public static void putField(String className,String fieldName,boolean first,int instanceId){
        classInfoMap.get(className).putField(fieldName,instanceId);
    }

    public static void getField(String className,String fieldName,boolean first,int instanceId){
        classInfoMap.get(className).getField(fieldName,instanceId);
    }
    public static void show(){
        classInfoMap.forEach((key, value) -> {
            System.out.print("ClassName : " + key + "\t");
            value.show();
        });
    }
    public static void showConstructSite(){
        System.out.println();
        classInfoMap.forEach((key, value) -> {
            System.out.println("ClassName : " + key + "\t");
            value.showConstructSite();
            System.out.println();
        });
    }
    public static  int getInstanceId(String className,String[] constructSite){
        if(!lockMap.containsKey(className)){
            lockMap.put(className,new ReentrantLock());
        }
        Lock lock = lockMap.get(className);
        int instanceId = -1;
        lock.lock();
        try{
            classInfoMap.get(className).newInstance(constructSite);
            instanceId = classInfoMap.get(className).getInstanceCount();
        }finally {
            lock.unlock();
        }
        if(instanceId == -1){
            System.out.println("getInstanceId ERROR : instanceId = -1");
        }
        return instanceId;
    }
    public static String[] getStack(){
        String[] constructSite;

        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();
        constructSite = new String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.length-2)];
        for(int i = callStack.length - 1,j = 0; i >= callStack.length - constructSite.length; --i,j++){
            constructSite[j] = callStack[i].toString();
        }

        return constructSite;
    }

    /*
    public static String[] pushStack(String className,String methodName,String methodDesc){
        Long threadId = Thread.currentThread().getId();
        String[] constructSite;
        if(callStackMap.containsKey(threadId)){
            //该线程已经有一个stack了
            Stack<CallSite> callStack =  callStackMap.get(threadId);
            callStack.push(new CallSite(className,methodName,methodDesc));

            constructSite = new String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.size())];
            for(int i = callStack.size() - 1,j = 0; i >= callStack.size() - constructSite.length; --i,j++){
                constructSite[j] = callStack.elementAt(i).toString();

            }

        }
        else{
            Stack<CallSite> callStack =  new Stack<>();
            callStack.push(new CallSite(className,methodName,methodDesc));
            callStackMap.put(threadId,callStack);
            constructSite = new String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.size())];
            for(int i = callStack.size() - 1,j = 0; i >= callStack.size() - constructSite.length; --i,j++){
                constructSite[j] = callStack.elementAt(i).toString();

            }


        }
        return constructSite;
    }
    public static void popStack(){
        Long threadId = Thread.currentThread().getId();
        if(callStackMap.containsKey(threadId)){
            //该线程已经有一个stack了
            Stack<CallSite> callStack =  callStackMap.get(threadId);
            callStack.pop();

        }
        else{
            System.out.println("popStack ERROR : threadID  = "+Thread.currentThread().getId());

        }
    }

    */
}
