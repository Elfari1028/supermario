package com.mario;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

import com.role.*;
import com.ui.GameFrame;

//自己的角色类
public class Mario {

	public GameFrame gf;

	// 地图的左右边界
	public int mapLeft = 0, mapRight = 2000;
	// 是否可以jump
	public boolean jumpFlag = true;
	// 重力加速度
	public double g = 0.15;
	// 马里奥的初始坐标
	public int x = 180, y = 200;
	// 马里奥的复活坐标
	public int reviveX = 180, reviveY = 200, reviveMapX=0;
	// 马里奥的速度
	public double xspeed = 5, yspeed = 6;
	// 马里奥的宽高
	public int width = 25, height = 30;
	// 马里奥的图片
	public Image img = new ImageIcon("image/mari1.png").getImage();
	// 马里奥朝左还是朝右
	public boolean isFaceRight = true;
	// 马里奥是否变大
	public boolean isBig = false;
	// 存档时马里奥的大小
	public boolean bigRecord = false;
	// 马里奥的金币数量
	public int coinNum = 0;
	// 上一个存档的的金币数量
	public int coinRecord = 0;
	// 马里奥是否死亡
	public boolean isDead = false;
	// 马里奥是否通关
	public boolean isWin = false;
	// 马里奥移动标志
	public boolean left = false, right = false, down = false, up = false;

	public String Dir_Up = "Up", Dir_Left = "Left", Dir_Right = "Right", Dir_Down = "Down";

	Thread jumpThread;
	Thread runThread;
	Thread gravityThread;

	public Mario(GameFrame gf) {
		this.gf = gf;
	}

	public void start() {
		this.initGravity();
		this.initRun();
		this.runThread.start();
		this.gravityThread.start();
	}

	// 重生
	public void revive() {
			//没有胜利复活，金币数量等于存档时的金币数量，大小等于存档的大小，并且不能往地图走
			//胜利复活，金币=0，大小正常，回到原点
			coinNum = isWin?0:coinRecord;
			isBig = isWin?false:bigRecord;
			if (isBig){
				width = 30;
				height = 36;
			}else {
				width = 25;
				height = 30;
			}
			jumpFlag = true;
			this.x = reviveX;
			this.y = reviveY;
			// 背景归位
			gf.bg.x = reviveMapX;
			// 重新加载地图
			gf.loadMap(reviveMapX);
			mapLeft = reviveMapX;
			isWin = false;
			isDead = false;
	}

	public void dispose() {
		if(this.jumpThread != null && this.jumpThread.isAlive()) this.jumpThread.interrupt();
		if(this.runThread != null && this.runThread.isAlive())this.runThread.interrupt();
		if(this.gravityThread != null && this.gravityThread.isAlive())this.gravityThread.interrupt();
	}

	// 玛丽移动的逻辑
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

