package com.util;

import com.role.Boom;
import com.ui.GameFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

//键盘按下监听类
public class KeyListener extends KeyAdapter {

	// 接收到了当前主界面：游戏界面
	public GameFrame gf;
	public Callback callback;

	public KeyListener(GameFrame gf, Callback escCallback) {
		this.gf = gf;
		this.callback = escCallback;
	}

	// 键盘监听
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			// 向右走
			case 39:
				gf.mario.right = true; // 信号位
				break;
			// 向左走
			case 37:
				gf.mario.left = true;
				break;
			case 66: // B 单人模式加Mario子弹 对人为Luigi
				if (!gf.mario.isDead && !gf.isMultiplayer)
					gf.mario.addBoom();
				else if (gf.isMultiplayer && !gf.luigi.isDead)
					gf.luigi.addBoom();
				break;
			case 77:
				if (gf.isMultiplayer && !gf.mario.isDead)
					gf.mario.addBoom();
				break;
				// 向上跳
			case 38:
				gf.mario.up = true;
				break;
			case 82:
				gf.mario.revive();
				if (gf.isMultiplayer)
					gf.luigi.revive();
				break;
			case 87: // W
				if (gf.isMultiplayer)
					gf.luigi.up = true;
				break;
			case 65: // A
				if (gf.isMultiplayer)
					gf.luigi.left = true;
				break;
			case 68: // D
				if (gf.isMultiplayer)
					gf.luigi.right = true;
				break;
			case 0x1B:
				gf.removeKeyListener(this);
				callback.execute();
		}
	}

	// 键盘释放监听
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		switch (code) {
			case 39:
				gf.mario.right = false;
				gf.mario.img = new ImageIcon("image/mari1.png").getImage();
				break;
			case 37:
				gf.mario.left = false;
				gf.mario.img = new ImageIcon("image/mari_left1.png").getImage();
				break;
			case 38:
				gf.mario.up = false;
				break;
			case 87: // W
				if (!gf.isMultiplayer)
					break;
				gf.luigi.up = false;
				break;
			case 65:// A
				if (!gf.isMultiplayer)
					break;
				gf.luigi.left = false;
				gf.luigi.img = new ImageIcon("image/mari_left1.png").getImage();
				break;
			case 68: // D
				if (!gf.isMultiplayer)
					break;
				gf.luigi.right = false;
				gf.luigi.img = new ImageIcon("image/mari1.png").getImage();
				break;

		}
	}

}
