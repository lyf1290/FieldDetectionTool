import tools.InfoCollector;

public class Test extends TestFather{
    int test = 0;
    int test1 = 1;
    int test2 = 2;

    class Inner{
        int k;
        public Inner(){
            this.k = 1;
        }
        public void getTest(){
            this.k = 1;
        }
    }

    public Test(int k) {


    }
    public Test() {
        this.test = 2;
    }

    public int getTest(){
        return this.test;
    }

    public static void main(String args[]){
        Test test = new Test(1);
        test.getTest();
        test.getTest();
        Test.Inner inner = test.new Inner();
        //InfoCollector.show();
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