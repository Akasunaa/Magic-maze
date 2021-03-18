package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.menu.BaseActor;
import com.utils.Colors;
import com.utils.Functions;

import java.io.Serializable;

import static com.utils.Functions.findCase;
import static com.utils.GameScreens.mainScreen;
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.*;


public class Pawn implements Serializable {
    private int color; // La couleur du pion

    public int getColor() {
        return color;
    }

    private boolean isLocked = false;
    // Un petit booléen qui permet d'éviter qu'on bouge les pions tant qu'on a pas mis une tuile à la sortie
    public void unlock() {isLocked = false;}

    public Case setCase; // La case sur laquelle est le pion
    private transient BaseActor sprite;

    public Pawn(int color) {
        this.color = color;
    }

    public void setCase(Case tempCase) {
        try {
            setCase.pawn = null;
        } catch (Exception e) {
            System.out.println("Error in Pawn.setCase(): Pawn hasn't got a Case yet");
        }
        setCase = tempCase;
        setCase.pawn = this;
        if (setCase.isExit && setCase.color == color) {
            queue.reveal();
            isLocked = true;
        }
    }

    public void setFirstCase() {
        for (Tile tile : tileList) {
            for (Case[] caseLine : tile.caseList) {
                for (Case tempCase : caseLine) {
                    if (tempCase.isAccessible && (this==tempCase.pawn || tempCase.pawn==null)) {
                        setCase(tempCase);
                        return;
                    }
                }
            }
        }
    }

    private float size;
    // Je le met parce que Compte me l'a demandé
    // mais je pense pas que ça vaille le coup vu qu'il est forcèment chargé ?

    private void setSize(float size) {
        this.size = size;
        sprite.setSize(size, 2 * size);
    }

    public void setSize() {
        setSize(caseSize / 1.5f);
    }
    // Fonctions classiques pour gérer la taille

    private boolean isMovable = false; // Même principe que pour la Queue

    public void load() { // Pour la sérialization
        sprite = new BaseActor();
        sprite.setTexture(new Texture("pions/" + Colors.getColor(color) + ".png"));
        mainScreen.getMainStage().addActor(sprite);
        setSize();
        updateCoordinates();
    }

    public void updateCoordinates() {
        sprite.setX(setCase.getX(setCase.x) + (caseSize - sprite.getWidth()) / 2);
        sprite.setY(setCase.getY(setCase.y) + caseSize / 3);
    }

    public void dispose() {
        sprite.remove();
    }

    private boolean hasExplored = false; // Pour regarder si on a déjà fait le pathfinding et éviter de le faire trop de fois

    public void handleInput(Player player) {
        if (isMovable) {
            player.takesPawn(this);
            sprite.setX(Functions.mouseInput().x - sprite.getWidth() / 2);
            sprite.setY(Functions.mouseInput().y - sprite.getHeight() / 2);
            if (!hasExplored) { // On fait le pathfinding une seule fois
                setCase.show(); // On montre la case de départ
                setCase.explore(player); // On lance le pathfinding (structure récursive)
                hasExplored = true; // Et on montre qu'on a déjà fait le pathfinding
            }
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Puis si on clique quelque part
                try {
                    Case nextCase = findCase(); // Est-on sur une case ?
                    if (!(nextCase == null) && nextCase.isValid) { // Si elle existe et est non nulle
                        setCase.revert(player); // On annule le pathfinding... avec un autre pathfinding
                        setCase.hide(); // On cache la case de départ
                        setCase(nextCase); // On change la case
                        hasExplored = false; // On réinitialise les variables booléennes
                        isMovable = false;
                        updateCoordinates(); // Et on met à jour les coordonées, qui sont entièrement calculées à partir de la case
                        player.dropsPawn(this);
                    }
                } catch (NullPointerException e) {
                    System.out.println("No Tile found in Pawn.handleInput");
                }
            }
        } else isMovable = (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                (sprite.getX() < Functions.mouseInput().x) && (Functions.mouseInput().x < sprite.getX() + sprite.getWidth() &&
                (sprite.getY() < Functions.mouseInput().y) && (Functions.mouseInput().y < sprite.getY() + sprite.getHeight())) &&
                player.pawn == null && !isLocked);
    }


}
