import adapters.ConstructSiteAdapter;
import adapters.FieldDetectionAdapter;
import javafx.util.Pair;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import system.SystemConfig;
import tools.ByteCodeTool;
import tools.InfoCollector;
import tools.XchartDraw;
import user.UserConfig;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.ClassInfo;

import static org.objectweb.asm.Opcodes.ASM8;

//TODO localvariable,max还没修改，输入输出模块还没修改，
public class Main {
    private final static int ASM_VERSION = ASM8;

    public static void main(String args[]) {
        Test test = new Test(1);
        int k = test.test;
        Test.Inner inner = test.new Inner();

////         System.out.println(args[0]);
//        String path = "/Users/liangyufei/Downloads/elasticsearch-master/testoutput";
//        //String path = args[0];
//        File file = new File(path);
//        ArrayList<String> jsonfiles = new ArrayList<String>();
//        File[] tempList = file.listFiles();
//        for (int i = 0; i < tempList.length; i++) {
//            if (tempList[i].isFile() && tempList[i].getName().endsWith(".json")) {
//                // System.out.println("文 件：" + tempList[i]);
//                jsonfiles.add(tempList[i].toString());
//            }
//            if (tempList[i].isDirectory()) {
//                // System.out.println("文件夹：" + tempList[i]);
//            }
//        }
//        System.out.println(jsonfiles);
//        // 开始反序列化
//        for (int i = 0; i < jsonfiles.size(); i++) {
//
//            try {
//                String jsonfilepath = jsonfiles.get(i);
//                System.out.println(jsonfilepath);
//                FileInputStream json = new FileInputStream(jsonfilepath);
//
//                ObjectMapper mapper = new ObjectMapper();
//                Map<String, ClassInfo> map = mapper.readValue(json, new TypeReference<Map<String, ClassInfo>>() {
//                });
//                // 然后来计算，画柱状图与饼图
//                // System.out.println(map.keySet());
//                List<Object> charts = XchartDraw.drawBarandPieChart(map);
//                String jsonfilename = jsonfilepath.split("/")[jsonfilepath.split("/").length - 1];
//                List<CategoryChart> barcharts = (List<CategoryChart>) charts.get(0);
//                List<PieChart> piecharts = (List<PieChart>) charts.get(1);
//                for (int j = 0; j < barcharts.size(); j++) {
//                    // System.out.println(charts.get(j).getSeriesMap());
//                    BitmapEncoder.saveBitmapWithDPI(barcharts.get(j), path+"/"+jsonfilename + j, BitmapFormat.PNG, 400);
//                }
//                for (int j = 0; j < piecharts.size(); j++) {
//                    // System.out.println(charts.get(j).getSeriesMap());
//                    BitmapEncoder.saveBitmapWithDPI(piecharts.get(j), path+"/"+jsonfilename + (j+barcharts.size()), BitmapFormat.PNG, 400);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//        }

    }
}

/*
 * 需要一个全局的Adapter去delegates各个任务 1、需要一个专门增加Field的Adapter 2、需要一个增加Method的Adapter
 * 3、需要一个增加Insn的Adapter 4、需要一个处理构造函数的AdviceAdapter
 */

// 如果是被子类构造函数调用的父类构造函数，里面的this是指向子类的
// -javaagent:/Users/liangyufei/Desktop/tool/target/tool-1.0-SNAPSHOT.jar

//        UserConfig.getInstance().setConfigFilePath("/Users/liangyufei/Desktop/FieldDetectionTool/tool/src/main/resources/UserConfig.txt","/Users/liangyufei/Desktop/FieldDetectionTool/tool/src/main/resources/EnvironmentConfig.txt");
//                UserConfig.getInstance().setConstructSiteSize(2);
//                byte[] classfileBuffer = ByteCodeTool.input("/Users/liangyufei/Desktop/FieldDetectionTool/tool/target/classes/Test.class");
//                ClassReader cr = new ClassReader(classfileBuffer);
//                ClassWriter cw = new ClassWriter(cr,ClassWriter.COMPUTE_MAXS);
//
//                //选择一个adpter去适配cr
////        FieldDetectionAdapter fda = new FieldDetectionAdapter(Opcodes.ASM8,cw);
////        cr.accept(fda,0);
//                ConstructSiteAdapter csa = new ConstructSiteAdapter(Opcodes.ASM8,cw);
//                cr.accept(csa,0);
//                ByteCodeTool.output(cw.toByteArray(),"/Users/liangyufei/Desktop/FieldDetectionTool/tool/out/Test.class");
//
