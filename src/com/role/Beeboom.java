package com.role;

import com.ui.GameFrame;

/*** 蜜蜂子弹类
 * @author 刘坤昊
 * @version 3
 * 蜜蜂发射出子弹的类
 */
public class Beeboom extends Boom{
    public double xspeed,yspeed;
    public Beeboom(int x, int y, int width, double xspeed,double yspeed,GameFrame gf){
        super(x,y,width,gf);
        this.xspeed=xspeed;
        this.yspeed=yspeed;
    }
}
