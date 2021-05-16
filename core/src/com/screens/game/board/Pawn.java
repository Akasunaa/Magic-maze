package com.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.screens.game.BaseActor;
import com.screens.game.hud.Clock;
import com.multiplayer.messages.pawn.AskPlacePawn;
import com.multiplayer.messages.pawn.AskTakePawn;
import com.multiplayer.messages.pawn.MovingPawn;
import com.utils.*;

import java.io.Serializable;

import static com.utils.Functions.findCase;
import static com.screens.GameScreens.gameScreen;
import static com.utils.TileAndCases.*;


public class Pawn implements Serializable {
    private final int color; // La couleur du pion
    public Player player = null;

//    public boolean hasWeapon = false;
//    //Booléen qui indique si le pion a récupéré son arme
//    public boolean onExit = false;
//    //Booléen qui indique si le pion est sur la sortie

    public int getColor() {
        return color;
    }

    private boolean isLocked = false;

    // Un petit booléen qui permet d'éviter qu'on bouge les pions tant qu'on a pas mis une tuile à la sortie
    void unlock() {
        isLocked = false;
    }

    private Case setCase; // La case sur laquelle est le pion
    private transient BaseActor sprite;

    Pawn(int color) {
        this.color = color;
    }

    void setCase(Case tempCase) {
        try {
            setCase.pawn = null;
        } catch (Exception e) {
            //System.out.println("Error in Pawn.setCase(): Pawn hasn't got a Case yet");
        }
        setCase = tempCase;
        setCase.pawn = this;
        if (setCase.isExit && setCase.color == color) {
            queue.reveal();
            isLocked = !queue.isEmpty;
        }
        if (setCase.hasHourglass){
            Clock.clock.reset();
            setCase.hasHourglass=false;
            setCase.used();
        }

        if (setCase.hasWeapon && setCase.color == color) {
            numberWeaponsRetrieved ++;
            isLocked = true;
        }

        if (setCase.isFinalExit) {        // Si on fait plus de scénarii, il faudra rajouter le fait qu'il faut que ce soit de la bonne couleur
            numberPawnsOut ++;
            gameScreen.removePawn(this);
            setCase.pawn = null;
            dispose();
        }

    }

    private void setFirstCase() {
        // Relique de l'époque où on devait placer la toute première case
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
        sprite.setSize(size/2, size);
    }

    public void setSize() {
        setSize(caseSize / 0.95f);
    }
    // Fonctions classiques pour gérer la taille

    private boolean isMovable = false; // Même principe que pour la Queue

    void load() { // Pour la sérialization
        sprite = new BaseActor(new Texture("pions/" + Colors.getColor(color) + ".png"));
        gameScreen.getMainStage().addActor(sprite);
        setSize();
        updateCoordinates();
    }

    private void setSpritePosition(Vector2 target) {
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
        // Anciennement pour de l'interpolation,
        // A cause d'un problème non résolu, on ne fait en soit plus tellement de l'interpolation maintenant
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
        sprite.setY(setCase.getY(setCase.y) + caseSize / 10);
    }

    public void dispose() {
        sprite.remove();
    }

    public Vector2 getPosition() {
        return position;
    }

    private boolean canPlaceHere(Vector2 coordinates, Player player) {
        try {
            Case nextCase = findCase(coordinates); // Est-on sur une case ?
            if ((!(nextCase == null) && nextCase.isValid && checkServerForPlaceable()) // Si elle existe et est non nulle, et qu'on peut la placer
                || nextCase.equals(player.pawn.setCase)) { // Pour checker le cas où on veut la replacer là où elle était de base i guess
                setCase.revert(player); // On annule le pathfinding... avec un autre pathfinding
                setCase.hide(); // On cache la case de départ
                isMovable = false;
                player.dropsPawn(this);
                return true;
            }
        } catch (NullPointerException e) {
            System.out.println("No Tile found in Pawn.handleInput");
        } catch (Exception e) {
            e.printStackTrace();
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

    void handleInput(Player player) {
        // On me fait remarquer que la seule valeur que prends player c'est Multiplayer.me
        // Ce ne serait pas le cas si le serveur checkait vraiment pour voir si tu peux le placer quelque part je crois
        // Ou alors c'est géré autre part et je me souviens plus d'où
        // Trop fatigué pour vérifier, je regarderai ça demain
        if (isMovable) {
            float x = Functions.mouseInput().x - sprite.getWidth() / 2;
            float y = Functions.mouseInput().y - sprite.getHeight() / 2;
            setSpritePosition(new Vector2(x,y));
            if (count == 1) { // On veut pas avoir à le faire trop souvent
                // Edit: ça vient d'un vieux fragment de code où je n'envoyais ma position que toutes les actualisations
                // Ca causait des problèmes que je ne comprends pas, où le thread de render restait bloqué
                // J'ai aucune idée de pourquoi, mais bon en mettant 1 ça fonctionne
                // ça rends aussi totalement inutile mon code d'interpolation mais bon
                Multiplayer.courrier.sendMessage(new MovingPawn(this, new Vector2(x,y)));
                count = 0;
            }
            count++;
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Puis si on clique quelque part
                if (canPlaceHere(Functions.mouseInput(), player)) {
                    place(Functions.mouseInput());
                    player.dropsPawn(this);
                }
            }
        } else {
            isMovable = (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) &&
                    (sprite.getX() < Functions.mouseInput().x) && (Functions.mouseInput().x < sprite.getX() + sprite.getWidth() &&
                    (sprite.getY() < Functions.mouseInput().y) && (Functions.mouseInput().y < sprite.getY() + sprite.getHeight())) &&
                    !isLocked && !player.isHoldingPawn() &&
                    checkServerForClickable());
            if (isMovable) {
                setCase.show(); // On montre la case de départ
                player.takesPawn(this);
                setCase.explore(player); // On lance le pathfinding (structure récursive)
            }
        }
    }

    private boolean checkServerForClickable() {
        Multiplayer.courrier.sendMessage(new AskTakePawn(this));
        try {
            System.out.println("Blocking in pawn check click");
            Multiplayer.cyclicBarrier.await();
            System.out.println("Unlocking in pawn check click");
            // Pour synchroniser les threads
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Multiplayer.courrier.getAnswer());
        return Multiplayer.courrier.getAnswer();
    }

    private boolean checkServerForPlaceable() {
        Multiplayer.courrier.sendMessage(new AskPlacePawn(this));
        System.out.println("Client: checked for placeable");
        try {
            System.out.println("Blocking in pawn check place");
            Multiplayer.cyclicBarrier.await();
            System.out.println("Unlocking in pawn check palce");
            // Pour synchroniser les threads
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Multiplayer.courrier.getAnswer());
        return Multiplayer.courrier.getAnswer();
    }


}
