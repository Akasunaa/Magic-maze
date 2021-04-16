package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.menu.BaseActor;
import com.utils.Multiplayer;

import java.io.IOException;
import java.io.Serializable;

import static com.utils.Directions.numberDirections;
import static com.utils.Functions.modulo;

public class Player implements Serializable {
    boolean north;
    boolean south;
    boolean east;
    boolean west;
    boolean shortcutTaker;
    boolean escalatorTaker;

    public String pseudo = "Placeholder2";
    public transient BaseActor avatar;
    public String avatarName;

    Pawn pawn = null;

    public Player() {
    }


    public Player load() {
        avatar = new BaseActor(new Texture(Gdx.files.internal("Avatars/" + avatarName + ".png")));
        return this;
    }

    public Player(boolean north,
            boolean south,
            boolean east,
            boolean west,
            boolean shortcutTaker,
            boolean escalatorTaker,
            Pawn pawn) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.shortcutTaker = shortcutTaker;
        this.escalatorTaker = escalatorTaker;
        this.pawn = pawn;
    }
    public Player(boolean north,
                  boolean south,
                  boolean east,
                  boolean west,
                  boolean shortcutTaker,
                  boolean escalatorTaker) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.shortcutTaker = shortcutTaker;
        this.escalatorTaker = escalatorTaker;
    }

    public void setPlayer(Player player) {
        north = player.north;
        south = player.south;
        west = player.west;
        east = player.east;
        escalatorTaker = player.escalatorTaker;
        shortcutTaker = player.shortcutTaker;
    }

    public Player rotate(int i) {
        if (i==0) return this;
        else return (new Player(south,north,west,east,shortcutTaker,escalatorTaker,pawn)).rotate(modulo(i-1,numberDirections));
    }
    public void takesPawn(Pawn pawn) {
        this.pawn = pawn;
        pawn.player = this;
        //TODO Envoyer le message indiquant qu'on a pris le pion
    }
    public void dropsPawn(Pawn pawn) {
        this.pawn = null;
        pawn.player = null;
        //TODO Envoyer le message indiquant qu'on a posé le pion, avec ses coordonées
    }
}
