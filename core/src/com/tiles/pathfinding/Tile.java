package com.tiles.pathfinding;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Tile implements Serializable {
    private int number;
    private String path;
    private transient Sprite sprite; // transient ça veut dire qu'on le stock pas dans la serialization
    public Case[][] caseList;

    public int rotation = 0;

    public float getX() {
        return sprite.getX();
    }

    public float getY() {
        return sprite.getY();
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }
    // fonctions classiques j'ai envie de dire

    Tile(int number) {
        this.number = number;
        // Bon on stock le path quelque part quand même
        path = "tuiles/tile" + number + ".jpg";

        /* Et maintenant, on construit allégrement la tuile
        Je vais maintenant expliquer comment on utilise le système de tableau
        On a un tableau 4x4 pour les cases, et pour les murs...
        Un tableau de 3x4 pour ceux verticaux
        Un tableau de 4x3 pour ceux horizontaux
        Pour le mur: 1 = un mur, 0 = pas de mur
        Pour les cases:
         - 0 = innacessible
         - 1 = accessible, case normale
         - 2 = une entrée
         // les racourcis et les escalators sont rajoutés à la main
         - 7 = un sablier
         - 8 = boule de crystal
         - 9 = caméra
         - 1x = une sortie normale
         - 2x = un téléporteur (voir comment les coder plus tard)
         - 3x = une arme
         - 4x = une sortie de joueur

         pour les grands nombres, on a
         - 0 -> vert
         - 1 -> violet
         - 2 -> jaune
         - 3 -> orange
         */
        caseList = new Case[4][4];
        for (int tempX = 0; tempX < 4; tempX++) {
            for (int tempY = 0; tempY < 4; tempY++) {
                caseList[tempY][tempX] = new Case(TileArray.getArray(number)[tempY][tempX], this);
            }
        }
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne) {
                tempCase.getNeighbours(
                        TileArray.getArrayWallHorizontal(number),
                        TileArray.getArrayWallVertical(number));
            }
        }
        // Et on rajoute les raccourcis et escalators
        complete();
    }

    public void load() {
        sprite = new Sprite(new Texture(path)); // On se charge soit même
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.load(); // et on charge toutes les cases
        }
        sprite.setX(0);
        sprite.setY(0);
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    public void showAll(Batch batch) { // Truc de déboguage
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.show(batch);
        }
    }

    public int[] getCaseCoordinates(Case self) {
        // C'est juste pour récupérer le x,y d'une case parce que flemme de le faire autrement
        int[] xy = {0, 0};
        for (Case[] ligne : caseList) {
            xy[0] = 0;
            for (Case comparator : ligne) {
                if (comparator.equals(self)) return xy;
                xy[0]++;
            }
            xy[1]++;
        }
        return xy;
    }

    public Case getCase(float x, float y) {
        return caseList[((int) y - 40) / 130][((int) x - 40) / 130];
        // C'est du calcul simple, si tu comprends pas retourne en maternelle
    }

    private void complete() {
        // Petite fonction pour rajouter les escalators et les raccourcis
        if (number == 2) Case.makeElevator(caseList[2][0], caseList[0][1]);
        if (number == 7) Case.makeElevator(caseList[0][1], caseList[2][2]);
        if (number == 10) Case.makeElevator(caseList[2][2], caseList[1][1]);
        if (number == 12) {
            Case.makeElevator(caseList[3][1], caseList[2][2]);
            Case.makeElevator(caseList[1][0], caseList[0][1]);
        }
        if (number == 13) Case.makeShortcut(caseList[2][2], caseList[2][3]);
        if (number == 14) {
            Case.makeElevator(caseList[2][0], caseList[1][2]);
            Case.makeShortcut(caseList[1][2], caseList[0][2]);
        }
        if (number == 15) {
            Case.makeElevator(caseList[3][0], caseList[2][2]);
            Case.makeShortcut(caseList[1][0], caseList[1][1]);
        }
        if (number == 16) Case.makeShortcut(caseList[1][2], caseList[1][3]);
        if (number == 19) Case.makeShortcut(caseList[1][2], caseList[1][3]);
        if (number == 24) Case.makeShortcut(caseList[0][2], caseList[0][3]);
    }

    public void dispose() {
        sprite.getTexture().dispose();
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.dispose();
        }
    }
}
