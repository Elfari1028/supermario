package com.util;

import com.role.Boom;
import com.ui.UIFrame;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;

public class UIFrameKeyListener extends KeyAdapter {

    // 接收到了当前主界面：游戏界面
    public UIFrame gf;
    public Callback callback;
    public boolean isStart;

    public UIFrameKeyListener(UIFrame gf, Callback callback, boolean isStart) {
        this.gf = gf;
        this.isStart = isStart;
        this.callback = callback;
    }

    // 键盘监听
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();
        if (isStart) {
            if (code == 0x1B) {
                gf.removeKeyListener(this);
                callback.exit();
            } else
                callback.enter();
            return;
        } else
            switch (code) {
                case 65: // A
                    callback.runLevel(0, false,false);
                    break;
                case 81:// Q
                    callback.runLevel(1, false,false);
                    break;
                case 87: // W
                    callback.runLevel(2, false,false);
                    break;
                case 69: // E
                    callback.runLevel(3, false,false);
                    break;
                case 66:// B
                    callback.runLevel(1, true,false);
                    break;
                case 78: // N
                    callback.runLevel(2, true,false);
                    break;
                case 77: // M
                    callback.runLevel(3, true,false);
                    break;
                case 0x1B:
                    gf.removeKeyListener(this);
                    callback.exit();
            }
    }

    // 键盘释放监听
    public void keyReleased(KeyEvent e) { }

}