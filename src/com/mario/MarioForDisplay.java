package com.mario;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

import com.role.*;
import com.ui.UIFrame;

//自己的角色类
public class MarioForDisplay {

	public UIFrame gf;

	// 地图的左右边界
	public int mapLeft = 0, mapRight = 2000;
	// 是否可以jump
	public boolean jumpFlag = true;
	// 重力加速度
	public double g = 0.15;
	// 马里奥的初始坐标
	public int x = 180, y = 200;
	// 马里奥的速度
	public double xspeed = 5, yspeed = 6;
	// 马里奥的宽高
	public int width = 25, height = 30;
	// 马里奥的图片
	public Image img = new ImageIcon("image/mari1.png").getImage();
	// 马里奥朝左还是朝右
	public boolean isFaceRight = true;
	// 马里奥移动标志
	public boolean left = false, right = false, down = false, up = true;

	public boolean forDisplayOnly;
	public String Dir_Up = "Up", Dir_Left = "Left", Dir_Right = "Right", Dir_Down = "Down";

	Thread jumpThread;
	Thread runThread;
	Thread gravityThread;
	public MarioForDisplay(UIFrame frame) {
		this.gf = frame;
	}

	public void start() {
		this.initGravity();
		this.initRun();
		this.runThread.start();
		this.gravityThread.start();
	}

	public void dispose() {
		if (this.jumpThread != null && this.jumpThread.isAlive())
			this.jumpThread.interrupt();
		if (this.runThread != null && this.runThread.isAlive())
			this.runThread.interrupt();
		if (this.gravityThread != null && this.gravityThread.isAlive())
			this.gravityThread.interrupt();
	}

	// 玛丽移动的逻辑
	public void initRun() {
		this.runThread = new Thread() {
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
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

	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void createJumpThread(double speed) {
		this.jumpThread = new Thread() {
			public void run() {
				jump(yspeed);
				jumpFlag = true;
			}
		};
		this.jumpThread.start();
	}

	// 向上跳的函数
	public void jump(double speed) {

		for (int i = 0; i < 1000; i++) {
			// 碰撞检测一定要在移动之前
			if (hit(Dir_Up))
				break;
			// 模拟重力加速度
			speed = ((speed - g) < 0 ? 0 : (speed - g));

			y -= speed;
			if (hit(Dir_Up))
				break;
			if (speed <= 0)
				break;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}
		}
		// 为了构造反弹效果，所以speed与碰撞之间相等但反向
		for (int i = 0; i < 1000; i++) {
			if (hit(Dir_Down))
				break;
			speed += g;

			speed = speed > 5 ? 5 : speed;
			y += speed;
			if (hit(Dir_Down))
				break;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				return;
			}

		}

	}

	// 检测碰撞
	public boolean hit(String dir) {

		Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height);

		Rectangle rect = null;

		for (int i = 0; i < gf.enemyList.size(); i++) {
			Enemy enemy = gf.enemyList.get(i);
			if (enemy == null)
				return false;
			if (dir.equals("Left")) {
				rect = new Rectangle(enemy.x + 5, enemy.y, enemy.width, enemy.height);
			} else if (dir.equals("Right")) {
				rect = new Rectangle(enemy.x - 5, enemy.y, enemy.width, enemy.height);
			} else if (dir.equals("Up")) {
				rect = new Rectangle(enemy.x, enemy.y + 5, enemy.width, enemy.height);
			} else if (dir.equals("Down")) {
				rect = new Rectangle(enemy.x, enemy.y - 5, enemy.width, enemy.height);
			}
			// 碰撞检测
			if(myrect.intersects(rect))return true;
		}
		return false;
	}

	// 重力线程
	public void initGravity() {
		this.gravityThread = new Thread() {
			public void run() {

				while (!Thread.currentThread().isInterrupted()) {
					try {
						sleep(10);
					} catch (InterruptedException e) {
						// e.printStackTrace();
						return;
					}

					while (!Thread.currentThread().isInterrupted()) {
						if (!jumpFlag) {
							break;
						}

						if (hit(Dir_Down)) {
							break;
						}

						double speed = 0;
						while (!Thread.currentThread().isInterrupted()) {
							if (!jumpFlag) {
								break;
							}
							speed += g;
							speed = speed > 5 ? 5 : speed;
							y += speed;
							if (hit(Dir_Down))
								break;
							try {
								Thread.sleep(10);
							} catch (InterruptedException e) {
								// e.printStackTrace();
								return;
							}
						}

						try {
							sleep(10);
						} catch (InterruptedException e) {
							// e.printStackTrace();
							return;
						}
					}
				}

			}
		};
	}
}
