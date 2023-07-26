package com.mygdx.game.desktop;


import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.screens.MagicGame;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setWindowedMode(1920, 1080);
        try {
            System.setOut(new PrintStream(new FileOutputStream("log.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Lwjgl3Application launcher = new Lwjgl3Application(new MagicGame(), config);
    }
}