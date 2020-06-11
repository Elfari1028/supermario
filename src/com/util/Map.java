package com.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*** 地图类
 * @author 艾力帕尔
 * @version 5
 * 用于读取地图
 */
public class Map {

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
