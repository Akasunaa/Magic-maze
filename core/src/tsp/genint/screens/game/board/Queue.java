package tsp.genint.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import tsp.genint.multiplayer.messages.*;
import tsp.genint.screens.game.BaseActor;
import tsp.genint.utils.FunctionsKt;
import tsp.genint.utils.Multiplayer;
import tsp.genint.utils.TileAndCasesKt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static tsp.genint.screens.GameScreens.gameScreen;

public class Queue implements Serializable {
    // Structure classique pour une pile
    private Queue tail;
    Tile head;
    private int numberTilesLeft;
    public String textTileLeft;
    private final float size = TileAndCasesKt.tileSize / 2;

    // Le sprite sera celui de la tuile en haut de la pile
    private transient BaseActor sprite;
    private transient BaseActor shown;

    // Coordonées et taille
    private float x;
    private float y;

    private void updateText() {
        if (numberTilesLeft > 0) textTileLeft = "Tuiles restantes: " + numberTilesLeft;
        try {
            gameScreen.getInterface().setText(textTileLeft);
        } catch (NullPointerException ignored) {
        }
    }
    public boolean isFirst = false;
    // Pour placer le premier

    // Booléens pour savoir si on est en train de placer la tuile, et si la liste est vide
    private boolean isMovable = false;
    boolean isEmpty = false;

    // Booléen pour savoir si la prochaine carte est visible ou non
    private boolean isHidden = true;
    private transient BaseActor hidden;

    private int numberRevealsDown = 0;
    public void setNumberRevealsDown(int i) {
        // Utilisé pour du déboguage par le passé
        numberRevealsDown = i;
    }

    private void updateSpriteSize() {
        //sprite.setSize(size, size);
        hidden.setSize(size, size);
        shown.setSize(size, size);
    }


