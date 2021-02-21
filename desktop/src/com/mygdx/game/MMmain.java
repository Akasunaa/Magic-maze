package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class MMmain {
    public static void main(String[] arg) {
        MMgame MM = new MMgame();
        LwjglApplication launcher = new LwjglApplication(MM);
    }
}
