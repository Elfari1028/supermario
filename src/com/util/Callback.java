package com.util;

public interface Callback {
    default public void exit(){};
    default public void enter(){};
    default public void runLevel(int level, boolean isMultiplayer,boolean fromGame){};
}