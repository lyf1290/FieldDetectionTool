package models;

public class CallSite {
    String className;
    String methodName;
    String methodDesc;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public CallSite(String className,String methodName,String methodDesc){
        this.className = className;
        this.methodName = methodName;
        this.methodDesc = methodDesc;
    }
    @Override
    public String toString(){
        return className+" "+methodName+" "+methodDesc;
    }
}
