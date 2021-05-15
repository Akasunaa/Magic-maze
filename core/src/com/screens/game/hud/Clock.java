package com.screens.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.screens.endings.DefeatScreen;
import com.screens.MagicGame;

public class Clock extends Label {
    public static Clock clock;
    float time;
    float baseTime = 900f;

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
        super.setText(""+time);
        // sans doute pas la meilleure manière de convertir un float en text mais bon
    }


    public void reset() {
        time = baseTime;
    }
}

