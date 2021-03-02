package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;

import static com.tiles.pathfinding.NeededConstants.mouseInput;
import static com.tiles.pathfinding.NeededConstants.tileList;

public class Pawn implements Serializable {
    private String color; // La couleur du pion

    public String getColor() {
        return color;
    }

    public Case setCase; // La case sur laquelle est le pion
    private transient Sprite sprite;

    Pawn(String color) {
        this.color = color;
    }

    private float size;
    // Je le met parce que Compte me l'a demandé
    // mais je pense pas que ça vaille le coup vu qu'il est forcèment chargé ?

    private void setSize(float size) {
        this.size = size;
        sprite.setSize(size, 2 * size);
    }

    public void setSize() {
        setSize(setCase.caseSize() / 2);
    }
    // Fonctions classiques pour gérer la taille

    private boolean isMovable = false; // Même principe que pour la Queue

    public void load() { // Pour la sérialization
        sprite = new Sprite(new Texture("pions/" + color + ".png"));
        setSize();
        updateCoordinates();
    }

    public void draw() {
        sprite.draw(NeededConstants.batch);
    }

    public void updateCoordinates() {
        sprite.setX(setCase.getX(setCase.getRotatedCoordinates()[0]) + (setCase.caseSize() - sprite.getWidth()) / 2);
        sprite.setY(setCase.getY(setCase.getRotatedCoordinates()[1]) + setCase.caseSize() / 3);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }

    private boolean hasExplored = false; // Pour regarder si on a déjà fait le pathfinding et éviter de le faire trop de fois

    public void handleInput(Player player) {
        if (isMovable) {
            sprite.setX(mouseInput().x - sprite.getWidth() / 2);
            sprite.setY(mouseInput().y - sprite.getHeight() / 2);
            if (!hasExplored) { // On fait le pathfinding une seule fois
                setCase.show(); // On montre la case de départ
                setCase.explore(player); // On lance le pathfinding (structure récursive)
                hasExplored = true; // Et on montre qu'on a déjà fait le pathfinding
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Puis si on clique quelque part
                try {
                    Case nextCase = findCase(); // Est-on sur une case ?
                    if (nextCase.isValid) { // Si elle existe et est non nulle
                        setCase.revert(player); // On annule le pathfinding... avec un autre pathfinding
                        setCase.hide(); // On cache la case de départ
                        setCase = nextCase; // On change la case
                        hasExplored = false; // On réinitialise les variables booléennes
                        isMovable = false;
                        updateCoordinates(); // Et on met à jour les coordonées, qui sont entièrement calculées à partir de la case
                    }
                } catch (NullPointerException e) {
                    System.out.println("No Tile found in Pawn.handleInput");
                }
            }
        } else isMovable = (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                (sprite.getX() < mouseInput().x) && (mouseInput().x < sprite.getX() + sprite.getWidth() &&
                (sprite.getY() < mouseInput().y) && (mouseInput().y < sprite.getY() + sprite.getHeight())));
    }

    public Case findCase() {
        for (Tile tile : tileList) {
            if (tile.getX() <= mouseInput().x && mouseInput().x <= tile.getX() + tile.getSize() &&
                    tile.getY() <= mouseInput().y && mouseInput().y <= tile.getY() + tile.getSize()) {
                try {
                    return tile.getCase(mouseInput());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Clicked border of Tile n°" + tile.number + " in Pawn.findCase");
                }
            }
        }
        return null;
    }
}
