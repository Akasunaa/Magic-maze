package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.menu.BaseActor;
import com.utils.Functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static com.utils.GameScreens.mainScreen;
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.*;

public class Queue implements Serializable {
    // Structure classique pour une pile
    private Queue tail;
    public Tile head;
    public int length = 0;
    public String textTileLeft;
    private void updateText() {
        textTileLeft = "Tuiles restantes: " + length;
    }

    // Le sprite sera celui de la tuile en haut de la pile
    private transient BaseActor sprite;
    private transient BaseActor shown;

    // Coordonées et taille
    private float x;
    private float y;
    private float size = tileSize/2;
    private boolean isFirst = true;
    // Pour placer le premier

    // Booléens pour savoir si on est en train de placer la tuile, et si la liste est vide
    private boolean isMovable = false;
    private boolean isEmpty = false;

    // Booléen pour savoir si la prochaine carte est visible ou non
    private boolean isHidden = true;
    private transient BaseActor hidden;


    private void updateSpriteSize() {
        //sprite.setSize(size, size);
        hidden.setSize(size, size);
        shown.setSize(size, size);
    }


    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
        updateCoordinates();
    }

    private void updateCoordinates() {
        sprite.setPosition(x, y);
        hidden.setPosition(x, y);
        shown.setPosition(x, y);
    }


    private void add(Tile toAdd) {
        // On ajoute la tête
        tail = this.copy();
        head = toAdd;
    }

    private void remove() {
        // On enlève la tête, on devient la queue
        try {
            this.head = tail.head;
            this.tail = tail.tail;
            length -=1;
            updateText();
            // Et on recharge le sprite
            loadSprite();
        } catch (NullPointerException e) { // S'il n'y a pas de queue, c'est qu'elle est vide
            System.out.println("File vide !");
            sprite = new BaseActor();
            sprite.setTexture(new Texture("tuiles/blueDot.png"));
            isEmpty = true; // On fait plus rien pour le futur
            isHidden = false;
        }

        updateSpriteSize();
        updateCoordinates();
    }

    private Queue copy() {
        return new Queue(head, tail);
    } // Self explanatory

    Queue(Tile head, Queue tail) {
        // Création d'une file à partir de sa tête et de sa queue
        this.head = head;
        this.tail = tail;
        if (tail == null) {
            length = 1;
        }
        else length = 1 + tail.length;
        updateText();
    }

    Queue(int number) {
        // Création d'une pile de carte de manière aléatoire
        ArrayList<Tile> tempList = new ArrayList<Tile>();
        for (int i = 2; i <= number; i++) tempList.add(new Tile(i));
        Collections.shuffle(tempList);
        tail = null; // Utile ?
        for (Tile tile : tempList) add(tile);
        add(new Tile(1)); // On commence toujours par la case numéro 1 I guess
        length = number;
        updateText();
    }

    public void load() { // Serialization
        hidden = new BaseActor();
        hidden.setTexture(new Texture("tuiles/hiddenOrange.png"));
        mainScreen.getUiStage().addActor(hidden);
        shown = new BaseActor();
        mainScreen.getUiStage().addActor(shown);
        loadSprite();
        updateSpriteSize();
    }

    private void loadSprite() { // Obligatoire pour la sérialization
        head.load();
        sprite = head.getSprite();
        sprite.setVisible(false);
        shown.setTexture(sprite.region.getTexture());
        shown.setVisible(false);
    }

    public void draw() {
        if (isHidden) hidden.draw(batch, 1);
        else sprite.draw(batch, 1);
    }

    private void place(Vector2 mousePosition) {
        hidden.setVisible(true);
        tileList.add(head); // On pose la tuile
        head.x = mousePosition.x; //Bon c'est classique ça
        head.y = mousePosition.y;
        head.updateAll(); // Mise à jour
        head.startCooldown(); // On veut pas le blinking
        remove(); // Et on enlève la tête
    }

    public void handleInput() {
        // Uh, this is going to be fun
        Vector2 mousePositionStatic = Functions.mouseInput((OrthographicCamera) shown.getStage().getCamera());
        Vector2 mousePosition = Functions.mouseInput();
        // Je le sauvegarde parce qu'on va le modifier
        if (!isEmpty) { // On fait rien si elle est vide
            if (isMovable) { // Truc classique pour avoir deux comportements sur un seul objet
                shown.setVisible(false);
                sprite.setVisible(true);
                mousePosition.sub(tileSize / 2, tileSize / 2); // Pour que le sprite soit centré sur la souris
                sprite.setX(mousePosition.x); // On suit la souris
                sprite.setY(mousePosition.y);
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) head.rotate(+1);
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) head.rotate(-1);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {// si on sélectionne un endroit
                    if (isFirst) {
                        isMovable = false; // Voilà voilà
                        isHidden = true;
                        isFirst = false;
                        origin.add(mousePosition);// Si c'est la première, on stock ses coordonées
                        place(mousePosition); // On pose la tuile
                    } else if (head.canPlaceThere()) {
                        // Attention !!!
                        // canPlaceThere est une fonction qui place la tuile !!!!
                        // Elle ne fait pas que renvoyer un booléen !!!
                        isMovable = false; // Voilà voilà
                        isHidden = true;
                        Functions.snap(mousePosition); // Tu alignes les coordonées sur la "grille"
                        place(mousePosition);
                    }
                    else {
                        isMovable = false; // Voilà voilà
                        isHidden = true;
                        hidden.setVisible(true);
                        sprite.setVisible(false);
                    }
                }
            }
            isMovable = isMovable || !isHidden &&
                    (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                            (x < mousePositionStatic.x) && (mousePositionStatic.x < x + size &&
                            (y < mousePositionStatic.y) && (mousePositionStatic.y < y + size)));
            // Java est paresseux, donc tout ce qu'il y a après le || n'est pas vérifié
            // si isMovable est true
            if (isHidden && (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                    (x < mousePositionStatic.x) && (mousePositionStatic.x < x + size &&
                    (y < mousePositionStatic.x) && (mousePositionStatic.y < y + size)))) {
                isHidden = false;
                hidden.setVisible(false);
                shown.setVisible(true);
            }
        }
    }
}
