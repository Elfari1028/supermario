package com.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

import com.mario.Mario;
import com.role.*;
import com.util.Map;
import com.util.MusicUtil;

/*
   主体窗口界面：展示角色。
 */
@SuppressWarnings("serial")
public class GameFrame extends JFrame {
	// 超级玛丽:界面需要一个超级玛丽的。
	public Mario mario;
	public Mario luigi;

	public boolean isMultiplayer;

	// 背景图片
	public BackgroundImage bg;
	// 定义一个集合容器装敌人对象
	public ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	// 定义一个集合容器装子弹
	public ArrayList<Boom> boomList = new ArrayList<Boom>();
	// 定义一个集合容器装检查点
	public ArrayList<CheckPoint> checkPointList = new ArrayList<CheckPoint>();

	// 地图数据
	public int[][] map = null;

	// 线程对象，便利控制
	public Thread repaintThread;
	public Thread musicThread;

	// 构造函数里面初始化背景图片和马里奥对象
	public GameFrame(String mapName, boolean isMultiplayer) throws Exception {
		// 初始化窗体相关属性信息数据
		// this代表了当前主界面对象。
		this.setSize(800, 450);
		this.setTitle("Mario");
		this.setResizable(false);
		// 居中展示窗口
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		this.isMultiplayer = isMultiplayer;
		// 创建玛丽对象
		mario = new Mario(this);
		if (isMultiplayer)
			luigi = new Mario(this);

		// 创建背景图片
		bg = new BackgroundImage();

		// 读取地图
		this.map = Map.readMap(mapName);

		// 配置地图
		loadMap(0);

		// 开启一个线程负责界面的窗体重绘线程, 被中断后停止
		this.repaintThread = new Thread() {
			public void run() {
				try {
					while (!Thread.currentThread().isInterrupted()) {

						// 重绘窗体
						repaint(); // 自动触发当前窗口中的paint方法

						// 检查子弹是否出界
						checkBoom();
						Thread.sleep(10);
					}

				} catch (InterruptedException e) {

				} finally {

				}
			}

		};

		// 设置背景音乐
		this.musicThread = new Thread(new Runnable() {
			@Override
			public void run() {
				MusicUtil.playBackground();
				try {
					while (!Thread.currentThread().isInterrupted()) {
						Thread.sleep(10000);
					}
					;
				} catch (InterruptedException e) {

				} finally {
					MusicUtil.stop();
				}
			}
		});
	}

	public void start() {
		this.setVisible(true);
		// 马里奥开始移动
		mario.start();
		if (isMultiplayer)
			luigi.start();
		repaintThread.start();
	}

	public void pause() {

	}

	public void dispose() {
		this.mario.dispose();
		if (isMultiplayer)
			this.luigi.dispose();
		if (this.repaintThread != null && this.repaintThread.isAlive())
			this.repaintThread.interrupt();
		if (this.repaintThread != null && this.musicThread.isAlive())
			this.musicThread.interrupt();
		for (Enemy enemy : enemyList) {
			enemy.dispose();
		}
	}

