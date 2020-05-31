package com.role;

import com.ui.GameFrame;

public class Beeboom extends Boom{
    public double xspeed,yspeed;
    public Beeboom(int x, int y, int width, double xspeed,double yspeed,GameFrame gf){
        super(x,y,width,gf);
        this.xspeed=xspeed;
        this.yspeed=yspeed;
    }
}
