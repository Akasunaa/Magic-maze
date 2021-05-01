package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.menu.MagicGame;
import com.utils.Multiplayer;

public class DesktopLauncher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1280;
        config.forceExit = false;
        LwjglApplication launcher = new LwjglApplication( new MagicGame() , config);
    }
}