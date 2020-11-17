

public class Test extends TestFather{
    int test = 0;
    int test1 = 1;
    int test2 = 2;

    public Test() {
    }

    public int getTest(){
        return this.test;
    }

    public static void main(String args[]){
        Test test = new Test();
        test.getTest();
        test.getTest();
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