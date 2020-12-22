public class TestFather {
    int testF = -1;
    int test;
    TestFather(){
        this.testF = 1;
        this.test = 1;
    }
    TestFather(int k){
        this.testF = 1;
    }
    public void mm(){

        new Test(1);
        new TestFather();
    }

    public static void main(String args[]){
        new Test(1);
        new TestFather();

    }
}