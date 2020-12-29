package tools;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import models.CallSite;
import models.ClassInfo;
import system.SystemConfig;
import user.UserConfig;
import java.sql.Time;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

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
    public static void putField(String className,String fieldName,int instanceId){
        classInfoMap.get(className).putField(fieldName,instanceId);
    }

    public static void getField(String className,String fieldName,int instanceId){
        classInfoMap.get(className).getField(fieldName,instanceId);
    }

    public static void show(String agentArgs) {
        try {

            String[] ss = agentArgs.split("/");
            ss = java.util.Arrays.copyOf(ss, ss.length - 1);

            Map<String, ClassInfo> map = Maps.filterValues(classInfoMap, r -> r.getInstanceCount() > 0);
            if (map.size()>0){
                String jsonfiledir=String.join("/", ss) + "/testoutput";
                File fp = new File(jsonfiledir);  
                // 创建目录  
                if (!fp.exists()) {  
                    fp.mkdirs();// 目录不存在的情况下，创建目录。  
                }
                String jsonfilepath=jsonfiledir + "/testoutput."+System.currentTimeMillis()+".json";
                FileOutputStream file=new FileOutputStream(jsonfilepath);

                if(SystemConfig.getInstance().getMode().equals("FieldDetection")){
                    ObjectMapper objectMapper = new ObjectMapper();
                    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("instanceInfoMap","fieldNameList");
                    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
                    objectMapper.writer(filters).writeValue(file, map);
                    System.out.println("json file path :"+jsonfilepath);
                    file.close();
                    // 然后来计算，画柱状图与饼图
                    // System.out.println(map.keySet());
                    List<Object> charts = XchartDraw.drawBarandPieChart(map);
                    String jsonfilename = jsonfilepath.split("/")[jsonfilepath.split("/").length - 1];
                    jsonfilename = jsonfiledir + "/" + jsonfilename;
                    List<CategoryChart> barcharts = (List<CategoryChart>) charts.get(0);
                    List<PieChart> piecharts = (List<PieChart>) charts.get(1);
                    for (int j = 0; j < barcharts.size(); j++) {
                        // System.out.println(charts.get(j).getSeriesMap());
                        BitmapEncoder.saveBitmapWithDPI(barcharts.get(j), jsonfilename + j, BitmapFormat.PNG, 400);
                    }
                    for (int j = 0; j < piecharts.size(); j++) {
                        // System.out.println(charts.get(j).getSeriesMap());
                        BitmapEncoder.saveBitmapWithDPI(piecharts.get(j), jsonfilename + (j+barcharts.size()), BitmapFormat.PNG, 400);
                    }
                }else if(SystemConfig.getInstance().getMode().equals("ConstructSite")){
                    ObjectMapper objectMapper = new ObjectMapper();
                    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("fieldInfoMap","fieldNameList");
                    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);
                    objectMapper.writer(filters).writeValue(file, map);
                    System.out.println("json file path :"+jsonfilepath);
                    file.close();
                }


            }
           
            // map.forEach((key, value) -> {
            //     System.out.print("ClassName : " + key + "\t");
            //     value.show();
            // });

            //out.close();
            

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public static void showConstructSite() {

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
        } finally {
            lock.unlock();
        }
        if (instanceId == -1) {
            System.out.println("getInstanceId ERROR : instanceId = -1");
        }
        return instanceId;
    }
    public static String[] getStack(){
        String[] constructSite;

        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

        constructSite = new String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.length-2)];
        for(int i = 2,j = 0; j < constructSite.length; ++i,++j){
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

    public static void popStack() {
        Long threadId = Thread.currentThread().getId();
        if (callStackMap.containsKey(threadId)) {
            // 该线程已经有一个stack了
            Stack<CallSite> callStack = callStackMap.get(threadId);
            callStack.pop();

        } else {
            System.out.println("popStack ERROR : threadID  = " + Thread.currentThread().getId());

        }
    }

    */
}
