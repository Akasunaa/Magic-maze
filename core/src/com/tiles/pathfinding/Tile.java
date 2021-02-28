package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;

public class Tile implements Serializable {
    private int number; // numéro de tuile
    private String path; // path de la tuile
    private transient Sprite sprite; // transient ça veut dire qu'on le stock pas dans la serialization

    public Sprite getSprite() {
        return sprite;
    } // On en a besoin pour la pile

    public Case[][] caseList; // Un tableau de 4x4 avec les cases
    public int rotation = 0; // Indicateur de rotation (dans le sens trigonométrique)

    private long cooldown;

    public void startCooldown() {
        cooldown = System.currentTimeMillis();
    }
    // L'utilité de cette variable et de cette méthode est questionnable
    // Elles sont utiles pour éviter un phénomène que j'appelle le blinking
    // Qui fait que, au moment où on dépose la carte, les trucs du pathfinding apparaissent

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

    public void setSize(float size) {
        sprite.setSize(size, size);
    }

    // fonctions classiques j'ai envie de dire

    public void handleInput(Batch batch, float mouseX, float mouseY, BitmapFont numberCase) {
        Case tempCase;
        // Puis si on clique gauche, boum, le pathfinding
        if ((Gdx.input.isButtonPressed(Input.Buttons.LEFT)) && (System.currentTimeMillis() - cooldown > 500)) {
            try {
                tempCase = getCase(mouseX - getX(), mouseY - getY());
                tempCase.show(batch);
                tempCase.explore(batch, false, false, true, false, false, false);
                numberCase.draw(batch, "x = " + tempCase.x + "; y = " + tempCase.y + "; couleur = " + tempCase.color + ", portal = " + tempCase.hasPortal, 700f, 200f);
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            // Bah oui parce que si on est pas dans les bornes de la tuile forcément getCase fonctionne moins bien lol
        }

        // On gère la rotation
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) rotate(-1);
        if (Gdx.input.isKeyJustPressed(Input.Keys.A)) rotate(+1);
        // Et la taille (deprecated, on devrait plus à avoir à faire ça maintenant)
        if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS)) resize(+50f);
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUMPAD_SUBTRACT)) resize(-50f);
    }

    public void resize(float size) {
        setSize(sprite.getWidth() + size);
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne) {
                tempCase.setSize(128 * sprite.getWidth() / 600);
                // On scale la taille des cases avec la taille de la tuile
                tempCase.updateCoordinates();
            }
        }
    }


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
        // On commence par créer le tableau de cases avec des Case à l'intérieur
        for (int tempX = 0; tempX < 4; tempX++) {
            for (int tempY = 0; tempY < 4; tempY++) {
                caseList[tempY][tempX] = new Case(TileArray.getArray(number)[tempY][tempX], this);
            }
        }
        // Puis on créé les liaisons entre les cases
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

    public void load() { // Obligatoire pour la serialization
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
        float offset = 40 * getWidth() / 600;
        float tileSize = (getWidth() - 2 * offset) / 4;
        int tempX = (int) ((x - offset) / tileSize);
        int tempY = (int) ((y - offset) / tileSize);
        int buffer;
        if (rotation == 1) {
            buffer = tempX;
            tempX = tempY;
            tempY = 3 - buffer;
        }
        if (rotation == 2) {
            tempX = 3 - tempX;
            tempY = 3 - tempY;
        }
        if (rotation == 3) {
            buffer = tempX;
            tempX = 3 - tempY;
            tempY = buffer;
        }
        return caseList[tempY][tempX];
        // C'est du calcul simple, si tu comprends pas retourne en maternelle
    }


    public void rotate(int angle) {
        rotation += angle;
        rotation = (rotation % 4 + 4) % 4; // Java et les modulos...
        sprite.rotate(angle * 90); // Dans le sens trigo
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.updateCoordinates();
        }
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

    public void dispose() { // fonction dispose classique
        sprite.getTexture().dispose();
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.dispose();
        }
    }
}
