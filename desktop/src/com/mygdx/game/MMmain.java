package com.mygdx.game;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class MMmain {
    public static void main(String[] arg) {
        MMgame MM = new MMgame();

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1280;
        config.forceExit = false;
        // On créé une configuration pour l'application, et on la fait tourner en 720x1280
        // Solution temporaire, il faudrait demander à Yrax comment il a fait pour avoir
        // un truc qui tourne en 1920x1080 de manière modulaire

        LwjglApplication launcher = new LwjglApplication(MM, config);
    }
}
