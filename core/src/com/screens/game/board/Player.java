package com.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.screens.game.BaseActor;

import java.io.Serializable;

import static com.utils.Directions.numberDirections;
import static com.utils.Functions.modulo;

public class Player implements Serializable {
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;
    public boolean escalatorTaker;
    public boolean portalTaker;
    public boolean cardChooser;

    public String pseudo = "Pseudo";
    public transient BaseActor avatar;
    public String avatarName;

    Pawn pawn = null;

    @JsonIgnore
    public boolean isHoldingPawn() {
        return pawn !=null;
    }

    public Player() {
    }


    public Player load() {
        avatar = new BaseActor(new Texture(Gdx.files.internal("Avatars/" + avatarName + ".png")));
        return this;
    }

    private Player(boolean north, boolean south,
            boolean east, boolean west,
            boolean escalatorTaker, boolean portalTaker,
            boolean cardChooser, Pawn pawn) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.escalatorTaker = escalatorTaker;
        this.portalTaker = portalTaker;
        this.cardChooser = cardChooser;
        this.pawn = pawn;
    }
    Player(boolean north, boolean south,
                  boolean east, boolean west,
                  boolean escalatorTaker, boolean portalTaker,
                  boolean cardChooser) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.escalatorTaker = escalatorTaker;
        this.portalTaker = portalTaker;
        this.cardChooser = cardChooser;
    }
    // Quel enfer

    void setPlayer(Player player) {
        north = player.north;
        south = player.south;
        west = player.west;
        east = player.east;
        escalatorTaker = player.escalatorTaker;
        portalTaker = player.portalTaker;
    }

    Player rotate(int i) {
        if (i==0) return this;
        else return (new Player(south,north,west,east,escalatorTaker,portalTaker, cardChooser,pawn)).rotate(modulo(i-1,numberDirections));
    }
    public void takesPawn(Pawn pawn) {
        this.pawn = pawn;
        pawn.player = this;
    }
    public void dropsPawn(Pawn pawn) {
        this.pawn = null;
        pawn.player = null;
    }
}
