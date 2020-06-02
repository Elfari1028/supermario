package com.role;

import com.ui.GameFrame;

import java.awt.*;

/*
  子弹类
 */
public class Boom {
	// 子弹的坐标，大小，速度
	public int x, y;
	public int width;
	public int speed = 2;
	GameFrame gf;

	public Boom(int x, int y, int width, GameFrame gf) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.gf = gf;
	}

	public boolean hit() {

		Rectangle myrect = new Rectangle(this.x, this.y, this.width, this.width);

		Rectangle rect = null;

		// 检测是否碰到障碍物
		for (int i = 0; i < gf.enemyList.size(); i++) {
			Enemy enemy = gf.enemyList.get(i);
			if (enemy instanceof Bee)
				continue;
			if (enemy == null)
				return false;

			rect = new Rectangle(enemy.x, enemy.y, enemy.width, enemy.height);

			if (myrect.intersects(rect)) {
				return true;
			}
		}

		return false;
	}

}
