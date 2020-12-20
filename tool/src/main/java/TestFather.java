import tools.InfoCollector;

public class TestFather {
    int testF = -1;

    public void mm(){

        new Test(1);
        new TestFather();
        InfoCollector.show();
    }

    public static void main(String args[]){
        new Test(1);
        new TestFather();
        InfoCollector.show();

    }
}
