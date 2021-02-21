package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;

public class HaikyuGame extends Game {
    public void create(){
        Menu hl = new Menu(this);
        setScreen(hl);
    }
}
