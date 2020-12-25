public class TestFather {
    int testF = -1;
    int test;
    int st;
    TestFather(){
        this.testF = 1;
        this.test = 1;
        this.st = 2;
    }
    TestFather(int k){
        this.testF = 1;
        this.st = 2;
    }
    public void mm(){

        new Test(1);
        new TestFather();
    }
    public void ttt(){
        System.out.println(this.test);
    }
    public static void main(String args[]){
        new Test(1);
        new TestFather();

    }
}