package com.tiles.pathfinding;

public class Player {
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;
    public boolean shortcutTaker;
    public boolean escalatorTaker;
    Player(boolean north, boolean south, boolean east, boolean west, boolean shortcutTaker, boolean escalatorTaker) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.escalatorTaker = escalatorTaker;
        this.shortcutTaker = shortcutTaker;
    }

    public Player copyEscalator() {
        return new Player(north,south,east,west,shortcutTaker,false);
    }
    public Player copyShortcut() {
        return new Player(north,south,east,west,false,escalatorTaker);
    }
}
