import tools.InfoCollector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test extends TestFather{
    int test = 0;
    int test1 = 1;
    int test2 = 2;

    public Test(int k) {
    }

    public int getTest(){
        return this.test;
    }

    public static void main(String args[]){
        Test test = new Test(1);
        test.getTest();
        test.getTest();
        InfoCollector.show(args[0]);
//        Class<?> clazz = Test.class;
//        Test test = new Test();
//        try {
//            //Method method = clazz.getMethod("modify",String.class);
//            Test test = new Test();
//            //method.invoke(test,(String)"test");
//        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//            e.printStackTrace();
//        }
    }

}