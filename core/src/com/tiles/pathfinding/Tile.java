package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;

import java.io.Serializable;

import static com.tiles.pathfinding.NeededConstants.*;
import static jdk.internal.dynalink.support.Guards.isNull;

public class Tile implements Serializable {
    public int number; // numéro de tuile
    private String path; // path de la tuile
    private transient Sprite sprite; // transient ça veut dire qu'on le stock pas dans la serialization

    public Sprite getSprite() {
        return sprite;
    } // On en a besoin pour la pile

    public Case[][] caseList; // Un tableau de 4x4 avec les cases
    private Case[][][] caseListofCases; // Pour la serialization
    public int rotation = 0; // Indicateur de rotation (dans le sens trigonométrique)
    public boolean[] exits;
    public Case[] exitCases;
    // Exit représente où sont les sorties de cette tuile
    // Comme d'habitude, 0 est le Sud, et on tourne dans le sens trigo
    public int entrance;
    public Case entranceCase;
    // L'entrée représente la direction cardinale de la tuile

    private long cooldown;

    public void startCooldown() {
        cooldown = System.currentTimeMillis();
    }
    // L'utilité de cette variable et de cette méthode est questionnable
    // Elles sont utiles pour éviter un phénomène que j'appelle le blinking
    // Qui fait que, au moment où on dépose la carte, les trucs du pathfinding apparaissent


    public float x = 0;
    public float y = 0;
    private float size = NeededConstants.tileSize;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void updateAll() {
        sprite.setX(x);
        sprite.setY(y);
        updateSize();
    }

    private void updateCoordinates() {
        sprite.setX(x);
        sprite.setY(y);
        updateAllCases();
    }

    public float getSize() {
        return size;
    }

    public void updateSize() {
        sprite.setSize(size, size);
        updateAllCases();
    }

    // fonctions classiques j'ai envie de dire
    private boolean exploring = false;
    public void handleInput(Player player, BitmapFont numberCase) {
        Case tempCase;
        Vector2 mouseInput = mouseInput();
        // Puis si on clique gauche, boum, le pathfinding
        if (!exploring && (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) && (System.currentTimeMillis() - cooldown > 500)) {
            try {
                tempCase = getCase(mouseInput);
                tempCase.show();
                tempCase.explore(player);
                numberCase.draw(NeededConstants.batch, "x = " + tempCase.x + "; y = " + tempCase.y + "; couleur = " + tempCase.color + ", portal = " + tempCase.hasPortal, 700f, 200f);
                exploring = true;
                lastExploredCase = tempCase;
            } catch (ArrayIndexOutOfBoundsException e) {
            }
            // Bah oui parce que si on est pas dans les bornes de la tuile forcément getCase fonctionne moins bien lol
        }
        else if (exploring && (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT))) {
            lastExploredCase.revert(player);
            lastExploredCase.hide();
            exploring = false;
        }
        // On gère la rotation
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
        caseListofCases = new Case[4][4][4];

        // On commence par créer le tableau de cases avec des Case à l'intérieur
        for (int tempX = 0; tempX < 4; tempX++) {
            for (int tempY = 0; tempY < 4; tempY++) {
                caseList[tempY][tempX] = new Case(TileArray.getArray(number)[tempY][tempX], this);
                caseListofCases[tempY][tempX] = caseList[tempY][tempX].caseList;
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

        exitCases = new Case[]{caseList[0][1], caseList[1][3],caseList[3][2], caseList[2][0]};
        exits = new boolean[]{caseList[0][1].isExit, caseList[1][3].isExit,caseList[3][2].isExit, caseList[2][0].isExit};

        System.out.println("Tile number " + number + " has exits " + exits[0] + " "+ exits[1] + " "+ exits[2] + " "+ exits[3]);

        if (caseList[0][1].isEntrance) {
            entrance = 0;
            entranceCase = caseList[0][1];
        }
        if (caseList[1][3].isEntrance) {
            entrance = 1;
            entranceCase = caseList[1][3];
        }
        if (caseList[2][0].isEntrance) {
            entrance = 3;
            entranceCase = caseList[2][0];
        }
        if (caseList[3][2].isEntrance) {
            entrance = 2;
            entranceCase = caseList[3][2];
        }
        System.out.println("Tile number " + number + " has entrance number " + entrance);
        // On récupère la coordonée d'entrée
    }

    public void load() { // Obligatoire pour la serialization
        sprite = new Sprite(new Texture(path)); // On se charge soit même
        sprite.setOrigin(tileSize/2, tileSize/2);
        for (int i = 0; i <=3; i ++) {
            for (int j = 0; j <=3; j++)
                caseList[j][i].load(this, caseListofCases[j][i]); // et on charge toutes les cases
        }
        // Et on rajoute les raccourcis et escalators
        complete();
        updateAll();
    }

    public void draw() {
        sprite.draw(NeededConstants.batch);
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.draw();
        }
    }

