package com.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.screens.game.BaseActor;
import com.utils.Colors;
import com.sun.tools.javac.comp.Todo;
import com.utils.TileAndCases;

import java.io.Serializable;
import java.util.function.ToDoubleBiFunction;

import static com.utils.CaseCorrespondance.*;
import static com.utils.Directions.*;
import static com.utils.Functions.modulo;
import static com.screens.GameScreens.gameScreen;
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.*;

public class Case implements Serializable {
    public transient Case[] caseList = new Case[4];

    int x;
    int y;

    boolean isAccessible; // Je suis même pas sûr qu'on l'utilise ça
    boolean hasPortal;
    boolean hasHourglass;
    boolean isExit;
    boolean isEntrance;
    boolean hasWeapon;
    boolean isFinalExit;
    public Pawn pawn = null;


    boolean isValid = false; // Utile pour le déplacement du pion
    private boolean isShowed = false;
    private transient Case shortcut;
    private transient Case elevator;
    int color;
    private transient Tile tile; // Il faut éviter de faire un StackOverFlowError lors de la conversion en Json ou de la serialization

    private transient BaseActor greenDot;
    private transient BaseActor redDot;

    Case(int number, Tile tile) {
        this.tile = tile;
        isAccessible = (number != unnacessible);
        color = number % 10;
        isEntrance = number == entrance;
        hasHourglass = number == hourglass;
        isExit = number / 10 == exit / 10;
        hasPortal = number / 10 == portal / 10;
        hasWeapon = number / 10 == weapon / 10;
        isFinalExit = number / 10 == finalExit / 10;
    }

    void getNeighbours(int[][] horizontalWalls, int[][] verticalWalls) {
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

    void load(Tile tile, Case[] caseList) {// Comme d'habitude, obligatoire pour la sérialisation
        this.tile = tile;
        this.caseList = caseList;
        greenDot = new BaseActor(new Texture("tuiles/greenDot.png"));
        greenDot.setVisible(false);
        redDot = new BaseActor(new Texture("tuiles/redDot.png"));
        redDot.setVisible(false);
        setSpriteCoordinates();
        gameScreen.getMainStage().addActor(greenDot);
        gameScreen.getMainStage().addActor(redDot);
    }

    float getX(int x) {
        return tile.x + offset + (x * caseSize);
    }

    float getY(int y) {
        return tile.y + offset + (y * caseSize);
    }

    private void setSpriteCoordinates() { // self explanatory
        float tempX = getX(x);
        float tempY = getY(y);
        greenDot.setX(tempX);
        greenDot.setY(tempY);
        redDot.setX(tempX);
        redDot.setY(tempY);
    }

    void setSize(float size) {
        greenDot.setSize(size, size);
        redDot.setSize(size, size);
    }

    void updateCoordinates() {
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
        setSpriteCoordinates();
    }

    void show() {
        isShowed = true;
        isValid = true;
        redDot.setVisible(isShowed);
        greenDot.setVisible(false);
    }

    void hide() {
        isShowed = false;
        isValid = false;
        redDot.setVisible(isShowed);
        greenDot.setVisible(isValid);

    }

    void explored() {
        isValid = true;
        greenDot.setVisible(!isShowed);
    }

    void unexplored() {
        isValid = false;
        greenDot.setVisible(isValid);
    }

    void used(){
        Texture t = new Texture(Gdx.files.internal("tuiles/used.png"));
        BaseActor cross = new BaseActor(t);
        cross.setOrigin((int) (cross.getWidth()/2));
        cross.setPosition(getX(x),getY(y));
        gameScreen.getMainStage().addActor(cross);
    }

    void draw() {
        if (isShowed) redDot.draw(batch, 1);
        else if (isValid) greenDot.draw(batch, 1);
    }

    private boolean seen = false;

    void explore(Player player) {
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        Player tempPlayer = player.rotate(tile.rotation);
        if (!seen && isAccessible && (player.pawn == pawn || pawn == null)) {
            seen = true; // Parcours de graphe classique pour éviter les StackOverflow
            explored();
            if (tempPlayer.north) {
                index = modulo(north + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.west) {
                index = modulo(west + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.south) {
                index = modulo(south + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explore(player);
                }
            }

            if (tempPlayer.east) {
                index = modulo(east + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].explore(player);
                }
            }
            if (player.escalatorTaker) {
                if (elevator != null) {
                    elevator.explore(player);
                }
            }

            if (player.pawn.getColor() == Colors.orange) {
                if (shortcut != null) {
                    shortcut.explore(player);
                }
            }

            if (player.portalTaker && !isInPhaseB){
                for (Case tempCase : TileAndCases.portalList[player.pawn.getColor()]){
                    tempCase.explore(player);
                }
            }
        }
    }

    void revert(Player player) {
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        Player tempPlayer = player.rotate(tile.rotation);
        if (seen) {
            seen = false;
            if (tempPlayer.north) {
                index = modulo(north + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.west) {
                index = modulo(west + tile.rotation, numberDirections);
                // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.south) {
                index = modulo(south + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }

            if (tempPlayer.east) {
                index = modulo(east + tile.rotation, numberDirections);
                if (caseList[index] != null) {
                    caseList[index].unexplored();
                    caseList[index].revert(player);
                }
            }
            if (tempPlayer.escalatorTaker) {
                if (elevator != null) {
                    elevator.unexplored();
                    elevator.revert(player);
                }
            }

            if (player.pawn.getColor() == Colors.orange) {
                if (shortcut != null) {
                    shortcut.unexplored();
                    shortcut.revert(player);
                }
            }

            if (player.portalTaker){
                for (Case tempCase : TileAndCases.portalList[player.pawn.getColor()]){
                    tempCase.unexplored();
                    tempCase.revert(player);
                }
            }
        }
    }

    // Je les fait en statique parce que c'est plus pratique
    static void makeElevator(Case case1, Case case2) {
        case1.elevator = case2;
        case2.elevator = case1;
    }

    static void makeShortcut(Case case1, Case case2) {
        case1.shortcut = case2;
        case2.shortcut = case1;
    }

    static void link(Case case1, Case case2, int direction) {
        // Direction indique la direction de la case 2 par rapport à la case 1
        // Exemple: la case 2 est au nord de la case 1
        // alors direction = 2
        case1.caseList[modulo(direction - case1.tile.rotation, numberDirections)] = case2;
        case2.caseList[modulo(direction + 2 - case2.tile.rotation, numberDirections)] = case1;
        case1.isExit = false;
        case2.isExit = false;
        if (case1.pawn != null)
            case1.pawn.unlock();
        if (case2.pawn != null)
            case2.pawn.unlock();
    }

    void dispose() {
        redDot.remove();
        greenDot.remove();
    }


}
