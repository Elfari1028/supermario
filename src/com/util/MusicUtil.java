package com.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

/*** 音乐播放类
 * @author 艾力帕尔
 * @version 1
 * 用于播放背景音乐
 */
public class MusicUtil {
    private static Clip clip;
    static {
        File bgMusicFile = new File("music/bg.wav");
        try {
            AudioInputStream audioInputStream =
                    AudioSystem.getAudioInputStream(bgMusicFile);
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 播放
    public static void playBackground() {
        //循环播放
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    
    public static void stop() {
        clip.close();
    }
}
