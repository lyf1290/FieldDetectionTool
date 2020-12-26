package tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Floats;

import models.ClassInfo;
import models.FieldInfo;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.Styler.TextAlignment;

import javafx.util.Pair;

public class XchartDraw {
    public static String[] chartinfo = { "instancePutCount", "instanceGetCount", "putCount", "GetCount" };

    public static List<Object> drawBarandPieChart(Map<String, ClassInfo> datamap) {
        List<String> classesname = new ArrayList<String>(datamap.keySet());
        List<CategoryChart> barcharts = new ArrayList<CategoryChart>();
        List<PieChart> piecharts = new ArrayList<PieChart>();
        for (int i = 0; i < chartinfo.length; i++) {
            CategoryChart barchart = new CategoryChartBuilder()
                    .width(classesname.size() * 10 > 1000 ? classesname.size() * 10 : 1000).height(1200)
                    .title("类的利用率情况：" + chartinfo[i]).xAxisTitle("类编号").yAxisTitle("利用率").theme(ChartTheme.Matlab)
                    .build();
            // double[] xData = new double[] { 0.0, 1.0, 2.0 };
            // double[] yData = new double[] { 2.0, 1.0, 0.0 };
            // double[] xData1 = new double[] { 1.0, 2.0 };
            // double[] yData1 = new double[] { 1.0, 0.0 };
            barchart.getStyler().setStacked(true);
            barchart.getStyler().setHasAnnotations(false);
            // chart.getStyler().setXAxisLabelAlignment(xAxisLabelAlignment);
            // barchart.getStyler().setYAxisMax((double) 20);
            barchart.getStyler().setXAxisLabelRotation(-90);
            barchart.getStyler().setXAxisLabelAlignmentVertical(TextAlignment.Right);
            barchart.getStyler().setYAxisDecimalPattern("#.00");
            // System.out.println(chart.getStyler().getDecimalPattern());
            barchart.getStyler().setShowTotalAnnotations(false);
            barchart.getStyler().setLegendVisible(false);
            // chart.getStyler().setInfoPanelVisible(true);
            // chart.getStyler().setInfoPanelPosition(InfoPanelPosition.OutsideS);
            // List<String> xdata = List.of("dw","2","3");
            // List<Float> ydata = List.of(1.0f,2.0f,3.0f);
            // List<String> xdata2 = List.of("6","3");
            // List<Float> ydata2 = List.of(2.0f,3.0f);

            // chart.addInfoContent("eknebneneiobneibneibneoibneopibneorbnerobnroeibneoirbnrnonborn\ndfdsfsdfvsfvsvsvsvsvsvsw\nrgegegegegegege\newfgwfwfwfwf\ndefdefefefef");

            // chart.addSeries("Gaussian Blob ",null, ydata);
            // chart.addSeries("Gaussian Blob 1",null , ydata2);
            // List<Integer> range = IntStream.rangeClosed(0,
            // datamap.keySet().size()).boxed().collect(Collectors.toList());
            // chart.addSeries("Gaussian Blob ", range, null);
            // System.out.println("nkefnkefnk");
            barcharts.add(barchart);
            PieChart piechart = new PieChartBuilder().width(1000).height(1000).title("类的利用率情况整体描述：" + chartinfo[i]).build();
            piecharts.add(piechart);

        }

        // System.out.println(classesname);
        // List<ClassInfo> classinfo=new ArrayList<ClassInfo>(datamap.values());
        List<LinkedList<String>> fieldsinfo = new ArrayList();
        for (int j = 0; j < classesname.size(); j++) {
            fieldsinfo.add(new LinkedList<String>(datamap.get(classesname.get(j)).getFieldInfoMap().keySet()));
        }
        boolean shouldcontinue = true;
        int seriesid = 0;
        // for (int i = 0; i < chartinfo.length; i++) {
        // charts.get(i).addSeries("xdata", classesname, null);
        // }
        int[] instancecount = new int[classesname.size()];// 记录每个class的、实例化总数
        for (int j = 0; j < classesname.size(); j++) {
            instancecount[j] = datamap.get(classesname.get(j)).getInstanceCount();
        }
        float[][] counts = new float[4][classesname.size()];// 记录每个class的成员的四个指标的综合
        while (shouldcontinue) {
            shouldcontinue = false;
            // double[][] result = new double[4][classesname.size()];
            List<List<Number>> result = new ArrayList();
            for (int j = 0; j < 4; j++) {
                result.add(new ArrayList<Number>());
            }
            // System.out.println("开始while");
            for (int j = 0; j < classesname.size(); j++) {
                String classname = classesname.get(j);
                // System.out.println("classname:"+classname);
                if (fieldsinfo.get(j).size() > 0) {// 如果某个类的成员变量还没有遍历完
                    String field = fieldsinfo.get(j).removeFirst();
                    // System.out.println("field:"+field);
                    FieldInfo fieldinfos = datamap.get(classname).getFieldInfoMap().get(field);

                    counts[0][j] += fieldinfos.getInstancePutCount() * 1.0;
                    result.get(0).add(fieldinfos.getInstancePutCount() * 1.0 / instancecount[j]);// "instancePutCount"

                    counts[1][j] += fieldinfos.getInstanceGetCount() * 1.0;
                    result.get(1).add(fieldinfos.getInstanceGetCount() * 1.0 / instancecount[j]); // "instanceGetCount"

                    counts[2][j] += fieldinfos.getPutCount() * 1.0;
                    result.get(2).add(fieldinfos.getPutCount() * 1.0 / instancecount[j]); // "putCount"

                    counts[3][j] += fieldinfos.getGetCount() * 1.0;
                    result.get(3).add(fieldinfos.getGetCount() * 1.0 / instancecount[j]); // "GetCount"
                    if (fieldsinfo.get(j).size() > 0) {
                        shouldcontinue = true;// 还需要循环

                    }
                    continue;
                }
                result.get(0).add(null);// "instancePutCount"
                result.get(1).add(null); // "instanceGetCount"
                result.get(2).add(null); // "putCount"
                result.get(3).add(null); // "GetCount"
            }

            for (int i = 0; i < chartinfo.length; i++) {
                barcharts.get(i).addSeries("" + seriesid, classesname, result.get(i));
            }
            seriesid++;

        }
        // 获取饼图的数据
        for (int j = 0; j < classesname.size(); j++) {
            // 按照class进行获取
            for (int i = 0; i < chartinfo.length; i++) {
                counts[i][j] = counts[i][j] / instancecount[j];
            }
        }
        // 饼图的数据划分
        for (int i = 0; i < chartinfo.length; i++) {

            // float min=Floats.min(counts[i]);
            Floats.sortDescending(counts[i]);// 降序
            int max = (int) Math.ceil(counts[i][0]);// 向上取整
            int min = 0;

            // 划分数组，5分类吧
            int splitnum = 5;
            if (max - min < 5) {
                splitnum = max - min;
            }
            float space = ((max - min) * 1.0f / splitnum);
            //  使用流处理 把 数字分组
            int[] result = new int[splitnum];
            int currentinterval = 0;
            int last = max;
            int nextcmp = Math.round(max - space);// 四舍五入
            // System.out.println(counts[i][0]);
            for (int j = 0; j < counts[i].length; j++) {
                //System.out.println(counts[i][j]);
                //System.out.println(currentinterval);
                //System.out.println(nextcmp);
                if (counts[i][j] >= nextcmp) {
                    result[currentinterval]++;
                } else {

                    piecharts.get(i).addSeries("[" + nextcmp + "," +last + ")",
                            result[currentinterval]);

                    currentinterval++;
                    //System.out.println(space);
                    last=nextcmp;
                    nextcmp = Math.round(max - (currentinterval + 1) * space);// 四舍五入;
                    result[currentinterval]++;
                    
                }

            }
            piecharts.get(i).addSeries("[" + nextcmp + "," + last + ")",
                    result[currentinterval]);

        }
        return Arrays.asList(barcharts, piecharts);

    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
