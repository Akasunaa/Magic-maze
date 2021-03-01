package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Pawn implements Serializable {
    private String color; // La couleur du pion
    public String getColor(){ return color;}
    public Case setCase; // La case sur laquelle est le pion
    private transient Sprite sprite;
    Pawn(String color) {
        this.color = color;
    }
    private void setSize(float size) {
        sprite.setSize(size,2*size);
    }
    public void setSize() {
        setSize(setCase.tileSize()/2);
    }
    public void load() {
        sprite = new Sprite(new Texture("pions/"+color+".png"));
        setSize();
        updateCoordinates();
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }
    public void updateCoordinates() {
        sprite.setX(setCase.getX(setCase.getRotatedCoordinates()[0])+(setCase.tileSize()-sprite.getWidth())/2);
        sprite.setY(setCase.getY(setCase.getRotatedCoordinates()[1])+setCase.tileSize()/3);
    }
    public void dispose() {
        sprite.getTexture().dispose();
    }
}
