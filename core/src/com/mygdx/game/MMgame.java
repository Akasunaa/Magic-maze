package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MMgame extends Game {
    public void create() {
        MMinterface ui = new MMinterface(this);
        setScreen(ui);
    }
}
