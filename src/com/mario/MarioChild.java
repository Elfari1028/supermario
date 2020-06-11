package com.mario;

import com.role.Boom;
import com.role.CheckPoint;
import com.role.Enemy;
import com.ui.GameFrame;

import javax.swing.*;

/*** 子马里奥类
 * @author 刘坤昊
 * @version 3
 * 继承马里奥类，用于双人模式下另一个角色，重写initRun()方法
 */
public class MarioChild extends Mario {
    public MarioChild(GameFrame gf){
        super(gf);
    }

    /**
     * 子马里奥重写移动函数
     */
    public void initRun() {
        this.runThread = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (isDead || isWin) {
                        try {
                            Thread.sleep(20);
                        } catch (InterruptedException e) {
                            // e.printStackTrace();
                            return;
                        }
                        continue;
                    }
                    hitCheckPoint();
                    // 向左走
                    if (left) {
                        isFaceRight = false;
                        // 碰撞到了
                        if (hit(Dir_Left)) {
                            xspeed = 0;
                        }

                        // 主马里奥死前，任人物向左移动,该马里奥无法移动地图
                        if (!gf.mario.isDead) {
                            if (x >= 0) {
                                x -= xspeed;
                                img = new ImageIcon("image/mari_left.gif").getImage();
                            }
                        }else {//死后则和主马里奥相同
                            if (x >= 200) {
                                x -= xspeed;
                                img = new ImageIcon("image/mari_left.gif").getImage();
                            }

                            if (x < 200) {
                                if (gf.bg.x < mapLeft) {
                                    // 背景向右移动
                                    gf.bg.x += xspeed;
                                    // 障碍物项右移动
                                    for (int i = 0; i < gf.enemyList.size(); i++) {
                                        Enemy enemy = gf.enemyList.get(i);
                                        enemy.x += xspeed;
                                    }
                                    // 子弹向右移动
                                    for (int i = 0; i < gf.boomList.size(); i++) {
                                        Boom b = gf.boomList.get(i);
                                        b.x += xspeed;
                                    }
                                    //检查点向右移动
                                    for (int i = 0; i < gf.checkPointList.size(); i++){
                                        CheckPoint c = gf.checkPointList.get(i);
                                        c.x +=xspeed;
                                    }
                                    //另一个马里奥移动
                                    if (gf.isMultiplayer){
                                        gf.luigi.x+=xspeed;
                                    }
                                    img = new ImageIcon("image/mari_left.gif").getImage();
                                } else {
                                    if (x >= 0) {
                                        x -= xspeed;
                                        img = new ImageIcon("image/mari_left.gif").getImage();
                                    }
                                }
                            }
                        }
                        xspeed = 5;
                    }

                    // 向右走
                    else if (right) {
                        isFaceRight = true;
                        // 右边碰撞物检测应该是往右走的时候检测
                        // 进行碰撞检测：至少主角（玛丽，碰撞物）
                        if (hit(Dir_Right)) {
                            xspeed = 0;
                        }

                        // 主马里奥死前，任人物向右移动,该马里奥无法移动地图
                        if (!gf.mario.isDead) {
                            if (x <= 800) {
                                x += xspeed;
                                img = new ImageIcon("image/mari_right.gif").getImage();
                            }
                        }else{//死后则和马里奥相同
                            if (x < 550) {
                                x += xspeed;
                                img = new ImageIcon("image/mari_right.gif").getImage();
                            }

                            if (x >= 550) {
                                if ((800 - gf.bg.x) < mapRight) {
                                    // 背景向左移动
                                    gf.bg.x -= xspeed;
                                    // 障碍物项左移动
                                    for (int i = 0; i < gf.enemyList.size(); i++) {
                                        Enemy enemy = gf.enemyList.get(i);
                                        enemy.x -= xspeed;
                                    }
                                    // 子弹向左移动
                                    for (int i = 0; i < gf.boomList.size(); i++) {
                                        Boom b = gf.boomList.get(i);
                                        b.x -= xspeed;
                                    }
                                    //检查点向左移动
                                    for (int i = 0; i < gf.checkPointList.size(); i++){
                                        CheckPoint c = gf.checkPointList.get(i);
                                        c.x -=xspeed;
                                    }
                                    //另一个马里奥移动
                                    if (gf.isMultiplayer){
                                        gf.luigi.x-=xspeed;
                                    }
                                    img = new ImageIcon("image/mari_right.gif").getImage();
                                } else {
                                    if (x <= 770) {
                                        x += xspeed;
                                        img = new ImageIcon("image/mari_right.gif").getImage();
                                    }
                                }
                            }
                        }
                        xspeed = 5;
                    } else {// 即没有向左走，又没向右走
                        hit(Dir_Right);
                        hit(Dir_Left);
                    }

                    // 向上跳
                    if (up) {
                        if (jumpFlag) {
                            jumpFlag = false;
                            // 为了实现在空中也能左右移动
                            createJumpThread(yspeed);
                        }
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
    }
}
