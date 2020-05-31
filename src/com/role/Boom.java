package com.role;

import com.ui.GameFrame;

import java.awt.*;

/*
  �ӵ���
 */
public class Boom {
	//�ӵ������꣬��С���ٶ�
	public int x,y;
	public int width;
	public int speed=2;
	GameFrame gf;
	
	public Boom(int x, int y, int width,GameFrame gf) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.gf = gf;
	}

	public boolean hit(String dir){

		Rectangle myrect = new Rectangle(this.x,this.y,this.width,this.width);

		Rectangle rect =null;

		//����Ƿ������ϰ���
		for (int i = 0; i < gf.enemyList.size(); i++) {
			Enemy enemy = gf.enemyList.get(i);
			if (enemy.getClass()==Bee.class)continue;
			if (enemy==null) return false;
			if(dir.equals("Left")){
				rect = new Rectangle(enemy.x+5,enemy.y,enemy.width,enemy.height);
			}else if(dir.equals("Right")){
				rect = new Rectangle(enemy.x-5,enemy.y,enemy.width,enemy.height);
			}

			if(myrect.intersects(rect)){
				return true;
			}
		}

		return false;
	}

}
