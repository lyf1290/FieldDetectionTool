import tools.InfoCollector;
import user.UserConfig;

public class Main {
    public static void main(String args[]){
        Test test = new Test();
        test.getTest();
        test.getTest();
        new TestFather();
        InfoCollector.show();
    }
}
//-javaagent:/Users/liangyufei/Desktop/FieldDetectionTool/tool/toolTest/tool.jar=/Users/liangyufei/Desktop/FieldDetectionTool/tool/toolTest/src/resources/UserConfig.txt