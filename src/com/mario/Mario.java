package com.mario;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

import com.role.*;
import com.ui.GameFrame;

//�Լ��Ľ�ɫ��
public class Mario {
	
	public GameFrame gf;

	//��ͼ�����ұ߽�
	public int mapLeft=0,mapRight=2000;
	//�Ƿ����jump
	public boolean jumpFlag=true;
	//�������ٶ�
	public double g=0.15;
	//����µ�����
	public int x=180,y=200;
	//����µ��ٶ�
	public double xspeed=5 , yspeed=6;
	//����µĿ��
	public int width=25,height=30;
	//����µ�ͼƬ
	public Image img = new ImageIcon("image/mari1.png").getImage();
	//����³����ǳ���
	public boolean isFaceRight=true;
	//������Ƿ���
	public boolean isBig=false;
	//����µĽ������
	public int coinNum = 0;
	//������Ƿ�����
	public boolean isDead = false;
	//������Ƿ�ͨ��
	public boolean isWin = false;
	
	public boolean left=false,right=false,down=false,up=false;
	
	public String Dir_Up="Up",Dir_Left="Left",Dir_Right="Right",Dir_Down="Down";
	
	
	public Mario (GameFrame gf) {
		this.gf = gf;
		this.gravity();
	}
	//����
	public void  revive(){
		isWin=false;
		isDead=false;
		coinNum=0;
		jumpFlag=true;
		isBig=false;
		width=25;
		height=30;
		this.x=200;
		this.y=180;
		//������λ
		gf.bg.x=0;
		//���¼��ص�ͼ
		gf.relodeMap();
	}

