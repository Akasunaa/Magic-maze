package com.mygdx.game.balloon;

import com.badlogic.gdx.Game;

public class BGame extends Game {
    public void create(){
        BalloonLevel v = new BalloonLevel(this);
        setScreen(v);
    }
}
