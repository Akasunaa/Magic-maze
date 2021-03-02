package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static com.tiles.pathfinding.NeededConstants.*;


public class Queue implements Serializable {
    // Structure classique pour une pile
    private Queue tail;
    public Tile head;

    // Le sprite sera celui de la tuile en haut de la pile
    private transient Sprite sprite;

    // Coordonées et taille
    private float x;
    private float y;
    private float size = tileSize;
    private boolean isFirst = true;
    // Pour placer le premier

    // Booléens pour savoir si on est en train de placer la tuile, et si la liste est vide
    private boolean isMovable = false;
    private boolean isEmpty = false;

    // Booléen pour savoir si la prochaine carte est visible ou non
    private boolean isHidden = true;
    private transient Sprite hidden;


    private void updateSpriteSize() {
        sprite.setSize(size, size);
        hidden.setSize(size, size);
    }


    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
        updateCoordinates();
    }

    private void updateCoordinates() {
        sprite.setX(x);
        sprite.setY(y);
        hidden.setX(x);
        hidden.setY(y);
    }


    private void add(Tile toAdd) {
        // On ajoute la tête
        tail = this.copy();
        head = toAdd;
    }

    private void remove() {
        // On enlève la tête, on devient la queue
        this.head = tail.head;
        this.tail = tail.tail;

        // Et on recharge le sprite
        loadSprite();
        this.updateSpriteSize();
        this.updateCoordinates();
    }

    private Queue copy() {
        return new Queue(head, tail);
    } // Self explanatory

    Queue(Tile head, Queue tail) {
        // Création d'une file à partir de sa tête et de sa queue
        this.head = head;
        this.tail = tail;
    }

    Queue(int number) {
        // Création d'une pile de carte de manière aléatoire
        ArrayList<Tile> tempList = new ArrayList<Tile>();
        for (int i = 2; i <= number; i++) tempList.add(new Tile(i));
        Collections.shuffle(tempList);
        tail = null; // Utile ?
        for (Tile tile : tempList) add(tile);
        add(new Tile(1)); // On commence toujours par la case numéro 1 I guess
    }

    public void load() { // Serialization
        hidden = new Sprite(new Texture("tuiles/hiddenOrange.png"));
        loadSprite();
        updateSpriteSize();
    }

    private void loadSprite() { // Obligatoire pour la sérialization
        head.load();
        sprite = head.getSprite();
    }

    public void draw() {
        if (isHidden) hidden.draw(NeededConstants.batch);
        else sprite.draw(NeededConstants.batch);
    }

    public void handleInput() {
        // Uh, this is going to be fun
        Vector2 mousePosition = mouseInput();
        // Je le sauvegarde parce qu'on va le modifier
        if (!isEmpty) { // On fait rien si elle est vide
            if (isMovable) { // Truc classique pour avoir deux comportements sur un seul objet
                mousePosition.sub(tileSize/2,tileSize/2); // Pour que le sprite soit centré sur la souris
                sprite.setX(mousePosition.x); // On suit la souris
                sprite.setY(mousePosition.y);
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // si on sélectionne un endroit
                    isMovable = false; // Voilà voilà
                    isHidden = true;
                    try {
                        tileList.add(head); // On pose la tuile
                        if (isFirst) {
                            origin.add(mousePosition); // Si c'est la première, on stock ses coordonées
                            isFirst = false; // Et c'est plus la rmière après, logique
                        }
                        else {
                            snap(mousePosition); // sinon, tu alignes les coordonées sur la "grille"
                        }
                        head.x = mousePosition.x; //Bon c'est classique ça
                        head.y = mousePosition.y;
                        head.updateAll(); // Mise à jour
                        head.startCooldown(); // On peut pas le blinking
                        remove(); // Et on enlève la tête
                    } catch (NullPointerException e) { // si elle est vide
                        System.out.println("File vide !");
                        sprite = new Sprite(new Texture("tuiles/blueDot.png"));
                        updateSpriteSize();
                        updateCoordinates();
                        isEmpty = true; // On fait plus rien pour le futur
                        isHidden = false;
                    }
                }
            }
            isMovable = isMovable || !isHidden &&
                    (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                            (x < mousePosition.x) && (mousePosition.x < x + size &&
                            (y < mousePosition.y) && (mousePosition.y < y + size)));
            // Java est paresseux, donc tout ce qu'il y a après le || n'est pas vérifié
            // si isMovable est true
            if (isHidden && (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                    (x < mousePosition.x) && (mousePosition.x < x + size &&
                    (y < mousePosition.x) && (mousePosition.y < y + size)))) {
                isHidden = false;
            }
        }
    }
}
