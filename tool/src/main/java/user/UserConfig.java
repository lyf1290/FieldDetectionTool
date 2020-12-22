package user;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class UserConfig {

    //用户关注的类在JVM中的internal name  例如com/Student
    private List<String> interestringClassFilesPathList = new ArrayList<>();
    private final List<String> environmentClassFilesPathList = new ArrayList<>();
    private String mode = "FieldDetection";
    private String userConfigFilePath = "";
    private int constructSiteSize = 2;
    private final static UserConfig USER_CONFIG = new UserConfig();
    public static UserConfig getInstance(){
        return USER_CONFIG;
    }

    private UserConfig(){

    }

    void init(){
        try {
            File f = new File(this.userConfigFilePath);
            this.interestringClassFilesPathList = new ArrayList<>();
            this.interestringClassFilesPathList = FileUtils.readLines(f, "UTF-8");
            this.environmentClassFilesPathList.add("/Users/liangyufei/Desktop/FieldDetectionTool/tool/target/classes/Test.class");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setUserConfigFilePath(String userConfigFilePath){
        this.userConfigFilePath = userConfigFilePath;
        this.init();
    }
    public void setMode(String mode){
        this.mode = mode;
    }
    public String  getMode(){
        return this.mode;
    }
    public List<String> getInterestringClassFilesPathList(){
        return this.interestringClassFilesPathList;
    }
    public List<String> getEnvironmentClassFilesPathList(){
        return this.environmentClassFilesPathList;
    }

    public int getConstructSiteSize() {
        return constructSiteSize;
    }
    public void setConstructSiteSize(int constructSiteSize){
        this.constructSiteSize = constructSiteSize;
    }
}
