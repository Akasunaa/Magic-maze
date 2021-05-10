package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.menu.BaseActor;
import com.utils.Colors;
import com.utils.Functions;
import com.utils.Multiplayer;

import java.io.Serializable;

import static com.utils.Functions.findCase;
import static com.utils.GameScreens.mainScreen;
import static com.utils.TileAndCases.*;


public class Pawn implements Serializable {
    private final int color; // La couleur du pion
    public Player player = null;

    public boolean hasWeapon = false;
    //Booléen qui indique si le pion a récupéré son arme

    public int getColor() {
        return color;
    }

    private boolean isLocked = false;

    // Un petit booléen qui permet d'éviter qu'on bouge les pions tant qu'on a pas mis une tuile à la sortie
    public void unlock() {
        isLocked = false;
    }

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
            isLocked = !queue.isEmpty;
        }

        hasWeapon = setCase.hasWeapon && setCase.color == color;
    }

    public void setFirstCase() {
        for (Tile tile : tileList) {
            for (Case[] caseLine : tile.caseList) {
                for (Case tempCase : caseLine) {
                    if (tempCase.isAccessible && (this == tempCase.pawn || tempCase.pawn == null)) {
                        setCase(tempCase);
                        return;
                    }
                }
            }
        }
    }

    private float size;
    public float getSize() {
        return size;
    }
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

    public void setSpritePosition(Vector2 target) {
        sprite.setX(target.x);
        sprite.setY(target.y);
        position.x = target.x;
        position.y = target.y;
    }

    private static final Vector2 target = new Vector2(0,0);
    private static final Vector2 position = new Vector2(0,0);
    public boolean hasTarget= false;
    public float speed = 0;
    public void setTarget(float x, float y) {
        target.x = x;
        target.y = y;
        hasTarget = true;
    }
    public void interpolate(float alpha, Interpolation interpolation) {
        Vector2 temp = position.interpolate(target, alpha, interpolation);
        setSpritePosition(temp);
    }
    private boolean firstTime = true;
    public void sendToTarget() {
        if (firstTime) {firstTime = false; return;}
        setSpritePosition(target);
        hasTarget = false;
    }
    public void updateCoordinates() {
        sprite.setX(setCase.getX(setCase.x) + (caseSize - sprite.getWidth()) / 2);
        sprite.setY(setCase.getY(setCase.y) + caseSize / 3);
    }

    public void dispose() {
        sprite.remove();
    }

    private boolean hasExplored = false; // Pour regarder si on a déjà fait le pathfinding et éviter de le faire trop de fois

    public Vector2 getPosition() {
        return position;
    }

    public boolean canPlaceHere(Vector2 coordinates, Player player) {
        try {
            Multiplayer.courrier.sendMessage("wantToPlacePawn " + Colors.getColor(color));
            Case nextCase = findCase(coordinates); // Est-on sur une case ?
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!(nextCase == null) && nextCase.isValid && Multiplayer.courrier.getAnswer()) { // Si elle existe et est non nulle
                setCase.revert(player); // On annule le pathfinding... avec un autre pathfinding
                setCase.hide(); // On cache la case de départ
                hasExplored = false; // On réinitialise les variables booléennes
                isMovable = false;
                player.dropsPawn(this);
                return true;
            }
        } catch (NullPointerException e) {
            System.out.println("No Tile found in Pawn.handleInput");
        }
        return false;
    }
    private int count = 2;

    public void place(Vector2 coordinates) {
        try {
            Case nextCase = findCase(coordinates); // Est-on sur une case ?
            setCase(nextCase); // On change la case
            updateCoordinates(); // Et on met à jour les coordonées, qui sont entièrement calculées à partir de la case
        } catch (NullPointerException e) {
            System.out.println("No Tile found in Pawn.handleInput");
        }
    }

    public void handleInput(Player player) {
        if (isMovable) {
            if (!hasExplored) { // On fait le pathfinding une seule fois
                setCase.show(); // On montre la case de départ
                player.takesPawn(this);
                setCase.explore(player); // On lance le pathfinding (structure récursive)
                hasExplored = true; // Et on montre qu'on a déjà fait le pathfinding
            }
            float x = Functions.mouseInput().x - sprite.getWidth() / 2;
            float y = Functions.mouseInput().y - sprite.getHeight() / 2;
            sprite.setX(x);
            sprite.setY(y);
            if (count == 2) { // On veut pas avoir à le faire trop souvent
                Multiplayer.courrier.sendMessage("movingPawn " + Colors.getColor(color) + " " + x + " " + y);
                count = 0;
            }
            count++;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Puis si on clique quelque part
                if (canPlaceHere(Functions.mouseInput(), player)) {
                    place(Functions.mouseInput());
                    player.dropsPawn(this);
                }
            }
        } else isMovable = (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                (sprite.getX() < Functions.mouseInput().x) && (Functions.mouseInput().x < sprite.getX() + sprite.getWidth() &&
                (sprite.getY() < Functions.mouseInput().y) && (Functions.mouseInput().y < sprite.getY() + sprite.getHeight())) &&
                !isLocked &&
                checkServerForClickable());
    }

    private boolean checkServerForClickable() {
        Multiplayer.courrier.sendMessage("wantToTakePawn " + Colors.getColor(color));
        try {
            Multiplayer.cyclicBarrier.await();
            // Pour synchroniser les threads
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Multiplayer.courrier.getAnswer();
    }


}
