package com.ui;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.*;

import com.mario.MarioForDisplay;
import com.role.*;
import com.util.PainterCallback;
import com.util.Map;
import com.util.MusicUtil;

/*** UI界面父类
 * @author 艾力帕尔
 * @version 5
 * 为关卡选择界面、开始界面提供初始属性和方法
 */
@SuppressWarnings("serial")
public abstract class UIFrame extends JFrame implements PainterCallback{
	// 超级玛丽:界面需要一个超级玛丽的。
	public MarioForDisplay mario;
	public MarioForDisplay luigi;

	// 背景图片
	public BackgroundImage bg;
	// 定义一个集合容器装敌人对象
	public ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	// 定义一个集合容器装子弹
	public ArrayList<Boom> boomList = new ArrayList<Boom>();

	// 地图数据
	public int[][] map = null;

	// 线程对象，便利控制
	public Thread repaintThread;
    public Thread musicThread;

	/**
	 * 构造函数里面初始化背景图片和马里奥对象
	 */
	public UIFrame(String mapName) throws Exception {
		// 初始化窗体相关属性信息数据
		// this代表了当前主界面对象。
		this.setSize(800, 450);
		this.setTitle("MarioForDisplay");
		this.setResizable(false);
		// 居中展示窗口
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		// 创建玛丽对象
		mario = new MarioForDisplay(this);
		luigi = new MarioForDisplay(this);
		luigi.setPosition(600, 200);
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
						Thread.sleep(10);
					}

				} catch (InterruptedException e) {

				} finally {

				}
			}

		};
	}

	public void start() {
		this.setVisible(true);
		mario.start();
        luigi.start();
		repaintThread.start();
	}

	/**
	 * 停止该类的线程
	 */
	public void dispose() {
		this.mario.dispose();
		this.luigi.dispose();
		if (this.repaintThread != null && this.repaintThread.isAlive())
			this.repaintThread.interrupt();
		for (Enemy enemy : enemyList) {
			enemy.dispose();
		}
	}

	/**
	 * 加载地图
	 */
	public void loadMap(int x) {

		// 分别定义:水管，金币,砖块，蘑菇，地刺
		Enemy enemy;

		for (Enemy e : enemyList) {
			e.isDead = true;
		}

		enemyList.clear();
		boomList.clear();

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

				if (map[i][j] == 7) {
					enemy = new Trap(j * 30 + x, i * 30 + 15, 30, 15, new ImageIcon("image/trap.png").getImage());
					enemyList.add(enemy);
				}

				if (map[i][j] == 9) {
					enemy = new Flag(j * 30 + x, i * 30, 30, 30, new ImageIcon("image/flag.png").getImage());
					enemyList.add(enemy);
				}

			}
		}
	}

	/**
	 * 绘制所有元素
	 */
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

			// 画人物mario

			big.drawImage(mario.img, mario.x, mario.y, mario.width, mario.height, null);
			big.drawImage(luigi.img, luigi.x, luigi.y, luigi.width, luigi.height, null);
			this.addPainter(big);
            g.drawImage(bi, 0, 0, null);
		} catch (Exception e) {
			System.out.println("Loading map...");
		}

	}
}
