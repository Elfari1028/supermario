package com.ui;

import java.awt.Image;

import javax.swing.ImageIcon;

/*** 背景图片类
 * @author 刘坤昊
 * @version 1
 */
public class BackgroundImage {
	
	public BackgroundImage(String name){
		img = new ImageIcon(name).getImage();
	}
	public BackgroundImage(){};
	public int x=0,y=0;
	public Image img=new ImageIcon("image/startBack.jpg").getImage();
	static public Image getImage(String str) {return new ImageIcon(str).getImage();}

}
