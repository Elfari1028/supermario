package com.util;

// import org.junit.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

//地图配置类
public class Map {

    // 单元测试：验证Map类的readMap()方法确实把地图配置文件map.txt
    // 加载成了二维数组
    // @Test
    // public void testResult() throws Exception {
    // int[][] result = readMap("map.txt");
    // // 二维数组的内容输出，看一下是否是地图的配置信息
    // for (int i = 0; i < result.length; i++) {
    // for (int j = 0; j < result[i].length; j++) {
    // System.out.print(result[i][j] + " ");
    // }
    // System.out.println();
    // }
    // }

    // 支持自定义地图文件名字，便利多地图调用

    public static int[][] readMap(String name) throws Exception {
        List<String> list = new ArrayList<>();
        int[][] map = null;
        // 构造文件输入流
        FileInputStream fis = new FileInputStream(name);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        // 直接读取一行数据
        String value = br.readLine();

        while (value != null) {
            // 将读取到的一行数据加入到容器中
            list.add(value);
            value = br.readLine();
        }

        br.close();

        // 得到多少行多少列
        int row = list.size();
        int cloum = 0;

        for (int i = 0; i < 1; i++) {
            String str = list.get(i);
            String[] values = str.split(",");
            cloum = values.length;
        }

        map = new int[row][cloum];

        // 将读到的字符创转换成整数，并赋值给二位数组map
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] values = str.split(",");
            for (int j = 0; j < values.length; j++) {
                try {
                    map[i][j] = Integer.parseInt(values[j]);
                }catch (Exception e){
                    System.out.println("Map loads fail!");
                }

            }
        }
        return map;
    }

}
