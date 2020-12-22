import tools.InfoCollector;

public class Test extends TestFather{
    int test = 0;
    int test1 = 1;
    int test2 = 2;
    public byte[] getDirtyTag;
    public byte[] putDirtyTag;
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
        switch (k){
            case 0:
                this.test = 1;
                break;
            case 3:
                this.test = 2;
                break;
            default:
                break;
        }

        if(k == 5){
            this.test = 5;
        }

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

    public void getField(String var1) {
        if (this.putDirtyTag == null) {
            this.putDirtyTag = new byte[4];
            this.getDirtyTag = new byte[4];
        }

        if (var1.equals("test")) {
            if (this.getDirtyTag[0] == 1) {
                InfoCollector.getField("Test", "test", false);
            } else {
                InfoCollector.getField("Test", "test", true);
                this.getDirtyTag[0] = 1;
            }
        } else if (var1.equals("test1")) {
            if (this.getDirtyTag[1] == 1) {
                InfoCollector.getField("Test", "test1", false);
            } else {
                InfoCollector.getField("Test", "test1", true);
                this.getDirtyTag[1] = 1;
            }
        } else if (var1.equals("test2")) {
            if (this.getDirtyTag[2] == 1) {
                InfoCollector.getField("Test", "test2", false);
            } else {
                InfoCollector.getField("Test", "test2", true);
                this.getDirtyTag[2] = 1;
            }
        } else if (var1.equals("testF")) {
            if (this.getDirtyTag[3] == 1) {
                InfoCollector.getField("Test", "testF", false);
            } else {
                InfoCollector.getField("Test", "testF", true);
                this.getDirtyTag[3] = 1;
            }
        } else {
            System.out.println("Error: instrument error!");
        }

    }

}