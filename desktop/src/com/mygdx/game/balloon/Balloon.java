package com.mygdx.game.balloon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class Balloon extends BBaseActor {
    public float speed;
    public float amplitude;
    public float oscillation;
    public float initialY;
    public float time;
    public int offset;

    public Balloon(){
        speed = 800 * MathUtils.random(0.5f,2.0f);
        amplitude = 50 * MathUtils.random(0.5f,2.0f);
        oscillation =  0.01f * MathUtils.random(0.5f,2.0f);
        initialY = 120 * MathUtils.random(0.5f,2.0f);
        time = 0;
        offset = -100;
        setTexture( new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\goal1.png")));
        setX(offset);
    }

    public void act(float dt){
        super.act(dt);
        time += dt;

        float xPos = speed * time + offset;
        float yPos = amplitude*MathUtils.sin(oscillation*xPos) + initialY;
        setPosition(xPos, yPos);
    }
}
