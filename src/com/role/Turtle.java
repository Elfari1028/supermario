package com.role;

import com.ui.GameFrame;

import java.awt.*;

public class Turtle extends Enemy {
    double speed;
    GameFrame gf;
    public boolean isDead = false;
    public Turtle(int x, int y, int width, int height, double speed,Image img,GameFrame gf) {
        super(x, y, width,height, img);
        this.speed = speed;
        this.gf = gf;
    }

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


        //判断是否和其他障碍物相撞
        for (int i = 0; i < gf.enemyList.size(); i++) {
            Enemy enemy = gf.enemyList.get(i);
            if (enemy==this) continue;
            if (enemy==null) return false;
            if(dir.equals("Left")){
                rect = new Rectangle(enemy.x+2,enemy.y,enemy.width,enemy.height);
            }else if(dir.equals("Right")){
                rect = new Rectangle(enemy.x-2,enemy.y,enemy.width,enemy.height);
            }else if(dir.equals("Up")){
                rect = new Rectangle(enemy.x,enemy.y+5,enemy.width,enemy.height);
            }else if(dir.equals("Down")){
                rect = new Rectangle(enemy.x,enemy.y-5,enemy.width,enemy.height);
            }
            //碰撞检测
            if(myrect.intersects(rect)){
                return true;
            }
        }

        return false;
    }

}
