package com.util;

/*** 回调接口
 * @author 艾力帕尔
 * @version 5
 * 用于实现回调函数
 */
public interface Callback {
    default public void exit(){};
    default public void enter(){};
    default public void runLevel(int level, boolean isMultiplayer,boolean fromGame){};
}