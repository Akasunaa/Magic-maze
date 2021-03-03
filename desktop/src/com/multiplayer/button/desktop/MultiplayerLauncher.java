package com.multiplayer.button.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import com.multiplayer.button.PushTheButton;
import com.multiplayer.button.PushTheButton;


import static com.multiplayer.button.NeededConstants.launchServer;
import static com.multiplayer.button.NeededConstants.stopServer;

public class MultiplayerLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.height = 600;
        config.width = 1000;
        config.forceExit = false;
        launchServer();
        new LwjglApplication(new PushTheButton(), config);
    }
}