    void setCoordinates(float x, float y) {
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

    void remove() {
        toRemove = false;
        numberTilesLeft -=1;
        // On enlève la tête, on devient la queue
        if (numberTilesLeft > 0) {
            this.head = tail.head;
            this.tail = tail.tail;
            System.out.println("Client : Removed a Tile, new Tile is number " + head.getNumber());
            updateText();
            // Et on recharge le sprite
            loadSprite();
            updateSpriteSize();
            updateCoordinates();
        } else { // S'il n'y a pas de queue, c'est qu'elle est vide
            System.out.println("Client : File vide !");
            hidden.setVisible(false);
            shown.setVisible(false);
            isEmpty = true; // On fait plus rien pour le futur
            isHidden = false;
            textTileLeft = "stop";
        }
    }

    private Queue copy() {
        return new Queue(head, tail);
    } // Self explanatory

    private Queue(Tile head, Queue tail) {
        // Création d'une file à partir de sa tête et de sa queue
        this.head = head;
        this.tail = tail;
        if (tail == null) {
            numberTilesLeft = 0;
        }
        else numberTilesLeft = 1 + tail.numberTilesLeft;
    }

    public Queue(int number) {
        // Création d'une pile de carte de manière aléatoire
        ArrayList<Tile> tempList = new ArrayList<Tile>();
        for (int i = 2; i <= number; i++) tempList.add(new Tile(i));
        Collections.shuffle(tempList);
        tail = null; // Utile ?
        for (Tile tile : tempList) add(tile);
        //add(new Tile(1)); // On commence toujours par la case numéro 1 I guess
        numberTilesLeft = number-1;
        updateText();
    }

    public Queue(String arg) {
        String[] args = arg.split(" ");
        tail = null;
        for (int i = args.length-1; i >=0; i--) {
            add(new Tile(Integer.parseInt(args[i])));
        }
        numberTilesLeft = args.length;
        updateText();
    }

    public String serialize() {
        if (tail.head == null) {
            return String.valueOf(head.getNumber());
        } else return head.getNumber() + " " + tail.serialize();
    }

    void load() { // Serialization
        hidden = new BaseActor(new Texture("Game/Tiles/hiddenOrange.png"));
        hidden.setOrigin(size/2,size/2);
        gameScreen.getUiStage().addActor(hidden);
        loadSprite();
        updateSpriteSize();
        updateCoordinates();
//        reveal();
    }

    private void loadSprite() { // Obligatoire pour la sérialization
        head.load();
        sprite = head.getSprite();
        sprite.setVisible(false);
        shown = new BaseActor(sprite.getTexture());
        shown.setOrigin(size/2,size/2);
        gameScreen.getUiStage().addActor(shown);
        if (isHidden) shown.setVisible(false);
        shown.setRotation(0);
    }

    public boolean toRemove = false;

    void hide() {
        isHidden = true;
        hidden.setVisible(true);
        shown.setVisible(false);
        if (numberRevealsDown > 0) {
            numberRevealsDown --;
            reveal();
        }
    }

    void reveal() {
        System.out.println("Client : Revealing Queue");
        if (!isHidden) {
            numberRevealsDown ++;
        }
        isHidden = false;
        hidden.setVisible(false);
        shown.setVisible(true);
    }

    // De manière assez amusante, seule la pile appelle hide, alors que reveal est appellé ailleurs

    public void makingMovable() {
        // Le genre de méthode qu'on doit créer pour gérer le multijoueur plus facilement
        // C'est utilisé dans Decryptor
        shown.setVisible(false);
        sprite.setVisible(true);
        sprite.toFront();
    }

    public void rotate(int i) {
        head.rotate(i);
        shown.rotateBy(90 * i);
    }

    public void place(Vector2 mousePosition) {
        FunctionsKt.snap(mousePosition); // Tu alignes les coordonées sur la "grille"
        sprite.toBack();
        head.place();
        TileAndCasesKt.getTileList().add(head); // On pose la tuile
        head.setX(mousePosition.x); //Bon c'est classique ça
        head.setY(mousePosition.y);
        head.updateAll(); // Mise à jour
        head.startCooldown(); // On veut pas le blinking
        toRemove = true; // Et on enlève la tête
        hide();
    }

    private boolean checkServerForPlacable() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Multiplayer.courrier.sendMessage(new WantToPlaceTile());
        Multiplayer.courrier.resetAnswer();
        try {
            System.out.println("Blocking in Queue Place");
            Multiplayer.cyclicBarrier.await(500, TimeUnit.MILLISECONDS);            // Pour synchroniser les threads
            System.out.println("Unblocking in Queue Place");
        } catch (TimeoutException e) {
            Multiplayer.cyclicBarrier.reset();
            return false;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return Multiplayer.courrier.getAnswer();
    }

    private boolean checkServerForClickable() {
        Multiplayer.courrier.sendMessage(new WantToTakeTile());
        Multiplayer.courrier.resetAnswer();
        try {
            System.out.println("Blocking in Queue Click");
            Multiplayer.cyclicBarrier.await(500, TimeUnit.MILLISECONDS);            // Pour synchroniser les threads
            System.out.println("Unblocking in Queue Click");
        } catch (TimeoutException e) {
            Multiplayer.cyclicBarrier.reset();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Multiplayer.courrier.getAnswer();
    }

    public void reset() {
        isMovable = false;
        shown.setVisible(true);
        sprite.setVisible(false);
    }
    private int count = 2;
    public void handleInput(Player player) {
        // Uh, this is going to be fun
        // Edit genre trois (3) mois après avoir écrit ce code ? Sans doute beaucoup plus
        // Hum C'était beaucoup, beaucoup plus simple que ce qui te restait à faire, jeune Hadrien
        // Et ouais, le placement des tuiles c'était drôle, mais faire le multijoueur, ça l'était encore plus
        Vector2 mousePositionStatic = FunctionsKt.mouseInput((OrthographicCamera) shown.getStage().getCamera());
        Vector2 mousePosition = FunctionsKt.mouseInput();
        // Je le sauvegarde parce qu'on va le modifier
        // Et forcément il faut deux vecteurs différents, parce qu'il nous faut les coordonées
        // dans le référentiel du HUD et dans le reférentiel du plateau
        if (!isEmpty) { // On fait rien si elle est vide
            if (isMovable) {
                // Truc classique pour avoir deux comportements sur un seul objet
                // Edit: j'ai écrit ce code peu après avoir démarré LibGDX
                // Et en fait c'est pas du tout un truc classique, enfin je disais ça parce que
                // c'était une solution simple à laquelle on peut facilement penser
                // Cependant, j'ai pas trouvé de meilleur solution depuis, et s'il y en a une, je ne sais pas
                // Si elle vaut le coup de remplacer celle là
                mousePosition.sub(TileAndCasesKt.tileSize / 2, TileAndCasesKt.tileSize / 2); // Pour que le sprite soit centré sur la souris
                setSpritePosition(mousePosition.x, mousePosition.y); // On suit la souris
                if (count == 2) {
                    Multiplayer.courrier.sendMessage(new MovingTile(mousePosition));
                    count = 0;
                }
                count ++;
                // Gestion de la rotation
                if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
                    rotate(1);
                    Multiplayer.courrier.sendMessage(new RotateTile(1));
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {
                    rotate(-1);
                    Multiplayer.courrier.sendMessage(new RotateTile(-1));
                }
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    if (head.canPlaceThere() && checkServerForPlacable()) {// si on sélectionne un endroit
                        place(mousePosition);
                        isMovable = false;
                    } else {
                        reset();
                        Multiplayer.courrier.sendMessage(new DroppedTile());
                    }
                }
            }
            else {// Beaucoup de Booléen donc je vais préciser
                if (!isHidden && // Si elle n'est pas bougeable, révélée
                        Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && // Qu'on clique droit
                        (x < mousePositionStatic.x) && (mousePositionStatic.x < x + size) && // Qu'on est dessus
                        (y < mousePositionStatic.y) && (mousePositionStatic.y < y + size) &&
                        player.cardChooser &&
                        checkServerForClickable()) { // Et qu'on a le droit
                    makingMovable(); // Alors c'est bon
                    setSpritePosition(FunctionsKt.mouseInput().x - TileAndCasesKt.tileSize / 2, FunctionsKt.mouseInput().y - TileAndCasesKt.tileSize / 2);
                    isMovable = true;
                }
            }
        }
    }
}
