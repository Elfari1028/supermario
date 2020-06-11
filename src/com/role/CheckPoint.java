package com.role;
import java.awt.Image;

/*** 检查点类
 * @author 刘坤昊
 * @version 4
 * 检查点的属性和构造方法
 */
public class CheckPoint {
    public int x, y;
    public int width, height;
    public Image img;
    public boolean isActive = false;
    public CheckPoint(int x, int y, int width, int height, Image img) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = img;
    }

}
