package models;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassInfoTest {

    @Test
    void initFieldInfoMap() {
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("test");
        fieldNames.add("tes1");
        ClassInfo classInfo = new ClassInfo(fieldNames);
        Map<String,FieldInfo> fieldInfoMap = classInfo.getFieldInfoMap();
        assertEquals(fieldNames.size(),fieldInfoMap.size());
        fieldInfoMap.forEach((key, value) -> {
            assertTrue(fieldNames.contains(key));
        });
        fieldNames.add("test2");
        classInfo.initFieldInfoMap(fieldNames);
        assertEquals(fieldNames.size(),fieldInfoMap.size());
        fieldInfoMap.forEach((key, value) -> {
            assertTrue(fieldNames.contains(key));
        });

    }

    @Test
    void newInstance() {
        ClassInfo classInfo = new ClassInfo();
        int oldCount = classInfo.getInstanceCount();
        classInfo.newInstance();
        assertEquals(oldCount+1,classInfo.getInstanceCount());


    }

    @Test
    void descInstanceCount(){
        ClassInfo classInfo = new ClassInfo();
        int oldCount = classInfo.getInstanceCount();
        classInfo.descInstanceCount();
        assertEquals(oldCount-1,classInfo.getInstanceCount());
    }

    @Test
    void getField() {
        List<String> fieldNames = Arrays.asList("test","test1");
        ClassInfo classInfo = new ClassInfo(fieldNames);

        FieldInfo fieldInfo = classInfo.getFieldInfoMap().get("test");
        classInfo.getField("test",false);
        assertEquals(1,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());
        classInfo.getField("test",true);
        assertEquals(2,fieldInfo.getGetCount());
        assertEquals(1,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());
        fieldInfo = classInfo.getFieldInfoMap().get("test1");
        classInfo.getField("test1",false);
        assertEquals(1,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());
        classInfo.getField("test1",true);
        assertEquals(2,fieldInfo.getGetCount());
        assertEquals(1,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());
    }

    @Test
    void putField() {
        List<String> fieldNames = Arrays.asList("test","test1");
        ClassInfo classInfo = new ClassInfo(fieldNames);

        FieldInfo fieldInfo = classInfo.getFieldInfoMap().get("test");
        classInfo.putField("test",false);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(1,fieldInfo.getPutCount());
        classInfo.putField("test",true);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(1,fieldInfo.getInstancePutCount());
        assertEquals(2,fieldInfo.getPutCount());
        fieldInfo = classInfo.getFieldInfoMap().get("test1");
        classInfo.putField("test1",false);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(1,fieldInfo.getPutCount());
        classInfo.putField("test1",true);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(1,fieldInfo.getInstancePutCount());
        assertEquals(2,fieldInfo.getPutCount());
    }
}