	// �����ƶ����߼�������
	public void run(){
		new Thread(){
			public void run(){
				while(true){
					if (isDead||isWin){
						try {
							this.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						continue;
					}
					//������
					if(left){
						isFaceRight=false;
						//��ײ����
						if(hit(Dir_Left)){
							xspeed=0;
						}

						//�����������ƶ�
						if(x>=200){
							x-=xspeed;
							img=new ImageIcon("image/mari_left.gif").getImage();
						}

						if(x<200){
							if (gf.bg.x<mapLeft) {
								//���������ƶ�
								gf.bg.x += xspeed;
								//�ϰ��������ƶ�
								for (int i = 0; i < gf.enemyList.size(); i++) {
									Enemy enemy = gf.enemyList.get(i);
									enemy.x += xspeed;
								}
								//�ӵ������ƶ�
								for (int i=0;i<gf.boomList.size();i++){
									Boom b=gf.boomList.get(i);
									b.x+=xspeed;
								}
								img = new ImageIcon("image/mari_left.gif").getImage();
							}else{
								if (x>=0) {
									x -= xspeed;
									img = new ImageIcon("image/mari_left.gif").getImage();
								}
							}
						}
						xspeed=5;
					}

					//������
					else if(right){
						isFaceRight=true;
						// �ұ���ײ����Ӧ���������ߵ�ʱ����
						// ������ײ��⣺�������ǣ���������ײ�
						if(hit(Dir_Right)){
							xspeed=0;
						}

						//�����������ƶ�
						if(x<550){
							x += xspeed;
							img=new ImageIcon("image/mari_right.gif").getImage();
						}

						if(x>=550){
							if ((800-gf.bg.x)<mapRight) {
								//���������ƶ�
								gf.bg.x -= xspeed;
								//�ϰ��������ƶ�
								for (int i = 0; i < gf.enemyList.size(); i++) {
									Enemy enemy = gf.enemyList.get(i);
									enemy.x -= xspeed;
								}
								//�ӵ������ƶ�
								for (int i=0;i<gf.boomList.size();i++){
									Boom b=gf.boomList.get(i);
									b.x-=xspeed;
								}
								img = new ImageIcon("image/mari_right.gif").getImage();
							}else{
								if (x<=770){
									x += xspeed;
									img=new ImageIcon("image/mari_right.gif").getImage();
								}
							}
						}
						xspeed=5;
					}else {//��û�������ߣ���û������
						hit(Dir_Right);
						hit(Dir_Left);
					}

					//������
					if(up){

						if(jumpFlag){
							jumpFlag=false;
							//Ϊ��ʵ���ڿ���Ҳ�������ƶ�
							new Thread(){
								public void run(){
									jump(yspeed);
									jumpFlag=true;
								}
							}.start();
						}

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
	
	//�������ĺ���
	public void jump(double speed){

		for (int i = 0; i < 1000; i++) {
			//��ײ���һ��Ҫ���ƶ�֮ǰ
			if(hit(Dir_Up))break;
			//ģ���������ٶ�
			speed=((speed-g)<0?0:(speed-g));

			y-=speed;
			if(hit(Dir_Up))break;
			if (speed<=0)break;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		//Ϊ�˹��췴��Ч��������speed����ײ֮����ȵ�����
		for (int i = 0; i <1000; i++) {
			if (y>600){
				isDead=true;
				break;
			}
			if(hit(Dir_Down))break;
			speed+=g;

			speed=speed>5?5:speed;
			y+=speed;
			if(hit(Dir_Down))break;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}

	//�����ײ
	public boolean hit(String dir){

		Rectangle myrect = new Rectangle(this.x,this.y,this.width,this.height);

		Rectangle rect =null;
		//����Ƿ�����BeeBoom
		for (int i=0;i<gf.boomList.size();i++){
			Boom b=gf.boomList.get(i);
			if (b instanceof Beeboom){
				Beeboom bb = (Beeboom)b;
				rect = new Rectangle(bb.x,bb.y,bb.width,bb.width);

				//������������BeeBoom����
				if (myrect.intersects(rect)&&!isDead){
					gf.boomList.remove(bb);
					isDead=true;
					deadMove(2);
					return true;
				}
			}
		}

		//����Ƿ������ϰ���
		for (int i = 0; i < gf.enemyList.size(); i++) {
			Enemy enemy = gf.enemyList.get(i);
			if (enemy==null) return false;
			if(dir.equals("Left")){
				rect = new Rectangle(enemy.x+5,enemy.y,enemy.width,enemy.height);
			}else if(dir.equals("Right")){
				rect = new Rectangle(enemy.x-5,enemy.y,enemy.width,enemy.height);
			}else if(dir.equals("Up")){
				rect = new Rectangle(enemy.x,enemy.y+5,enemy.width,enemy.height);
			}else if(dir.equals("Down")){
				rect = new Rectangle(enemy.x,enemy.y-5,enemy.width,enemy.height);
			}
			//��ײ���
			if(myrect.intersects(rect)){
				//��������Ģ������һ�α�󣬵ڶ��α�С
				if (enemy instanceof Mashroom){
					if (isBig){
						gf.enemyList.remove(enemy);
						isBig=false;
						width=25;
						height=30;
					}else {
						gf.enemyList.remove(enemy);
						isBig=true;
						y-=6;
						width=30;
						height=36;
					}
				}
				//�������ǽ��ש��
				else if (dir.equals("Up")&&enemy instanceof CoinBrick){
					if (!((CoinBrick) enemy).isOpen) {
						((CoinBrick) enemy).isOpen=true;
						enemy.setImg(new ImageIcon("image/coin_brick_null.png").getImage());
						Coin coin = new Coin(enemy.x+4,enemy.y-10,22,30,new ImageIcon("image/coin.png").getImage());
						gf.enemyList.add(coin);

						for (int j=0;j<10;j++){
							try {
								Thread.sleep(10);
							}catch (InterruptedException e) {
								e.printStackTrace();
							}
							coin.y-=2;
						}
					}
				}
				//�������
				else if (enemy instanceof Coin){
					gf.enemyList.remove(enemy);
					coinNum++;
				}
				//�����������ڹ꣬�ڹ��ǻ�ģ������Ҳ�ǻ�ģ�����²�����
				else if ((!isDead)&&enemy instanceof Turtle&&(dir.equals(Dir_Left)||dir.equals(Dir_Right))&&!((Turtle) enemy).isDead){
					isDead=true;
					deadMove(2);
				}
				//���Ϸ������ڹ꣬�ڹ��ǻ�ģ������Ҳ�ǻ�ģ��ڹ������
				else if ((!isDead)&&enemy instanceof Turtle&&dir.equals(Dir_Down)&&!((Turtle) enemy).isDead){
					((Turtle) enemy).isDead=true;
					((Turtle) enemy).deadMove(2);
				}
				//�������������ش̣����۴��Ǹ����򣬾�����
				else if((!isDead)&&enemy instanceof Trap){
					isDead=true;
					deadMove(2);
				}
				//����������������۷䣬�����Ǹ�������
				else if((!isDead)&&enemy instanceof Bee&&((Bee)enemy).life>0){
					isDead=true;
					deadMove(2);
				}
				//������������flag��ʤ��
				else if((!isDead)&&enemy instanceof Flag){
					isWin=true;
				}

				return true;
			}
		}
		
		return false;
	}

	//�������������
	public void deadMove(double speed){
		//����ײ���
		for (int i = 0; i < 1000; i++) {
			//ģ���������ٶ�
			speed=((speed-g)<0?0:(speed-g));

			y-=speed;
			if (speed<=0)break;
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		for (int i = 0; i <1000; i++) {
			speed+=g;

			speed=speed>5?5:speed;
			y+=speed;
			if (y>450)break;
			if (!isDead)break;

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	//�����߳�
	public void gravity(){
			new Thread(){
				public void run(){
					
					while(true){
						try {
							sleep(10);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						

						while(true){
							if(!jumpFlag){
								break;
							}
							
							if(hit(Dir_Down)){
								break;
							}


							double speed = 0;
							while (true){
								if (y>600){
									isDead=true;
									break;
								}
								if(!jumpFlag){
									break;
								}
								speed += g;
								speed=speed>5?5:speed;
								y += speed;
								if (hit(Dir_Down)) break;
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}


							try {
								sleep(10);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}.start();
	
	}

	//����ӵ�
	public void addBoom() {
		Boom b = new Boom(x,y+5,10,gf);
		b.speed = isFaceRight?4:-4;
		gf.boomList.add(b);
	}

}
