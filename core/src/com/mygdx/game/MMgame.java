package com.mygdx.game;
//ça c'est juste pour créer le jeux
import com.badlogic.gdx.Game;

public class MMgame extends Game {
    public void create() {
        MMinterface ui = new MMinterface(this);
        setScreen(ui);
    }
}
