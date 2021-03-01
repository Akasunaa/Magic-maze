package com.mygdx.game;

//on crée la classe qu'on va utiliser pour décrire tous les objets de l'interface, y a des trucs qui servent a rien vu que c'est du copier coller du code du bouquin

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class IBaseActor extends Actor {
    public TextureRegion region;
    public Rectangle boundary;
    public float velocityX;
    public float velocityY;

    //constructeur

    public IBaseActor() {
        super();
        region = new TextureRegion();
        boundary = new Rectangle();
        velocityX = 0;
        velocityY = 0;
    }

    // on attribue la texture à l'actor, càd qu'on attribue l'image à la variable

    public void setTexture(Texture t) {
        int w = t.getWidth();
        int h = t.getHeight();
        setWidth(w);
        setHeight(h);
        region.setRegion(t);
    }

    //et le reste sert à rien là

    public Rectangle getBoundingRectangle() {
        boundary.set(getX(), getY(), getWidth(), getHeight());
        return boundary;
    }

    public void act(float dt) {
        super.act(dt);
        moveBy(velocityX * dt, velocityY * dt);
    }

    public void draw(Batch batch, float parentAlpha) {
        Color c = getColor();
        batch.setColor(c.r, c.g, c.b, c.a);
        if (isVisible()) {
            batch.draw(region, getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
        }
    }
}
