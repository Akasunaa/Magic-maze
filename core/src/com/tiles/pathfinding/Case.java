package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

import static com.utils.CaseCorrespondance.*;
import static com.utils.Directions.*;
import static com.utils.Functions.modulo;
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.caseSize;
import static com.utils.TileAndCases.offset;
import static com.utils.Colors.*;

public class Case implements Serializable {
    public transient Case[] caseList = new Case[4];

    public int x;
    public int y;

    public boolean isAccessible; // Je suis même pas sûr qu'on l'utilise ça
    public boolean hasPortal;
    public boolean isExit;
    public boolean isEntrance;
    public boolean hasWeapon;
    public boolean isFinalExit;

    public boolean isValid = false; // Utiler pour le déplacement du pion
    private boolean isShowed = false;
    public transient Case shortcut;
    public transient Case elevator;
    public int color;
    private transient Tile tile; // Il faut éviter de faire un StackOverFlowError lors de la conversion en Json ou de la serialization

    private transient Sprite greenDot;
    private transient Sprite redDot;
    private transient Sprite blueDot;

    Case(int number, Tile tile) {
        this.tile = tile;
        isAccessible = (number != unnacessible);
        color = number % 10;
        isEntrance = number == entrance;
        isExit = number / 10 == exit / 10;
        hasPortal = number / 10 == portal / 10;
        hasWeapon = number / 10 == weapon / 10;
        isFinalExit = number / 10 == finalExit / 10;
    }

    public void getNeighbours(int[][] horizontalWalls, int[][] verticalWalls) {
        x = tile.getCaseCoordinates(this)[0];
        y = tile.getCaseCoordinates(this)[1];
        // Voisin en haut
        if (y == 0 || horizontalWalls[y - 1][x] == 1) caseList[0] = null;
        else caseList[0] = tile.caseList[y - 1][x];
        // Voisin de droite
        if (x == 3 || verticalWalls[y][x] == 1) caseList[1] = null;
        else caseList[1] = tile.caseList[y][x + 1];
        // Voisin du bas
        if (y == 3 || horizontalWalls[y][x] == 1) caseList[2] = null;
        else caseList[2] = tile.caseList[y + 1][x];
        // Voisin de gauche
        if (x == 0 || verticalWalls[y][x - 1] == 1) caseList[3] = null;
        else caseList[3] = tile.caseList[y][x - 1];
    }

    public void load(Tile tile, Case[] caseList) {// Comme d'habitude, obligatoire pour la sérialization
        this.tile = tile;
        this.caseList = caseList;
        greenDot = new Sprite(new Texture("tuiles/greenDot.png"));
        redDot = new Sprite(new Texture("tuiles/redDot.png"));
        blueDot = new Sprite(new Texture("tuiles/blueDot.png"));
        setSpriteCoordinates(x, y);
        // Le blueDot est inutile pour le moment mais sait-on jamais
    }

    public float getX(int x) {
        return tile.getX() + offset + (x * caseSize);
    }

    public float getY(int y) {
        return tile.getY() + offset + (y * caseSize);
    }

    private void setSpriteCoordinates(int x, int y) { // self explanatory
        float tempX = getX(x);
        float tempY = getY(y);
        greenDot.setX(tempX);
        greenDot.setY(tempY);
        redDot.setX(tempX);
        redDot.setY(tempY);
        blueDot.setX(tempX);
        blueDot.setY(tempY);
    }

    public void setSize(float size) {
        greenDot.setSize(size, size);
        blueDot.setSize(size, size);
        redDot.setSize(size, size);
    }

    public int[] getRotatedCoordinates() {
        int[] xy = tile.getCaseCoordinates(this);
        if (tile.rotation == 0) {
            x = xy[0];
            y = xy[1];
        }
        if (tile.rotation == 3) {
            x = xy[1];
            y = 3 - xy[0];
        }
        if (tile.rotation == 2) {
            x = 3 - xy[0];
            y = 3 - xy[1];
        }
        if (tile.rotation == 1) {
            x = 3 - xy[1];
            y = xy[0];
        }
        return new int[]{x, y};
    }

    public void updateCoordinates() {
        // Cette fonction met à jour les coordonées des sprites lorsque la tuile est tournée
        int[] xy = getRotatedCoordinates();
        setSpriteCoordinates(x, y);
    }

    public void show() {
        isShowed = true;
        isValid = true;
    }

    public void hide() {
        isShowed = false;
        isValid = false;
    }

    public void explored() {
        isValid = true && isAccessible;
    }

    public void unexplored() {
        isValid = false;
    }

    public void draw() {
        if (isShowed) redDot.draw(batch);
        else if (isValid) greenDot.draw(batch);
    }

    private boolean seen = false;

    public void explore(Player player) {
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        Player tempPlayer = player.rotate(tile.rotation);
        if (!seen) {
            seen = true; // Parcours de graphe classique pour éviter les StackOverflow
            if (tempPlayer.getNorth()) {
                index = modulo(north + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explored();
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.getWest()) {
                index = modulo(west + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explored();
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.getSouth()) {
                index = modulo(south + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explored();
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.getEast()) {
                index = modulo(east + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explored();
                    caseList[index].explore(player);
                }
            }
            if (player.getEscalatorTaker()) {
                if (elevator != null) {
                    elevator.explored();
                    elevator.explore(player);
                }
            }

            if (player.getShortcutTaker()) {
                if (shortcut != null) {
                    shortcut.explored();
                    shortcut.explore(player);
                }
            }
        }
    }

    public void revert(Player player) {
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        Player tempPlayer = player.rotate(tile.rotation);
        if (seen) {
            seen = false;
            if (tempPlayer.getNorth()) {
                index = modulo(north + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.getWest()) {
                index = modulo(west + tile.rotation, numberDirections);
                // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.getSouth()) {
                index = modulo(south + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.getEast()) {
                index = modulo(east + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }
            if (tempPlayer.getEscalatorTaker()) {
                if (elevator != null) {
                    elevator.unexplored();
                    elevator.revert(player);
                }
            }

            if (tempPlayer.getShortcutTaker()) {
                if (shortcut != null) {
                    shortcut.unexplored();
                    shortcut.revert(player);
                }
            }
        }
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

    public static void link(Case case1, Case case2, int direction) {
        // Direction indique la direction de la case 2 par rapport à la case 1
        // Exemple: la case 2 est au nord de la case 1
        // alors direction = 2
        case1.caseList[modulo(direction-case1.tile.rotation, numberDirections)] = case2;
        case2.caseList[modulo(direction + 2 - case2.tile.rotation, numberDirections)] = case1;
    }

    public void dispose() {
        redDot.getTexture().dispose();
        greenDot.getTexture().dispose();
        blueDot.getTexture().dispose();
    }


}