	//x表示初始时的偏移量，用于适配检查点复活
	public void loadMap(int x) {

		// 分别定义:水管，金币,砖块，蘑菇，地刺
		Enemy enemy;

		for (Enemy e : enemyList) {
			e.isDead = true;
		}

		enemyList.clear();
		boomList.clear();
		checkPointList.clear();

		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				// 读到1，画泥土
				if (map[i][j] == 1) {
					enemy = new Mud(j * 30 + x, i * 30, 30, 30, new ImageIcon("image/mud.jpeg").getImage());
					enemyList.add(enemy);
				}
				// 读取到的是2，画砖头
				if (map[i][j] == 2) {
					enemy = new Brick(j * 30 + x, i * 30, 30, 30, new ImageIcon("image/brick.png").getImage());
					enemyList.add(enemy);
				}
				// 读到3画金币砖块
				if (map[i][j] == 3) {
					enemy = new CoinBrick(j * 30 + x, i * 30, 30, 30, new ImageIcon("image/coin_brick.png").getImage());
					enemyList.add(enemy);
				}
				// 读到4画水管
				if (map[i][j] == 4) {
					enemy = new Pipe(j * 30 + x, i * 30, 60, 120, new ImageIcon("image/pipe.png").getImage());
					enemyList.add(enemy);
				}
				// 读到5画蘑菇
				if (map[i][j] == 5) {
					enemy = new Mashroom(j * 30 + 5 + x, i * 30 + 10, 20, 20, new ImageIcon("image/mogu.png").getImage());
					enemyList.add(enemy);
				}
				// 读到6画乌龟
				if (map[i][j] == 6) {
					enemy = new Turtle(j * 30 + x, i * 30, 30, 30, 2.5, new ImageIcon("image/turtle.png").getImage(), this);
					enemyList.add(enemy);
					((Turtle) enemy).start();
				}
				// 读到7画地刺
				if (map[i][j] == 7) {
					enemy = new Trap(j * 30 + x, i * 30 + 15, 30, 15, new ImageIcon("image/trap.png").getImage());
					enemyList.add(enemy);
				}
				// 读到8画蜜蜂
				if (map[i][j] == 8) {
					enemy = new Bee(j * 30 + x, i * 30, 30, 30, 2, 150, new ImageIcon("image/bee.png").getImage(), this);
					enemyList.add(enemy);
					((Bee) enemy).start();
				}
				// 读到9画终点flag
				if (map[i][j] == 9) {
					enemy = new Flag(j * 30 + x, i * 30, 30, 30, new ImageIcon("image/flag.png").getImage());
					enemyList.add(enemy);
				}
				//读到10画检查点
				if (map[i][j] == 10){
					CheckPoint checkPoint = new CheckPoint( j * 30 + x, i * 30, 30, 30, new ImageIcon("image/checkpoint.png").getImage());
					checkPointList.add(checkPoint);
				}
			}
		}
	}

	public void paint(Graphics g) {
		try {
			// 利用双缓冲画背景图片和马里奥
			BufferedImage bi = (BufferedImage) this.createImage(this.getSize().width, this.getSize().height);
			Graphics big = bi.getGraphics();

			big.drawImage(bg.img, bg.x, bg.y, null);

			// 开始绘制界面上的敌人。
			for (int i = 0; i < enemyList.size(); i++) {
				Enemy e = enemyList.get(i);
				big.drawImage(e.img, e.x, e.y, e.width, e.height, null);
			}

			//绘制检查点
			for (int i=0; i<checkPointList.size(); i++){
				CheckPoint c = checkPointList.get(i);
				big.drawImage(c.img, c.x, c.y, c.width, c.height,null);
			}

			// 画子弹
			for (int i = 0; i < boomList.size(); i++) {
				Boom b = boomList.get(i);
				Color c = big.getColor();
				if (b instanceof Beeboom) {
					Beeboom bb = (Beeboom) b;
					bb.x += bb.xspeed;
					bb.yspeed += 0.15;
					bb.y += bb.yspeed;
					big.setColor(Color.black);
					big.fillOval(bb.x, bb.y, bb.width, bb.width);
				} else {
					big.setColor(Color.yellow);
					big.fillOval(b.x += b.speed, b.y, b.width, b.width);
				}

				if (b.hit())
					boomList.remove(b);
				big.setColor(c);
			}

			// 画人物mario

			big.drawImage(mario.img, mario.x, mario.y, mario.width, mario.height, null);
			if (isMultiplayer)
				big.drawImage(luigi.img, luigi.x, luigi.y, luigi.width, luigi.height, null);

			// 画金币数量标签
			Color c = big.getColor();
			big.setColor(Color.black);
			big.drawString("Coins:" + mario.coinNum, 50, 50);
			big.setColor(c);

			// 画死亡消息
			if (mario.isDead && (!isMultiplayer || (isMultiplayer && luigi.isDead))) {
				c = big.getColor();
				big.setColor(Color.red);
				big.drawString("You are dead, Please press R", 300, 220);
				big.setColor(c);
			}

			// 画胜利消息
			if (!isMultiplayer&&mario.isWin) {
				c = big.getColor();
				big.setColor(Color.yellow);
				big.drawString("You won, Press R to restart", 300, 220);
				big.setColor(c);
			}else if(isMultiplayer&&mario.isWin&&luigi.isWin){
				c = big.getColor();
				big.setColor(Color.yellow);
				big.drawString("Both of you won, Press R to restart", 300, 220);
				big.setColor(c);
			}else if((isMultiplayer && luigi.isWin && mario.isDead) || (isMultiplayer && luigi.isDead && mario.isWin)){
				c = big.getColor();
				big.setColor(Color.yellow);
				big.drawString("Only one of you won, Press R to restart", 300, 220);
				big.setColor(c);
			}

			g.drawImage(bi, 0, 0, null);
		} catch (Exception e) {
			System.out.println("Loading map...");
			// System.out.println(e.getMessage());
		}

	}

	// 检查子弹是否出界，出界则从容器中移除
	public void checkBoom() {
		for (int i = 0; i < boomList.size(); i++) {
			Boom b = boomList.get(i);
			if (b.x < 0 || b.x > 800 || b.y > 450) {
				boomList.remove(i);
			}
		}
	}

}
