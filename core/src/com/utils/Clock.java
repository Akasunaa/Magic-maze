package com.utils;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.menu.BaseGame;
import com.menu.DefeatScreen;
import com.menu.MagicGame;

public class Clock extends Label {
    public static Clock clock;
    float time;
    float baseTime = 90f;

    @Override
    public void act(float delta) {
        super.act(delta);
        time -= delta;
    }

    public void update(MagicGame game) {

        setText("Time: " + (int) time);

        // Check if timer reached 0
        if (time < 0) {
            time = baseTime;
            // Parce que comme ça au moins je suis sûr qu'il va pas me faire une saloperie
            game.setScreen(new DefeatScreen(game));

        }
    }

    public Clock(LabelStyle style) {
        super(" ", style);
        time = baseTime;
        super.setText("" + time);
    }


    public void reset() {
        time = baseTime;
    }
};

