package com.screens.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BaseActor extends Actor {
    TextureRegion region;
    Rectangle boundary;

    public Texture getTexture() {
        return region.getTexture();
    }

    public BaseActor(Texture t) {
        super();
        region = new TextureRegion();
        boundary = new Rectangle();
        setTexture(t);
    }

    public void setTexture(Texture t) {
        int w = t.getWidth();
        int h = t.getHeight();
        setWidth( w );
        setHeight( h );
        region.setRegion( t );
    }
    Rectangle getBoundingRectangle() {
        boundary.set( getX(), getY(), getWidth(), getHeight() );
        return boundary;
    }
    public void act(float dt) {
        super.act( dt );
    }
    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if ( isVisible() )
            batch.draw( region, getX(), getY(), getOriginX(), getOriginY(),
                    getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation() );
    }
}