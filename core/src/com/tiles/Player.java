package com.tiles;

import static com.utils.Directions.numberDirections;
import static com.utils.Functions.modulo;

public class Player {
    boolean north;
    boolean south;
    boolean east;
    boolean west;
    boolean shortcutTaker;
    boolean escalatorTaker;

    Pawn pawn = null;

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

    public Player rotate(int i) {
        if (i==0) return this;
        else return (new Player(south,north,west,east,shortcutTaker,escalatorTaker,pawn)).rotate(modulo(i-1,numberDirections));
    }
    public void takesPawn(Pawn pawn) {
        this.pawn = pawn;
        //TODO Envoyer le message indiquant qu'on a pris le pion
    }
    public void dropsPawn(Pawn pawn) {
        this.pawn = null;
        //TODO Envoyer le message indiquant qu'on a posé le pion, avec ses coordonées
    }
}
