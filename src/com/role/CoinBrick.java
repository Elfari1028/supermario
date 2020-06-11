package com.role;
import java.awt.Image;

/*** 金币砖块类
 * @author 刘坤昊
 * @version 1
 */
public class CoinBrick extends Enemy {
    public boolean isOpen=false;
    public CoinBrick(int x, int y, int width, int height, Image img) {
        super(x, y, width, height, img);
    }
}
