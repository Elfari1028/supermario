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
public class GameFrame extends JFrame{
	// 超级玛丽:界面需要一个超级玛丽的。
	public Mario mario;
	// 分别定义:水管，金币,砖块，蘑菇
	public Enemy pipe ,cionBrick , brick,mashroom,turtle;
	//背景图片
	public BackgroundImage bg ;
	//定义一个集合容器装敌人对象
	public ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	//定义一个集合容器装子弹
	public ArrayList<Boom> boomList = new ArrayList<Boom>();

	//地图数据，制定规则，是1画砖头，是2画金币，是3画水管,4是蘑菇
	public int[][] map = null;
	{
		// 实例代码块中初始化地图资源的数据
		Map mp = new Map();
		map = mp.readMap();
	}

	//构造函数里面初始化背景图片和马里奥对象
	public GameFrame() throws Exception {
		//初始化窗体相关属性信息数据
		// this代表了当前主界面对象。
		this.setSize(800,450);
		this.setTitle("Mario");
		this.setResizable(false);
		// 居中展示窗口
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		// 创建玛丽对象
		mario = new Mario(this);

		// 创建背景图片
		bg = new BackgroundImage();

		// 读取地图，并配置地图
		relodeMap();

		mario.start();

		//开启一个线程负责界面的窗体重绘线程
		new Thread(){
			public void run(){
				while(true){
					//重绘窗体
					repaint(); // 自动触发当前窗口中的paint方法
					//检查子弹是否出界
					checkBoom();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();


		//设置背景音乐
		new Thread(new Runnable() {
			@Override
			public void run() {
				MusicUtil.playBackground();
			}
		}).start();
	}

	public void relodeMap(){
		enemyList.clear();
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				//读取到的是1，画砖头
				if(map[i][j]==1){
					brick = new Brick(j*30,i*30,30,30,new ImageIcon("image/brick.png").getImage());
					enemyList.add(brick);
				}
				//读到2画金币砖块
				if(map[i][j]==2){
					cionBrick = new CoinBrick(j*30,i*30,30,30,new ImageIcon("image/coin_brick.png").getImage());
					enemyList.add(cionBrick);
				}
				//读到3画水管
				if(map[i][j]==3){
					pipe = new Pipe(j*30,i*30,60,120,new ImageIcon("image/pipe.png").getImage());
					enemyList.add(pipe);
				}
				//读到4画蘑菇
				if (map[i][j]==4){
					mashroom = new Mashroom(j*30+5,i*30+10,20,20,new ImageIcon("image/mogu.png").getImage());
					enemyList.add(mashroom);
				}
				//读到5画乌龟
				if (map[i][j]==5){
					turtle = new Turtle(j*30,i*30,30,30,2.5,new ImageIcon("image/turtle.png").getImage(),this);
					enemyList.add(turtle);
					((Turtle)turtle).move();
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		try {
			//利用双缓冲画背景图片和马里奥
			BufferedImage bi = (BufferedImage) this.createImage(this.getSize().width, this.getSize().height);
			Graphics big = bi.getGraphics();

			big.drawImage(bg.img, bg.x, bg.y, null);


			// 开始绘制界面上的敌人。
			for (int i = 0; i < enemyList.size(); i++) {
				Enemy e = enemyList.get(i);
				big.drawImage(e.img, e.x, e.y, e.width, e.height, null);
			}


			//画子弹
			for (int i = 0; i < boomList.size(); i++) {
				Boom b = boomList.get(i);
				Color c = big.getColor();
				big.setColor(Color.yellow);
				big.fillOval(b.x += b.speed, b.y, b.width, b.width);
				big.setColor(c);
			}


			//画人物mario

			big.drawImage(mario.img, mario.x, mario.y, mario.width, mario.height, null);


			//画金币数量标签
			Color c = big.getColor();
			big.setColor(Color.black);
			big.drawString("Coins:" + mario.coinNum, 50, 50);
			big.setColor(c);

			//画死亡消息
			if (mario.isDead) {
				c = big.getColor();
				big.setColor(Color.red);
				big.drawString("You are dead, Please press R", 300, 220);
				big.setColor(c);
			}

			g.drawImage(bi, 0, 0, null);
		}catch (Exception e){}

	}

	//检查子弹是否出界，出界则从容器中移除
	public void checkBoom(){
		for (int i = 0; i < boomList.size(); i++) {
			Boom b = boomList.get(i);
			if(b.x<0 || b.x>800){
				boomList.remove(i);
			}
		}
	}

}
