package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class BigRedButtonJava {
    private float x;
    private float y;
    private float width;
    private float height;

    private int counter;

    private Sprite idle;
    private Sprite pushed;
    private Sprite active;

    BigRedButtonJava(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        idle = new Sprite(new Texture("redButtonIdle.png"));
        pushed = new Sprite(new Texture("redButtonPushed.png"));
        active = idle;
        updateSprite();
    }

    private void updateSprite() {
        idle.setPosition(x,y);
        pushed.setPosition(x,y);
        idle.setSize(width, height);
        pushed.setSize(width, height);
    }
    public void update(SpriteBatch batch) {active.draw(batch);}

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateSprite();
    }
    public void translateX(int x) {
        this.x += x;
        updateSprite();
    }
    public void translateY(int y) {
        this.y += y;
        updateSprite();
    }
    public String stringPosition() {
        return "x = " + x + ";y = " + y;
    }

    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public void isClicked(float iX, float iY) {
        if (x <= iX && iX <= x + width) {
            if (y <= iY && iY <= y + height) {
                System.out.println("Button Pressed !");
                active = pushed;
                counter++;
            }
        }
    }

    public Boolean isClickable() {
        if (counter > 30) {
            counter = 0;
            active = idle;
        }
        if (counter == 0) return true;
        else {
            counter ++;
            return false;
        }
    }

    public void dispose() {
        //idle.texture.dispose();
        //pushed.texture.dispose();
    }

}
