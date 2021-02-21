package com.mygdx.game.balloon;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

public class BalloonMain {
    public static void main (String[] arg){
        BGame jeux = new BGame();
        LwjglApplication launcher = new LwjglApplication(jeux);
    }
}
