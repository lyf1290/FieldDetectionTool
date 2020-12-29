package tools;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import models.CallSite;
import models.ClassInfo;
import system.SystemConfig;
import user.UserConfig;
import java.sql.Time;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
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
    // private static Map<Long, Stack<CallSite>> callStackMap = new HashMap<>();
    private static Map<String, Lock> lockMap = new HashMap<>();

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

    public static void putField(String className, String fieldName, int instanceId) {
        classInfoMap.get(className).putField(fieldName, instanceId);
    }

    public static void getField(String className, String fieldName, int instanceId) {
        classInfoMap.get(className).getField(fieldName, instanceId);
    }

    public static void show(String agentArgs, String p) {
        try {

            String[] ss = agentArgs.split("/");
            ss = java.util.Arrays.copyOf(ss, ss.length - 1);
            // PrintStream out = new PrintStream(new FileOutputStream(String.join("/", ss) +
            // "/testoutput.txt", true),
            // true);
            // PrintStream out = new

            // PrintStream("/home/liziming/software-analyze/elasticsearch-7.9.3/testoutput.txt");
            // System.setOut(out);
            // List<String> classNameList =
            // SystemConfig.getInstance().getInterestringClassNameList();
            // for (String className : classNameList) {
            // System.out.println("interest ClassName : " + className);
            // }
            Map<String, ClassInfo> map = Maps.filterValues(classInfoMap, r -> r.getInstanceCount() > 0);
            if (map.size() > 0) {
                String jsonfiledir = String.join("/", ss) + "/testoutput";
                File fp = new File(jsonfiledir);
                // 创建目录
                if (!fp.exists()) {
                    fp.mkdirs();// 目录不存在的情况下，创建目录。
                }
                long times = System.currentTimeMillis();
                String detailfiledir = jsonfiledir + "/detailoutput";// 存放利用率低的类的柱状图
                fp = new File(detailfiledir);
                // 创建目录
                if (!fp.exists()) {
                    fp.mkdirs();// 目录不存在的情况下，创建目录。
                }
                String jsonfilepath = jsonfiledir + "/testoutput." + times + ".json";
                FileOutputStream file = new FileOutputStream(jsonfilepath);
                if (SystemConfig.getInstance().getMode().equals("FieldDetection")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("instanceInfoMap",
                            "fieldNameList");
                    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);

                    objectMapper.writer(filters).writeValue(file, map);
                    file.close();
                    // 然后来计算，画柱状图与饼图
                    // System.out.println(map.keySet());
                    List<Object> charts = XchartDraw.drawBarandPieChart(map, Float.parseFloat(p));
                    String jsonfilename = jsonfilepath.split("/")[jsonfilepath.split("/").length - 1];
                    // jsonfilename = jsonfiledir + "/" + jsonfilename;
                    List<CategoryChart> barcharts = (List<CategoryChart>) charts.get(0);
                    List<PieChart> piecharts = (List<PieChart>) charts.get(1);
                    List<CategoryChart> detailcharts = (List<CategoryChart>) charts.get(2);
                    List<String> userconfig2 = (List<String>) charts.get(3);
                    // for (int j = 0; j < barcharts.size(); j++) {
                    //     // System.out.println(charts.get(j).getSeriesMap());
                    //     BitmapEncoder.saveBitmapWithDPI(barcharts.get(j), jsonfiledir + "/" + jsonfilename + j,
                    //             BitmapFormat.PNG, 400);
                    // }
                    // for (int j = 0; j < piecharts.size(); j++) {
                    //     // System.out.println("fefefefefefefefefewgegbegegbebe");
                    //     BitmapEncoder.saveBitmapWithDPI(piecharts.get(j),
                    //             jsonfiledir + "/" + jsonfilename + (j + barcharts.size()), BitmapFormat.PNG, 400);
                    // }
                    // for (int j = 0; j < detailcharts.size(); j++) {
                    //     BitmapEncoder.saveBitmapWithDPI(detailcharts.get(j), detailfiledir + "/" + jsonfilename + j,
                    //             BitmapFormat.PNG, 400);
                    // }
                    String userconfig2filepath = String.join("/", ss) + "/UserConfig2.txt";
                    File file2 = new File(userconfig2filepath);
                    // 如果没有文件就创建
                    if (!file2.isFile()) {
                        file2.createNewFile();
                    }
                    BufferedWriter writer = new BufferedWriter(new FileWriter(userconfig2filepath));
                   
                    
                    for (int j = 0; j < userconfig2.size(); j++) {
                        if (userconfig2.get(j) == null) {
                            continue;
                        }
                        writer.write(userconfig2.get(j) + "\n");
                    }
                    writer.close();

                } else if (SystemConfig.getInstance().getMode().equals("ConstructSite")) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter.serializeAllExcept("fieldInfoMap",
                            "fieldNameList");
                    FilterProvider filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);

                    objectMapper.writer(filters).writeValue(file, map);
                    file.close();
                }

            }

            // map.forEach((key, value) -> {
            // System.out.print("ClassName : " + key + "\t");
            // value.show();
            // });

            // out.close();

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

    public static int getInstanceId(String className, String[] constructSite) {
        if (!lockMap.containsKey(className)) {
            lockMap.put(className, new ReentrantLock());
        }
        Lock lock = lockMap.get(className);
        int instanceId = -1;
        lock.lock();
        try {
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

    public static String[] getStack() {
        String[] constructSite;

        StackTraceElement[] callStack = Thread.currentThread().getStackTrace();

        constructSite = new String[Math.min(SystemConfig.getInstance().getConstructSiteSize(), callStack.length - 2)];
        for (int i = 2, j = 0; j < constructSite.length; ++i, ++j) {
            constructSite[j] = callStack[i].toString();
        }

        return constructSite;
    }

    /*
     * public static String[] pushStack(String className,String methodName,String
     * methodDesc){ Long threadId = Thread.currentThread().getId(); String[]
     * constructSite; if(callStackMap.containsKey(threadId)){ //该线程已经有一个stack了
     * Stack<CallSite> callStack = callStackMap.get(threadId); callStack.push(new
     * CallSite(className,methodName,methodDesc));
     * 
     * constructSite = new
     * String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.
     * size())]; for(int i = callStack.size() - 1,j = 0; i >= callStack.size() -
     * constructSite.length; --i,j++){ constructSite[j] =
     * callStack.elementAt(i).toString();
     * 
     * }
     * 
     * } else{ Stack<CallSite> callStack = new Stack<>(); callStack.push(new
     * CallSite(className,methodName,methodDesc));
     * callStackMap.put(threadId,callStack); constructSite = new
     * String[Math.min(SystemConfig.getInstance().getConstructSiteSize(),callStack.
     * size())]; for(int i = callStack.size() - 1,j = 0; i >= callStack.size() -
     * constructSite.length; --i,j++){ constructSite[j] =
     * callStack.elementAt(i).toString();
     * 
     * }
     * 
     * 
     * } return constructSite; }
     * 
     * public static void popStack() { Long threadId =
     * Thread.currentThread().getId(); if (callStackMap.containsKey(threadId)) { //
     * 该线程已经有一个stack了 Stack<CallSite> callStack = callStackMap.get(threadId);
     * callStack.pop();
     * 
     * } else { System.out.println("popStack ERROR : threadID  = " +
     * Thread.currentThread().getId());
     * 
     * } }
     * 
     */
}
