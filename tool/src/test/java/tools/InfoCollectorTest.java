package tools;

import models.ClassInfo;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InfoCollectorTest {

    @Test
    void newInstance() {
        Map<String, ClassInfo> infoMap = new HashMap<>();
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("test");
        ClassInfo classInfo = new ClassInfo(fieldNames);
        infoMap.put("test",classInfo);
        InfoCollector.setClassInfoMap(infoMap);
        InfoCollector.newInstance("test");
        assertEquals(1,classInfo.getInstanceCount());

    }

    @Test
    void descInstanceCount() {
        Map<String, ClassInfo> infoMap = new HashMap<>();
        List<String> fieldNames = new ArrayList<>();
        fieldNames.add("test");
        ClassInfo classInfo = new ClassInfo(fieldNames);
        infoMap.put("test",classInfo);
        InfoCollector.setClassInfoMap(infoMap);
        InfoCollector.descInstanceCount("test");
        assertEquals(-1,classInfo.getInstanceCount());
    }
}