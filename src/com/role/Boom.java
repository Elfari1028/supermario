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

}