    public void showAll(Batch batch) { // Truc de déboguage
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne)
                tempCase.show();
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

    public Case getCase(Vector2 mouseInput) {
        int tempX = (int) Math.floor((mouseInput.x - x - offset) / caseSize);
        int tempY = (int) Math.floor((mouseInput.y - y - offset ) / caseSize);
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
        updateAllCases();
    }

    private void updateAllCases() {
        for (Case[] ligne : caseList) {
            for (Case tempCase : ligne) {
                tempCase.updateCoordinates();
                tempCase.setSize(128 * sprite.getWidth() / 600);
            }
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

    private static boolean isValidPlacement(Tile tileToPlace, Tile tileToJoin) {
        if (tileToJoin == null) return false;
        return tileToJoin.exits[((2+tileToPlace.entrance - tileToPlace.rotation + tileToJoin.rotation)%4 +4)%4];
    }

    private Tile[] getNeighbouringTiles() {
        Vector2 mousePosition = mouseInput();
        mousePosition.sub(NeededConstants.origin);
        mousePosition.sub(tileSize/2, tileSize/2);
        mousePosition.mul(NeededConstants.newBaseInvert);
        int x = Math.round(mousePosition.x);
        int y = Math.round(mousePosition.y);
        return new Tile[]{
                getTile(new Vector2(x,y-1).mul(NeededConstants.newBase).add(NeededConstants.origin).add(tileSize/2, tileSize/2)),
                getTile(new Vector2(x+1,y).mul(NeededConstants.newBase).add(NeededConstants.origin).add(tileSize/2, tileSize/2)),
                getTile(new Vector2(x,y+1).mul(NeededConstants.newBase).add(NeededConstants.origin).add(tileSize/2, tileSize/2)),
                getTile(new Vector2(x-1,y).mul(NeededConstants.newBase).add(NeededConstants.origin).add(tileSize/2, tileSize/2)),
        };
    }

    private boolean noOverlap() {
        for (Tile tile : tileList) {
            if (!tile.equals(this) && ((tile.x < mouseInput().x) && (mouseInput().x < tile.x + tileSize) && (tile.y < mouseInput().y) && (mouseInput().y < tile.y + tileSize))) {
                return false;
            }
        }
        return true;
    }
    public boolean canPlaceThere() {
        Tile[] neighbors = getNeighbouringTiles();
        int index = ((entrance+rotation)%4 + 4)%4;
        boolean temp = isValidPlacement(this, neighbors[index]) && noOverlap();
        if (temp) {
            entranceCase.caseList[index] = neighbors[index].exitCases[(index+2) % 4];
            neighbors[index].exitCases[(index+2)%4].caseList[(index+2)%4] = entranceCase;
            Tile tempTile;
            int indexPrime;
//            for (int i = 0; i <= 3; i ++) {
//                tempTile = neighbors[i];
//                index = ((i+rotation)%4 + 4)%4;
//                if (exits[index] &&  tempTile != null && tempTile.exits[((2+i - tempTile.rotation)%4 +4)%4]) {
//                    indexPrime = ((2+i - tempTile.rotation)%4 +4)%4;
//                    exitCases[index].caseList[i] = tempTile.exitCases[indexPrime];
//                    tempTile.exitCases[indexPrime].caseList[indexPrime] = exitCases[index];
//                }
//            }
        }
        return temp;
    }
}
