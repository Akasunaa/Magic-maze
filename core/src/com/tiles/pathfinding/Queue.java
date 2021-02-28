package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;


public class Queue implements Serializable {
    // Structure classique pour une pile
    private Queue tail;
    public Tile head;

    // Le sprite sera celui de la tuile en haut de la pile
    private transient Sprite sprite;

    // Coordonées et taille
    private float x;
    private float y;
    private float size;

    // Booléens pour savoir si on est en train de placer la tuile, et si la liste est vide
    private boolean isMovable = false;
    private boolean isEmpty = false;

    // Un cooldown, c'est classique
    private long cooldown = 0L;

    public void setSize(float size) {
        this.size = size;
        updateSpriteSize();
    }

    private void updateSpriteSize() {
        sprite.setSize(size, size);
    }


    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
        updateCoordinates();
    }

    private void updateCoordinates() {
        sprite.setX(x);
        sprite.setY(y);
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
        load();
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

    public void load() { // Obligatoire pour la sérialization
        head.load();
        sprite = head.getSprite();
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    public void handleInput(float mouseX, float mouseY, ArrayList<Tile> tileList) {
        if (!isEmpty) {
            if (isMovable) {
                sprite.setX(mouseX - size / 2);
                sprite.setY(mouseY - size / 2);
                if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                    isMovable = false;
                    try {
                        tileList.add(head);
                        head.resize(0f); // Pour mettre à jour les cases, tu peux essayer sans pour voir ce que ça fait
                        head.startCooldown();
                        remove();
                    } catch (NullPointerException e) {
                        System.out.println("File vide !");
                        sprite = new Sprite(new Texture("tuiles/blueDot.png"));
                        updateSpriteSize();
                        updateCoordinates();
                        isEmpty = true;
                    }
                }
            }
            if (System.currentTimeMillis() - cooldown > 1000) { // Un cooldown d'une seconde
                isMovable = isMovable ||
                        (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) &&
                                (x < mouseX) && (mouseX < x + size &&
                                (y < mouseY) && (mouseY < y + size)));
                // Java est paresseux, donc tout ce qu'il y a après le || n'est pas vérifié
                // si isMovable est true
            }
        }
    }
}
