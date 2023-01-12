package com.almasb.fxglgames.pong;

public enum BulletDir {
    RIGHT(4), LEFT(3), UP(1), DOWN(2);

    private int value = 0;
    private BulletDir(int val) {
        value = val;
    }

    public int getValue() {
        return value;
    }

}