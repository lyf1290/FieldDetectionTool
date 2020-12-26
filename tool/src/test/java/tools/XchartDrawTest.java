package tools;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ClassInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.demo.charts.ExampleChart;

import javafx.util.Pair;
import tools.XchartDraw;

public class XchartDrawTest {
    @Test
    void ReadJson() {
        // Test test = new Test(1);
        // test.getTest();
        // test.getTest();
        // Test.Inner inner = test.new Inner();
        // System.out.println(args[0]);
        String path = "/home/liziming/software-analyze/elasticsearch-7.9.3/testoutput1";
        File file = new File(path);
        ArrayList<String> jsonfiles = new ArrayList<String>();
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile() && tempList[i].getName().endsWith(".json")) {
                // System.out.println("文 件：" + tempList[i]);
                jsonfiles.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {
                // System.out.println("文件夹：" + tempList[i]);
            }
        }
        System.out.println(jsonfiles);
        // 开始反序列化
        for (int i = 0; i < 1; i++) {

            try {
                String jsonfilepath = jsonfiles.get(i);
                System.out.println(jsonfilepath);
                FileInputStream json = new FileInputStream(jsonfilepath);

                ObjectMapper mapper = new ObjectMapper();
                Map<String, ClassInfo> map = mapper.readValue(json, new TypeReference<Map<String, ClassInfo>>() {
                });
                // 然后来计算，画柱状图与饼图
                // System.out.println(map.keySet());
                List<Object> charts = XchartDraw.drawBarandPieChart(map);
                String jsonfilename = jsonfilepath.split("/")[jsonfilepath.split("/").length - 1];
                List<CategoryChart> barcharts = (List<CategoryChart>) charts.get(0);
                List<PieChart> piecharts = (List<PieChart>) charts.get(1);
                for (int j = 0; j < barcharts.size(); j++) {
                    // System.out.println(charts.get(j).getSeriesMap());
                    BitmapEncoder.saveBitmapWithDPI(barcharts.get(j), jsonfilename + j, BitmapFormat.PNG, 400);
                }
                for (int j = 0; j < piecharts.size(); j++) {
                    // System.out.println(charts.get(j).getSeriesMap());
                    BitmapEncoder.saveBitmapWithDPI(piecharts.get(j), jsonfilename + j + barcharts.size(),
                            BitmapFormat.PNG, 400);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
