package com.screens.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.screens.MagicGame;
import com.screens.endings.DefeatScreen;

import static com.screens.GameScreens.gameScreen;

public class Clock extends Label {
    public static Clock clock;
    float time;
    final static float baseTime = 120f;

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isPaused) time -= delta;
    }

    private boolean isPaused = false;

    public void pause() {
        isPaused = true;
        gameScreen.getInterface().unmute();
    }

    public void unpause() {
        isPaused = false;
        gameScreen.getInterface().mute();
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

    public Clock(Skin skin) {
        super(" ", skin, "clockStyle");
        time = baseTime;
        super.setText(String.valueOf(time));
        setAlignment(Align.center);
        setOrigin(getWidth() / 2f, getHeight() / 2f);
        setPosition(1920 / 2f, 1080 - getHeight() - 50);
        // sans doute pas la meilleure manière de convertir un float en text mais bon
    }


    public void reset() {
        time = baseTime;
    }
}

