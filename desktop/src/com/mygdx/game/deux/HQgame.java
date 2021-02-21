package com.mygdx.game.deux;

import com.badlogic.gdx.Game;

public class HQgame extends Game {
    public void create(){
        Menu2 hl = new Menu2(this);
        setScreen(hl);
    }
}
