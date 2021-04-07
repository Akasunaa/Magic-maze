package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.menu.BaseActor;
import com.utils.Functions;
import com.utils.Multiplayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import static com.utils.Functions.findCase;
import static com.utils.GameScreens.mainScreen;
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.*;

public class Queue implements Serializable {
    // Structure classique pour une pile
    private Queue tail;
    public Tile head;
    public int length;
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
    public boolean isFirst = true;
    // Pour placer le premier

    // Booléens pour savoir si on est en train de placer la tuile, et si la liste est vide
    private boolean isMovable = false;
    private boolean isEmpty = false;

    // Booléen pour savoir si la prochaine carte est visible ou non
    private boolean isHidden = true;
    private transient BaseActor hidden;

    private int numberRevealsDown = 0;
    public void setNumberRevealsDown(int i) {
        numberRevealsDown = i;
    }

    private void updateSpriteSize() {
        //sprite.setSize(size, size);
        hidden.setSize(size, size);
        shown.setSize(size, size);
    }


    public void setCoordinates(float x, float y) {
        this.x = x;
        this.y = y;
        //updateCoordinates();
    }

    private void updateCoordinates() {
        sprite.setPosition(x, y);
        hidden.setPosition(x, y);
        shown.setPosition(x, y);
    }

    public void setSpritePosition(float x, float y) {
        sprite.setPosition(x,y);
    }

    public Vector2 getSpritePosition() {
        return new Vector2(sprite.getX(), sprite.getY());
    }


    private void add(Tile toAdd) {
        // On ajoute la tête
        tail = this.copy();
        head = toAdd;
    }

    public void remove() {
        toRemove = false;
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

    public Queue(String arg) {
        String[] args = arg.split(" ");
        tail = null;
        for (int i = args.length-1; i >=0; i--) {
            add(new Tile(Integer.parseInt(args[i])));
        }
        length = args.length;
        updateText();
    }

    public String serialize() {
        if (tail.head == null) {
            return String.valueOf(head.number);
        }
        else return head.number + " " + tail.serialize();
    }

    public void load() { // Serialization
        hidden = new BaseActor();
        hidden.setTexture(new Texture("tuiles/hiddenOrange.png"));
        hidden.setOrigin(size/2,size/2);
        mainScreen.getUiStage().addActor(hidden);
        shown = new BaseActor();
        shown.setOrigin(size/2,size/2);
        mainScreen.getUiStage().addActor(shown);
        loadSprite();
        updateSpriteSize();
        updateCoordinates();
        reveal();
    }

    private void loadSprite() { // Obligatoire pour la sérialization
        head.load();
        sprite = head.getSprite();
        sprite.setVisible(false);
        shown.setTexture(sprite.region.getTexture());
        if (isHidden) shown.setVisible(false);
        shown.setRotation(0);
    }

    public void draw() {
        if (isHidden) hidden.draw(batch, 1);
        else sprite.draw(batch, 1);
    }

    public void place(Vector2 mousePosition) {
        sprite.toBack();
        head.place();
        tileList.add(head); // On pose la tuile
        head.x = mousePosition.x; //Bon c'est classique ça
        head.y = mousePosition.y;
        head.updateAll(); // Mise à jour
        head.startCooldown(); // On veut pas le blinking
        toRemove = true; // Et on enlève la tête
    }
    public boolean toRemove = false;

    public void hide() {
        isHidden = true;
        hidden.setVisible(true);
        shown.setVisible(false);
        if (numberRevealsDown > 0) {
            numberRevealsDown --;
            reveal();
        }
    }

    public void reveal() {
        System.out.println("Revealing Queue");
        if (!isHidden) {
            numberRevealsDown ++;
        }
        isHidden = false;
        hidden.setVisible(false);
        shown.setVisible(true);
    }

    public void makingMovable() {
        shown.setVisible(false);
        sprite.setVisible(true);
        sprite.toFront();
    }

    public void rotate(int i) {
        head.rotate(i);
        shown.rotateBy(90 * i);
    }

    public void placeHandleAll(Vector2 mousePosition) {
        if (isFirst) {
            isFirst = false;
            origin.add(mousePosition);// Si c'est la première, on stock ses coordonées
            place(mousePosition); // On pose la tuile
            hide();
        } else if (head.canPlaceThere()) {
            Functions.snap(mousePosition); // Tu alignes les coordonées sur la "grille"
            place(mousePosition);
            hide();
        }
        else {
            shown.setVisible(true);
            sprite.setVisible(false);
        }
    }

    private boolean checkServerForPlacable() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Multiplayer.courrier.sendMessage("wantToPlaceTile none");
        try {
            Multiplayer.cyclicBarrier.await();
            // Pour synchroniser les threads
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Multiplayer.courrier.getAnswer();
    }

    private boolean checkServerForClickable() {
        Multiplayer.courrier.sendMessage("wantToTakeTile none");
        try {
            Multiplayer.cyclicBarrier.await();
            // Pour synchroniser les threads
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Multiplayer.courrier.getAnswer();
    }

    private int count = 2;
    public void handleInput() {
        // Uh, this is going to be fun
        Vector2 mousePositionStatic = Functions.mouseInput((OrthographicCamera) shown.getStage().getCamera());
        Vector2 mousePosition = Functions.mouseInput();
        // Je le sauvegarde parce qu'on va le modifier
        if (!isEmpty) { // On fait rien si elle est vide
            if (!isMovable && !isHidden &&
                    Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT) &&
                    (x < mousePositionStatic.x) && (mousePositionStatic.x < x + size) &&
                    (y < mousePositionStatic.y) && (mousePositionStatic.y < y + size) &&
                    checkServerForClickable()) {
                makingMovable();
                isMovable = true;
            }
            if (isMovable) { // Truc classique pour avoir deux comportements sur un seul objet
                mousePosition.sub(tileSize / 2, tileSize / 2); // Pour que le sprite soit centré sur la souris
                setSpritePosition(mousePosition.x,mousePosition.y); // On suit la souris
                if (count == 2) {
                    Multiplayer.courrier.sendMessage("movingTile " + mousePosition.x + " " + mousePosition.y);
                    count = 0;
                }
                count ++;
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    rotate(1);
                    Multiplayer.courrier.sendMessage("rotateTile 1");
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    rotate(-1);
                    Multiplayer.courrier.sendMessage("rotateTile -1");
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && checkServerForPlacable()) {// si on sélectionne un endroit
                    placeHandleAll(mousePosition);
                    isMovable = false;
                }
            }
        }
    }
}
