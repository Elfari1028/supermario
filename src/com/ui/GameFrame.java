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
   ���崰�ڽ��棺չʾ��ɫ��
 */
public class GameFrame extends JFrame{
	// ��������:������Ҫһ�����������ġ�
	public Mario mario;
	// �ֱ���:ˮ�ܣ����,ש�飬Ģ��
	public Enemy pipe ,cionBrick , brick,mashroom,turtle;
	//����ͼƬ
	public BackgroundImage bg ;
	//����һ����������װ���˶���
	public ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	//����һ����������װ�ӵ�
	public ArrayList<Boom> boomList = new ArrayList<Boom>();

	//��ͼ���ݣ��ƶ�������1��שͷ����2����ң���3��ˮ��,4��Ģ��
	public int[][] map = null;
	{
		// ʵ��������г�ʼ����ͼ��Դ������
		Map mp = new Map();
		map = mp.readMap();
	}

	//���캯�������ʼ������ͼƬ������¶���
	public GameFrame() throws Exception {
		//��ʼ���������������Ϣ����
		// this�����˵�ǰ���������
		this.setSize(800,450);
		this.setTitle("Mario");
		this.setResizable(false);
		// ����չʾ����
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);

		// ������������
		mario = new Mario(this);

		// ��������ͼƬ
		bg = new BackgroundImage();

		// ��ȡ��ͼ�������õ�ͼ
		relodeMap();

		mario.start();

		//����һ���̸߳������Ĵ����ػ��߳�
		new Thread(){
			public void run(){
				while(true){
					//�ػ洰��
					repaint(); // �Զ�������ǰ�����е�paint����
					//����ӵ��Ƿ����
					checkBoom();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();


		//���ñ�������
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
				//��ȡ������1����שͷ
				if(map[i][j]==1){
					brick = new Brick(j*30,i*30,30,30,new ImageIcon("image/brick.png").getImage());
					enemyList.add(brick);
				}
				//����2�����ש��
				if(map[i][j]==2){
					cionBrick = new CoinBrick(j*30,i*30,30,30,new ImageIcon("image/coin_brick.png").getImage());
					enemyList.add(cionBrick);
				}
				//����3��ˮ��
				if(map[i][j]==3){
					pipe = new Pipe(j*30,i*30,60,120,new ImageIcon("image/pipe.png").getImage());
					enemyList.add(pipe);
				}
				//����4��Ģ��
				if (map[i][j]==4){
					mashroom = new Mashroom(j*30+5,i*30+10,20,20,new ImageIcon("image/mogu.png").getImage());
					enemyList.add(mashroom);
				}
				//����5���ڹ�
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
			//����˫���廭����ͼƬ�������
			BufferedImage bi = (BufferedImage) this.createImage(this.getSize().width, this.getSize().height);
			Graphics big = bi.getGraphics();

			big.drawImage(bg.img, bg.x, bg.y, null);


			// ��ʼ���ƽ����ϵĵ��ˡ�
			for (int i = 0; i < enemyList.size(); i++) {
				Enemy e = enemyList.get(i);
				big.drawImage(e.img, e.x, e.y, e.width, e.height, null);
			}


			//���ӵ�
			for (int i = 0; i < boomList.size(); i++) {
				Boom b = boomList.get(i);
				Color c = big.getColor();
				big.setColor(Color.yellow);
				big.fillOval(b.x += b.speed, b.y, b.width, b.width);
				big.setColor(c);
			}


			//������mario

			big.drawImage(mario.img, mario.x, mario.y, mario.width, mario.height, null);


			//�����������ǩ
			Color c = big.getColor();
			big.setColor(Color.black);
			big.drawString("Coins:" + mario.coinNum, 50, 50);
			big.setColor(c);

			//��������Ϣ
			if (mario.isDead) {
				c = big.getColor();
				big.setColor(Color.red);
				big.drawString("You are dead, Please press R", 300, 220);
				big.setColor(c);
			}

			g.drawImage(bi, 0, 0, null);
		}catch (Exception e){}

	}

	//����ӵ��Ƿ���磬��������������Ƴ�
	public void checkBoom(){
		for (int i = 0; i < boomList.size(); i++) {
			Boom b = boomList.get(i);
			if(b.x<0 || b.x>800){
				boomList.remove(i);
			}
		}
	}

}
