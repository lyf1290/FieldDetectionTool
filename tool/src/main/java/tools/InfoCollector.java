package tools;

import models.ClassInfo;
import system.SystemConfig;
import user.UserConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂时只考虑单线程
 */
public class InfoCollector {

    private static Map<String, ClassInfo> classInfoMap = new HashMap<>();


    static {
        classInfoMap.clear();
        List<String> classNameList = SystemConfig.getInstance().getInterestringClassNameList();
        for(String className : classNameList){
            classInfoMap.put(className,new ClassInfo(SystemConfig.getInstance().getFieldNameList(className)));
        }
//        List<String> fieldNames1 = new ArrayList<>();
//        fieldNames1.add("test");
//        fieldNames1.add("test1");
//        fieldNames1.add("test2");
//        fieldNames1.add("testF");
//        List<String> fieldNames2 = new ArrayList<>();
//        fieldNames2.add("testF");
//        classInfoMap.put("Test",new ClassInfo(fieldNames1));
//        classInfoMap.put("TestFather",new ClassInfo(fieldNames2));
    }

    public static void setClassInfoMap(Map<String, ClassInfo> classInfoMap){
        InfoCollector.classInfoMap = classInfoMap;
    }

    public static Map<String, ClassInfo> getClassInfoMap(){
        return InfoCollector.classInfoMap;
    }

    public static void newInstance(String className){
        classInfoMap.get(className).newInstance();
    }

    public static void descInstanceCount(String className){
        classInfoMap.get(className).descInstanceCount();
    }

    public static void putField(String className,String fieldName,boolean first){
        classInfoMap.get(className).putField(fieldName,first);
    }

    public static void getField(String className,String fieldName,boolean first){
        classInfoMap.get(className).getField(fieldName,first);
    }

    public static void show(){
        classInfoMap.forEach((key, value) -> {
            System.out.print("ClassName : " + key + "\t");
            value.show();
        });
    }


}
