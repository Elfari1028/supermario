package com.role;
import java.awt.Image;

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
