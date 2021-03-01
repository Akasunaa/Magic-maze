package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Case implements Serializable {
    private Case[] caseList = new Case[4];

    public int x;
    public int y;

    public boolean isAccessible; // Je suis même pas sûr qu'on l'utilise ça
    public boolean hasPortal;
    public boolean isValid = false; // Utiler pour le déplacement du pion
    private boolean isShowed = false;
    public Case shortcut;
    public Case elevator;
    public String color;
    private Tile tile;

    private transient Sprite greenDot;
    private transient Sprite redDot;
    private transient Sprite blueDot;

    Case(int number, Tile tile) {
        this.tile = tile;
        isAccessible = (number != 0);
        if (number < 10) {
            color = "none";
        } else {
            if (number % 10 == 0) color = "green";
            if (number % 10 == 1) color = "purple";
            if (number % 10 == 2) color = "yellow";
            if (number % 10 == 3) color = "orange";
        }
        hasPortal = number / 10 == 2;
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

    public void load() { // Comme d'habitude, obligatoire pour la sérialization
        greenDot = new Sprite(new Texture("tuiles/greenDot.png"));
        redDot = new Sprite(new Texture("tuiles/redDot.png"));
        blueDot = new Sprite(new Texture("tuiles/blueDot.png"));
        setSpriteCoordinates(x, y);
        // Le blueDot est inutile pour le moment mais sait-on jamais
    }

    public float offset() {
        return 40 * tile.getWidth() / 600;
    }

    public float tileSize() {
        return (tile.getWidth() - 2 * offset()) / 4;
    }

    public float getX(int x) {
        return tile.getX() + offset() + (x * tileSize());
    }

    public float getY(int y) {
        return tile.getY() + offset() + (y * tileSize());
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

    public void draw(Batch batch) {
        if (isShowed) redDot.draw(batch);
        else if (isValid && isAccessible) greenDot.draw(batch);
    }

    public void revert(Player player) {
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        if (player.getNorth()) {
            index = (2 + tile.rotation) % 4;
            if (caseList[index] != null) {
                caseList[index].unexplored();
                caseList[index].revert(player);
            }
        }

        if (player.getWest()) {
            index = ((3 - tile.rotation) % 4 + 4) % 4;
            // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
            if (caseList[index] != null) {
                caseList[index].unexplored();
                caseList[index].revert(player);
            }
        }

        if (player.getSouth()) {
            index = (tile.rotation % 4);
            if (caseList[index] != null) {
                caseList[index].unexplored();
                caseList[index].revert(player);
            }
        }

        if (player.getEast()) {
            index = ((1 - tile.rotation) % 4 + 4) % 4;
            if (caseList[index] != null) {
                caseList[index].unexplored();
                caseList[index].revert(player);
            }
        }
        if (player.getEscalatorTaker()) {
            if (elevator != null) {
                elevator.unexplored();
                elevator.revert(player.copyEscalator());
            }
        }

        if (player.getShortcutTaker()) {
            if (shortcut != null) {
                shortcut.unexplored();
                shortcut.revert(player.copyShortcut());
            }
        }
    }

    public void explore(Player player) {
        // C'est moche mais on fait comme ça pour éviter les NullPointerException
        // C'est fait pour ! Comme ça on a pas à checker que les prochaines cases existent, c'est automatique
        // Et j'ai vraiment la flemme de checker si la prochaine case existe à chaque fois
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        if (player.getNorth()) {
            index = (2 + tile.rotation) % 4;
            if (caseList[index] != null) {
                caseList[index].explored();
                caseList[index].explore(player);
            }
        }

        if (player.getWest()) {
            index = ((3 - tile.rotation) % 4 + 4) % 4;
            // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
            if (caseList[index] != null) {
                caseList[index].explored();
                caseList[index].explore(player);
            }
        }

        if (player.getSouth()) {
            index = (tile.rotation % 4);
            if (caseList[index] != null) {
                caseList[index].explored();
                caseList[index].explore(player);
            }
        }

        if (player.getEast()) {
            index = ((1 - tile.rotation) % 4 + 4) % 4;
            if (caseList[index] != null) {
                caseList[index].explored();
                caseList[index].explore(player);
            }
        }
        if (player.getEscalatorTaker()) {
            if (elevator != null) {
                elevator.explored();
                elevator.explore(player.copyEscalator());
            }
        }

        if (player.getShortcutTaker()) {
            if (shortcut != null) {
                shortcut.explored();
                shortcut.explore(player.copyShortcut());
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

    public void dispose() {
        redDot.getTexture().dispose();
        greenDot.getTexture().dispose();
        blueDot.getTexture().dispose();
    }


}
