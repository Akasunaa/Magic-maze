package com.mygdx.game.deux;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.mygdx.game.desktop.HaikyuGame;

public class Main {
    public static void main (String[] arg) {
        HaikyuGame yohoo = new HaikyuGame();
        LwjglApplication launcher = new LwjglApplication(yohoo);
    }
}