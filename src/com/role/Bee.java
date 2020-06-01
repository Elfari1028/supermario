package com.role;
import com.ui.GameFrame;

import javax.swing.*;
import java.awt.*;

public class Bee extends Enemy{
    public double g=0.15;
    double speed;
    //range是蜜蜂飞翔的范围
    int range;
    //记录移动距离
    int moveDistance=0;
    GameFrame gf;
    //生命值，默认都为3
    public int life = 3;

    public Bee(int x, int y, int width, int height, double speed,int range,Image img,GameFrame gf) {
        super(x, y, width,height, img);
        this.speed = speed;
        this.range = range;
        this.gf=gf;
    }

    //移动线程
    public void move(){
        new Thread(){
            public void run(){
                //用于控制Bee发射子弹的频率
                int tem=0;
                while (true){
                    if (isDead)return;
                    hit("Right");
                    if (moveDistance>=range&&speed>0){
                        speed = -speed;
                    }else if(moveDistance<=0&&speed<0){
                        speed = -speed;
                    }

                    x+=speed;
                    moveDistance+=speed;
                    tem+=speed<0?-speed:speed;
                    if (tem>80){
                        addBeeBoom();
                        tem=0;
                    }

                    try {
                        this.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    //碰撞函数
    public boolean hit(String dir){

        Rectangle myrect = new Rectangle(this.x,this.y,this.width,this.height);

        Rectangle rect =null;

        //判断是否和子弹相撞
        for (Boom b : gf.boomList){
            if (b instanceof Beeboom)continue;
            if (dir.equals("Left")) {
                rect = new Rectangle(b.x + 5, b.y, b.width, b.width);
            } else if (dir.equals("Right")) {
                rect = new Rectangle(b.x - 5, b.y, b.width, b.width);
            }

            //如果活的bee碰到子弹，减去一条命
            if (myrect.intersects(rect)){
                if (life>1) {
                    gf.boomList.remove(b);
                    life--;
                    hurt();
                    return true;
                }else {
                    gf.boomList.remove(b);
                    isDead=true;
                    life--;
                    deadMove(2);
                    return true;
                }
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

    //受伤动画
    public void hurt(){
        new Thread(){
            public void run(){
                setImg(new ImageIcon("image/bee_hurt.png").getImage());
                try {
                    Thread.sleep(100);
                } catch (InterruptedException k) {
                    k.printStackTrace();
                }
                setImg(new ImageIcon("image/bee.png").getImage());
            }
        }.start();
    }

    //发射子弹
    public void addBeeBoom(){
        Beeboom b1 = new Beeboom(x,y,10,2,-3,gf);
        Beeboom b2 = new Beeboom(x,y,10,-2,-3,gf);
        gf.boomList.add(b1);
        gf.boomList.add(b2);
    }
}
