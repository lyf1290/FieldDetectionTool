package models;

import static org.junit.jupiter.api.Assertions.*;


class FieldInfoTest {

    @org.junit.jupiter.api.Test
    void getField() {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.getField(false);
        assertEquals(1,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());
        fieldInfo.getField(true);
        assertEquals(2,fieldInfo.getGetCount());
        assertEquals(1,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(0,fieldInfo.getPutCount());

    }

    @org.junit.jupiter.api.Test
    void putField() {
        FieldInfo fieldInfo = new FieldInfo();
        fieldInfo.putField(false);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(0,fieldInfo.getInstancePutCount());
        assertEquals(1,fieldInfo.getPutCount());
        fieldInfo.putField(true);
        assertEquals(0,fieldInfo.getGetCount());
        assertEquals(0,fieldInfo.getInstanceGetCount());
        assertEquals(1,fieldInfo.getInstancePutCount());
        assertEquals(2,fieldInfo.getPutCount());
    }
}