package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Case implements Serializable {
    private Case[] caseList = new Case[4];

    public int x;
    public int y;

    public boolean accessible;
    public boolean hasPortal;
    public Case shortcut;
    public Case elevator;
    public String color;

    private Tile tile;

    private transient Sprite greenDot;
    private transient Sprite redDot;
    private transient Sprite blueDot;

    Case (int number, Tile tile) {
        this.tile = tile;
        accessible = (number != 0);
        if (number < 10) {
            color = "none";
        }
        else {
            if (number%10 == 0) color = "vert";
            if (number%10 == 1) color = "violet";
            if (number%10 == 2) color = "jaune";
            if (number%10 == 3) color = "orange";
        }
        hasPortal = number/10==2;
    }

    public void getNeighbours(int[][] horizontalWalls, int [][] verticalWalls) {
        x = tile.getCaseCoordinates(this)[0];
        y = tile.getCaseCoordinates(this)[1];
        // Voisin en haut
        if (y == 0 || horizontalWalls[y-1][x] == 1) caseList[0] = null;
        else caseList[0] = tile.caseList[y-1][x];
        // Voisin de droite
        if (x == 3 || verticalWalls[y][x] == 1) caseList[1] = null;
        else caseList[1] = tile.caseList[y][x+1];
        // Voisin du bas
        if (y == 3 || horizontalWalls[y][x] == 1) caseList[2] = null;
        else caseList[2] = tile.caseList[y+1][x];
        // Voisin de gauche
        if (x == 0 || verticalWalls[y][x-1] == 1) caseList[3] = null;
        else caseList[3] = tile.caseList[y][x-1];
    }

    public void load() {
        greenDot = new Sprite(new Texture("tuiles/greenDot.png"));
        greenDot.setX(tile.getX() + 40 + x*130);
        greenDot.setY(tile.getY() + 40 + y*130);
        redDot = new Sprite(new Texture("tuiles/redDot.png"));
        redDot.setX(tile.getX() + 40 + x*130);
        redDot.setY(tile.getY() + 40 + y*130);
        blueDot = new Sprite(new Texture("tuiles/blueDot.png"));
        blueDot.setX(tile.getX() + 40 + x*130);
        blueDot.setY(tile.getY() + 40 + y*130);
        // Le blueDot est inutile pour le moment mais sait-on jamais
    }

    public void show(Batch batch) {
        redDot.draw(batch); // Pour indiquer laquelle qu'on clique dessus quand même
    }

    public void explored(Batch batch) {
        if (accessible) greenDot.draw(batch); // On va pas explorer des cases innacessibles quand même
    }

    private void exploreSpecial(Batch batch,
                                boolean north, boolean south, boolean east, boolean west,
                                boolean shortcutTaker, boolean escalatorTaker){
        // Cette fonction est nécessaire pour éviter les StackOverflow à cause des shortcuts ou des escaliers
        // En effet, c'est le seul déplacement qui permet de revenir à son propre état
        // D'où la nécessité de ne pas, sous aucun prétexte, utiliser explore pour explorer un shortcut ou escalier
        // Pour le moment je vais me contenter de désactiver la capacité de prendre les escaliers après en avoir pris une première fois
        // Ca devrait pas être la mort
    }

    public void explore(Batch batch,
                        boolean north, boolean south, boolean east, boolean west,
                        boolean shortcutTaker, boolean escalatorTaker) {
        // C'est moche mais on fait comme ça pour éviter les NullPointerException
        // C'est fait pour ! Comme ça on a pas à checker que les prochaines cases existent, c'est automatique
        try {
            if (north) {
                caseList[(2 + tile.rotation) % 4].explored(batch);
                caseList[(2 + tile.rotation) % 4].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (Exception e) {}
        try {
            if (west) {
                caseList[(1 + tile.rotation) % 4].explored(batch);
                caseList[(1 + tile.rotation) % 4].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (Exception e) {}
        try {
            if (south) {
                caseList[(tile.rotation) % 4].explored(batch);
                caseList[(tile.rotation) % 4].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (Exception e) {}
        try {
            if (east) {
                caseList[(3 + tile.rotation) % 4].explored(batch);
                caseList[(3 + tile.rotation) % 4].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (Exception e) {}
        try {
            if (escalatorTaker) {
                elevator.explored(batch);
                elevator.explore(batch, north, south, east, west, shortcutTaker, !escalatorTaker);
            }
        } catch (Exception e) {}
        try {
            if (shortcutTaker) {
                shortcut.explored(batch);
                shortcut.explore(batch, north, south, east, west, !shortcutTaker, escalatorTaker);
            }
        } catch (Exception e) {}
    }

    // Je les fait en statique parce que c'est plus pratique
    public static void makeElevator(Case case1, Case case2) {
        case1.elevator = case2;
        case2.elevator = case1;
    }
    public static void makeShortcut(Case case1, Case case2) {
        case1.shortcut = case2;
        case2.shortcut = case1;
    }

    public void dispose() {
        redDot.getTexture().dispose();
        greenDot.getTexture().dispose();
        blueDot.getTexture().dispose();
    }


}
