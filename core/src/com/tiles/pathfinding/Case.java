package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Case implements Serializable {
    private Case[] caseList = new Case[4];

    public int x;
    public int y;

    public boolean accessible; // Je suis même pas sûr qu'on l'utilise ça
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
            if (number%10 == 0) color = "green";
            if (number%10 == 1) color = "purple";
            if (number%10 == 2) color = "yellow";
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

    public void load() { // Comme d'habitude, obligatoire pour la sérialization
        greenDot = new Sprite(new Texture("tuiles/greenDot.png"));
        redDot = new Sprite(new Texture("tuiles/redDot.png"));
        blueDot = new Sprite(new Texture("tuiles/blueDot.png"));
        setSpriteCoordinates(x,y);
        // Le blueDot est inutile pour le moment mais sait-on jamais
    }

    public float offset() {return 40 * tile.getWidth() / 600;}
    public float tileSize() {return (tile.getWidth() - 2 * offset()) / 4;}
    public float getX(int x) {return tile.getX() + offset() + (x * tileSize());}
    public float getY(int y) {return tile.getY() + offset() + (y * tileSize());}
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
        greenDot.setSize(size,size);
        blueDot.setSize(size,size);
        redDot.setSize(size,size);
    }
    public int[] getRotatedCoordinates() {
        int[] xy = tile.getCaseCoordinates(this);
        if (tile.rotation == 0) {
            x = xy[0];
            y = xy[1];
        }
        if (tile.rotation == 3) {
            x = xy[1];
            y = 3-xy[0];
        }
        if (tile.rotation == 2) {
            x = 3-xy[0];
            y = 3-xy[1];
        }
        if (tile.rotation == 1) {
            x = 3-xy[1];
            y = xy[0];
        }
        return new int[]{x, y};
    }
    public void updateCoordinates() {
        // Cette fonction met à jour les coordonées des sprites lorsque la tuile est tournée
        int[] xy = getRotatedCoordinates();
        setSpriteCoordinates(x,y);
    }

    public void show(Batch batch) {
        redDot.draw(batch); // Pour indiquer laquelle qu'on clique dessus quand même
    }

    public void explored(Batch batch) {
        if (accessible) greenDot.draw(batch); // On va pas explorer des cases innacessibles quand même
        // Ah bah si tiens je m'en sert
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
        // Et j'ai vraiment la flemme de checker si la prochaine case existe à chaque fois
        int index; // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        try {
            if (north) {
                index = (2 + tile.rotation) % 4;
                caseList[index].explored(batch);
                caseList[index].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (NullPointerException e) {}
        try {
            if (west) {
                index = ((3 - tile.rotation) % 4 +4) % 4;
                // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
                caseList[index].explored(batch);
                caseList[index].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (NullPointerException e) {}
        try {
            if (south) {
                index = (tile.rotation %4);
                caseList[index].explored(batch);
                caseList[index].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (NullPointerException e) {}
        try {
            if (east) {
                index = ((1 - tile.rotation) % 4 +4) % 4;
                caseList[index].explored(batch);
                caseList[index].explore(batch, north, south, east, west, shortcutTaker, escalatorTaker);
            }
        }catch (NullPointerException e) {}
        try {
            if (escalatorTaker) {
                elevator.explored(batch);
                elevator.explore(batch, north, south, east, west, shortcutTaker, !escalatorTaker);
            }
        } catch (NullPointerException e) {}
        try {
            if (shortcutTaker) {
                shortcut.explored(batch);
                shortcut.explore(batch, north, south, east, west, !shortcutTaker, escalatorTaker);
            }
        } catch (NullPointerException e) {}
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