						// 任人物向左移动
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
								img = new ImageIcon("image/mari_left.gif").getImage();
							} else {
								if (x >= 0) {
									x -= xspeed;
									img = new ImageIcon("image/mari_left.gif").getImage();
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

						// 任人物向右移动
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
								img = new ImageIcon("image/mari_right.gif").getImage();
							} else {
								if (x <= 770) {
									x += xspeed;
									img = new ImageIcon("image/mari_right.gif").getImage();
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
			if (y > 600) {
				isDead = true;
				break;
			}
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

	// 检测是否与检查点相交（如果放在碰撞函数里，可能会出现穿模bug）
	public void hitCheckPoint(){
		Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height);
		Rectangle rect = null;

		for (int i=0;i<gf.checkPointList.size();i++){
			CheckPoint c = gf.checkPointList.get(i);
			rect = new Rectangle(c.x,c.y,c.width,c.height);

			//活的马里奥碰到未启动的检查点，则检查点启动并且刷新复活的位置
			if (myrect.intersects(rect) && !isDead && !c.isActive) {
				c.isActive = true;
				c.img = new ImageIcon("image/checkpoint_active.png").getImage();
				//设置复活坐标为检查点
				reviveX = 180;
				//稍微向上设置一些，防止卡住
				reviveY = c.y - 10;
				reviveMapX = gf.bg.x - (c.x - 180);
				coinRecord=coinNum;
				bigRecord=isBig;
			}
		}
	}

	// 检测碰撞
	public boolean hit(String dir) {

		Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.height);

		Rectangle rect = null;
		// 检测是否碰到BeeBoom
		try {
			for (int i = 0; i < gf.boomList.size(); i++) {
				Boom b = gf.boomList.get(i);
				if (b instanceof Beeboom) {
					Beeboom bb = (Beeboom) b;
					rect = new Rectangle(bb.x, bb.y, bb.width, bb.width);

					// 活的马里奥碰到BeeBoom，死
					if (myrect.intersects(rect) && !isDead) {
						gf.boomList.remove(bb);
						isDead = true;
						deadMove(2);
						return true;
					}
				}
			}

			// 检测是否碰到障碍物
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

				if (myrect.intersects(rect)) {
					// 碰到到是蘑菇，第一次变大，第二次变小
					if (enemy instanceof Mashroom) {
						if (isBig) {
							gf.enemyList.remove(enemy);
							isBig = false;
							width = 25;
							height = 30;
						} else {
							gf.enemyList.remove(enemy);
							isBig = true;
							y -= 6;
							width = 30;
							height = 36;
						}
					}
					// 碰到的是金币砖块
					else if (dir.equals("Up") && enemy instanceof CoinBrick) {
						if (!((CoinBrick) enemy).isOpen) {
							((CoinBrick) enemy).isOpen = true;
							enemy.setImg(new ImageIcon("image/coin_brick_null.png").getImage());
							Coin coin = new Coin(enemy.x + 4, enemy.y - 10, 22, 30,
									new ImageIcon("image/coin.png").getImage());
							gf.enemyList.add(coin);

							for (int j = 0; j < 10; j++) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								coin.y -= 2;
							}
						}
					}
					// 碰到金币
					else if (enemy instanceof Coin) {
						gf.enemyList.remove(enemy);
						coinNum++;
					}

					// 从左右碰到乌龟，乌龟是活的，马里奥也是活的，马里奥才能死
					else if ((!isDead) && enemy instanceof Turtle && (dir.equals(Dir_Left) || dir.equals(Dir_Right))
							&& !((Turtle) enemy).isDead) {
						isDead = true;
						deadMove(2);
					}
					// 从上方碰到乌龟，乌龟是活的，马里奥也是活的，乌龟才能死
					else if ((!isDead) && enemy instanceof Turtle && dir.equals(Dir_Down) && !((Turtle) enemy).isDead) {
						((Turtle) enemy).isDead = true;
						((Turtle) enemy).deadMove(2);
					}
					// 活的马里奥碰到地刺，无论从那个方向，就死亡
					else if ((!isDead) && enemy instanceof Trap) {
						isDead = true;
						deadMove(2);
					}
					// 活的马里奥碰到活的蜜蜂，无论那个方向，死
					else if ((!isDead) && enemy instanceof Bee && ((Bee) enemy).life > 0) {
						isDead = true;
						deadMove(2);
					}
					// 活的马里奥碰到flag，胜利,并且重置复活点和record
					else if ((!isDead) && enemy instanceof Flag) {
						isWin = true;
						reviveX = 180;
						reviveY = 200;
						reviveMapX = 0;
						coinRecord = 0;
						bigRecord = false;
					}

					return true;
				}
			}
		} catch (InterruptedException e) {
			// 本来应该立即退出 true false 无所谓了
			return false;
		}

		return false;
	}

	// 马里奥死亡动画
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
			if (!isDead)
				break;
			Thread.sleep(10);
		}
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
							if (y > 600) {
								isDead = true;
								break;
							}
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

	// 添加子弹
	public void addBoom() {
		Boom b = new Boom(x, y + 5, 10, gf);
		b.speed = isFaceRight ? 4 : -4;
		gf.boomList.add(b);
	}

}
