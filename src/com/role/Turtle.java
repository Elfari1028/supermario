package com.role;

import com.ui.GameFrame;

import java.awt.*;

/*** 乌龟类
 * @author 刘坤昊
 * @version 5
 * 规定乌龟的基本属性和方法
 */
public class Turtle extends Enemy {
    public double g = 0.15;
    double speed;
    GameFrame gf;
    Thread moveThread;

    public Turtle(int x, int y, int width, int height, double speed, Image img, GameFrame gf) {
        super(x, y, width, height, img);
        this.speed = speed;
        this.gf = gf;
        this.initMove();
    }

    /**
     * 停止该类的线程
     */
    @Override
    public void dispose() {
        if (this.moveThread != null && this.moveThread.isAlive())
            this.moveThread.interrupt();
    }

    public void start() {
        this.moveThread.start();
    }

    /**
     * 移动线程
     */
    public void initMove() {
        this.moveThread = new Thread() {
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (isDead)
                        return;
                    if (hit("Left")) {
                        speed = -speed;
                    }

                    x += speed;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        };
    }

    /**
     * 碰撞检测
     */
    public boolean hit(String dir) {
        Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height);

        Rectangle rect = null;

        // 判断是否和其他障碍物相撞
        for (int i = 0; i < gf.enemyList.size(); i++) {
            Enemy enemy = gf.enemyList.get(i);
            if (enemy == this)
                continue;
            if (enemy == null)
                return false;
            if (dir.equals("Left")) {
                rect = new Rectangle(enemy.x + 5, enemy.y, enemy.width, enemy.height);
            } else if (dir.equals("Right")) {
                rect = new Rectangle(enemy.x - 5, enemy.y, enemy.width, enemy.height);
            }
            // 碰撞检测
            if (myrect.intersects(rect)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 死亡动画
     */
    public void deadMove(double speed) throws InterruptedException {
        // 无碰撞检测
        for (int i = 0; i < 1000; i++) {
            // 模拟重力加速度
            speed = ((speed - g) < 0 ? 0 : (speed - g));

            y -= speed;
            if (speed <= 0)
                break;

            Thread.sleep(10);

        }
        for (int i = 0; i < 1000; i++) {
            speed += g;

            speed = speed > 5 ? 5 : speed;
            y += speed;
            if (y > 450)
                break;

            Thread.sleep(10);

        }
        gf.enemyList.remove(this);
    }

}
