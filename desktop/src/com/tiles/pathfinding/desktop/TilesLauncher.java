package com.tiles.pathfinding.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tiles.pathfinding.FindThePath;


public class TilesLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 720;
        config.width = 1280;
        config.forceExit = false;
        new LwjglApplication(new FindThePath(), config);
    }
}
