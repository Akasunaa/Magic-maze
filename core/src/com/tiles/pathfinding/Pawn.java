package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;
import java.util.ArrayList;

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

    private boolean isMovable = false;

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

    private boolean hasExplored = false;
    public void handleInput(Player player, float mouseX, float mouseY, ArrayList<Tile> tileList) {
        if (isMovable) {
            sprite.setX(mouseX - sprite.getWidth() / 2);
            sprite.setY(mouseY - sprite.getHeight() / 2);
            if (!hasExplored) {
                setCase.show();
                setCase.explore(player);
                hasExplored = true;
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                try {
                    Case nextCase = findCase(mouseX,mouseY, tileList);
                    if (nextCase.isValid) {
                        setCase.revert(player);
                        setCase.hide();
                        setCase = nextCase;
                        hasExplored = false;
                        isMovable = false;
                        updateCoordinates();
                    }
                } catch( NullPointerException e) {

                }
            }
        }
        else isMovable =(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                        (sprite.getX() < mouseX) && (mouseX < sprite.getX() + sprite.getWidth() &&
                        (sprite.getY() < mouseY) && (mouseY < sprite.getY() + sprite.getHeight())));
    }

    public Case findCase(float x, float y, ArrayList<Tile> tileList) {
        for (Tile tile : tileList) {
            if (tile.getX() <= x && x <= tile.getX() + tile.getWidth() || tile.getY() <= y && y <= tile.getY() + tile.getHeight()) {
                return tile.getCase(x,y);
            }
        }
        return null;
    }
}
