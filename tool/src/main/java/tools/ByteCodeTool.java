package tools;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ByteCodeTool {

    public static byte[] input(String sourceClassPath){
        byte[] sourceByteCodes= null;

        try {
            sourceByteCodes = FileUtils.readFileToByteArray(new File(sourceClassPath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sourceByteCodes;
    }

    public static void output(byte[] bytesModified,String outputPath) {

        File file = new File(outputPath);

        FileOutputStream fileOutputStream = null;
        try {
            if (!file.exists()) {				//如果文件不存在则新建文件
                if(file.createNewFile()){
                    fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(bytesModified);
                }
            }
            else{
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(bytesModified);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {   //关闭流
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
