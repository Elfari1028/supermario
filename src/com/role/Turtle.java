package com.role;

import com.ui.GameFrame;

import java.awt.*;

public class Turtle extends Enemy {
    public double g=0.15;
    double speed;
    GameFrame gf;
    public boolean isDead = false;
    public Turtle(int x, int y, int width, int height, double speed,Image img,GameFrame gf) {
        super(x, y, width,height, img);
        this.speed = speed;
        this.gf = gf;
    }

    //移动线程
    public void move(){
        new Thread(){
            public void run(){
                while (true){
                    if (hit("Right")){
                        speed = -speed;
                    }

                    x+=speed;
                    try {
                        this.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //检测碰撞
    public boolean hit(String dir){

        Rectangle myrect = new Rectangle(this.x,this.y,this.width,this.height);

        Rectangle rect =null;

        //判断是否和子弹相撞
        for (Boom b : gf.boomList){
            if(dir.equals("Left")){
                rect = new Rectangle(b.x+5,b.y,b.width,b.width);
            }else if(dir.equals("Right")) {
                rect = new Rectangle(b.x-5,b.y,b.width,b.width);
            }
            //如果活的乌龟碰到子弹，死
            if (myrect.intersects(rect)&&!isDead){
                gf.boomList.remove(b);
                isDead = true;
                deadMove(2);
                return true;
            }
        }

        //判断是否和其他障碍物相撞
        for (int i = 0; i < gf.enemyList.size(); i++) {
            Enemy enemy = gf.enemyList.get(i);
            if (enemy==this) continue;
            if (enemy==null) return false;
            if(dir.equals("Left")){
                rect = new Rectangle(enemy.x+5,enemy.y,enemy.width,enemy.height);
            }else if(dir.equals("Right")) {
                rect = new Rectangle(enemy.x-5, enemy.y, enemy.width, enemy.height);
            }
            //碰撞检测
            if(myrect.intersects(rect)){
                return true;
            }
        }

        return false;
    }

    //死亡动画
    public void deadMove(double speed){
        //无碰撞检测
        for (int i = 0; i < 1000; i++) {
            //模拟重力加速度
            speed=((speed-g)<0?0:(speed-g));

            y-=speed;
            if (speed<=0)break;
            try {
                Thread.sleep(10);
            } catch (InterruptedException k) {
                k.printStackTrace();
            }
        }
        for (int i = 0; i <1000; i++) {
            speed+=g;

            speed=speed>5?5:speed;
            y+=speed;
            if (y>450) break;

            try {
                Thread.sleep(10);
            } catch (InterruptedException k) {
                k.printStackTrace();
            }
        }
        gf.enemyList.remove(this);
    }

}
