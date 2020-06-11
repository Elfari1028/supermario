package com.ui;

import java.awt.*;

import com.util.Callback;
import com.util.KeyListener;

import com.util.MusicUtil;
import com.util.UIFrameKeyListener;

public class NavigatorFrame {
    public static NavigatorFrame instance = new NavigatorFrame();

    GameFrame gameFrame;
    StartPageFrame startFrame;
    LevelPageFrame levelFrame;

    // 音乐线程
    public Thread musicThread;

    private NavigatorFrame() {
        initStart();
    }

    public void start() {
        startFrame.start();
    }

    void initStart() {
        try {
            startFrame = new StartPageFrame("background_map.txt");
            startFrame.addKeyListener(this.StartPageListener(startFrame));
            // 设置背景音乐
            this.musicThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    MusicUtil.playBackground();
                    try {
                        while (!Thread.currentThread().isInterrupted()) {
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {

                    } finally {
                        MusicUtil.stop();
                    }
                }
            });
            this.musicThread.start();
        } catch (Exception e) {
        }
    }

    void initLevel() {
        try {
            levelFrame = new LevelPageFrame("background_map.txt");
            levelFrame.addKeyListener(this.LevelPageListener(levelFrame));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    UIFrameKeyListener StartPageListener(StartPageFrame frame) {
        return new UIFrameKeyListener(frame, new Callback() {
            public void enter() {
                startFrame.setVisible(false);
                startFrame.dispose();
                startFrame = null;
                initLevel();
                levelFrame.start();
            }
        }, true);
    }

    UIFrameKeyListener LevelPageListener(LevelPageFrame frame) {
        return new UIFrameKeyListener(frame, new Callback() {
            public void runLevel(int level, boolean isMultiplayer, boolean fromGame) {
                runNewLevel(level, isMultiplayer, fromGame);
            }
        }, false);
    }

    void runNewLevel(int level, boolean isMultiplayer, boolean fromGame) {
        System.out.println(level);
        if (!fromGame) {
            levelFrame.setVisible(false);
            levelFrame.dispose();
            levelFrame = null;
        }
        if (gameFrame != null) {
            gameFrame.setVisible(false);
            gameFrame.dispose();
            gameFrame = null;
        }
        try {
            gameFrame = new GameFrame(level, "level" + Integer.toString(level) + "_map.txt", isMultiplayer);
            System.out.println("r" + gameFrame.level);
            gameFrame.addKeyListener(getKeyListener(gameFrame));
        } catch (Exception e) {
            e.printStackTrace();
        }
        gameFrame.start();
    }

    public KeyListener getKeyListener(GameFrame frame) {
        System.out.println("q" + frame.level);
        return new KeyListener(frame, new Callback() {
            public void runLevel(int level, boolean isMultiplayer, boolean fromGame) {
                runNewLevel(level, isMultiplayer, fromGame);
            }

            public void exit() {
                onGameEsc();
            }
        });
    }

    public void onGameEsc() {
        gameFrame.setVisible(false);
        gameFrame.dispose();
        gameFrame = null;
        initLevel();
        levelFrame.start();
    }
}

@SuppressWarnings("serial")
class StartPageFrame extends UIFrame {

    public StartPageFrame(String mapName) throws Exception {
        super(mapName);
        this.setName("Simple Mario");
        this.setTitle("Simple Mario");
    }

    @Override
    public void addPainter(Graphics drawer) {
        drawer.drawImage(BackgroundImage.getImage("image/start.png"), 150, 40, null);
    }

}

@SuppressWarnings("serial")
class LevelPageFrame extends UIFrame {

    public LevelPageFrame(String mapName) throws Exception {
        super(mapName);
        this.setName("Simple Mario");
        this.setTitle("Simple Mario");
    }

    @Override
    public void addPainter(Graphics drawer) {
        drawer.drawImage(BackgroundImage.getImage("image/level_choose.png"), 80, 50, null);
    }

}