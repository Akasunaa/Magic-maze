package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ibm.gpu.Maths;
import com.menu.BaseActor;
import com.menu.GameInterface;
import com.menu.BaseScreen;
import com.menu.MagicGame;


import java.util.ArrayList;

// La caméra
import static com.utils.Functions.*;
import static com.utils.MainConstants.camera;

// La liste des tuiles affichées sur l'écran

// le batch pour dessiner
import static com.utils.MainConstants.batch;
import static com.utils.TileAndCases.*;

public class MainScreen extends BaseScreen {
    // Trucs de déboguages pour afficher les coordonées de la souris
    BitmapFont coordMouse;
    BitmapFont numberCase;


    // Le pion
    Pawn greenPawn;
    boolean pawnTime = false;

    // Le joueur
    Player player;

    // Pour la sérialisation
    ObjectMapper mapper = new ObjectMapper();

    GameInterface gameInterface;


    public String stringMousePosition(OrthographicCamera camera) {
        return "x = " + (int) mouseInput(camera).x + "; y = " + (int) mouseInput(camera).y;
    }

    public MainScreen(MagicGame g) {
        super(g);
        create();
    }

    Tile tempTile;
    // une tempTile on en aura besoin plus tard

    BaseActor background;

    public void create() {
        background = new BaseActor();
        background.setTexture(new Texture("GameUIAssets/floorboard.png"));
        background.setScale(1.5f);

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = (OrthographicCamera) mainStage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Cette caméra nous sert à avoir le bon système de coordonées
        Gdx.input.setInputProcessor(this);
        // Pour le système de zoom, il écoute les actions de la souris
        // Le niveau est un InputListener, weirdly enough
        // Je ne vais pas critiquer les actions de Nathan, il doit y avoir une raison à cela

        tileList = new ArrayList();

        player = new Player(true, true, false, false, false, false);

        queue = new Queue(9); // J'ai fait les cases uniquement jusqu'à la 9


        // Bon là c'est le batch et les trucs pour écrire, rien d'important
        batch = new SpriteBatch();
        coordMouse = new BitmapFont();
        numberCase = new BitmapFont();
        coordMouse.setColor(0, 0, 0, 1);
        numberCase.setColor(0, 0, 0, 1);
    }

    public void load() {
        for (Tile tile : tileList) {
            tile.load();
        }
        gameInterface = new GameInterface(game);
        queue.load();
        queue.setCoordinates(1280f - tileSize - 50f, 50f);
        // A gere pour pouvoir le faire sans avoir load
    }

    @Override
    public void update(float dt) {
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("Scrolled by " + amountX + " and " + amountY);
        if (camera.zoom >= 0.2 || amountY >= 0) camera.zoom += amountY * 0.1;
        camera.update();
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Couleur d'arrière plan, et on clear tout
        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();
        background.draw(batch, 1);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        // On change le système de coordonées
        mainStage.draw();
        gameInterface.render(delta);

        batch.begin();
        coordMouse.draw(batch,
                stringMousePosition(camera) + "\n" + stringMousePosition((OrthographicCamera) uiStage.getCamera()),
                700f, 150f); // On écrit les coordonées
        queue.handleInput();

//        if (pawnTime) {
//            greenPawn.draw();
//            greenPawn.handleInput(player);
//        } else {
//            tempTile = getTile();
//            if (tempTile != null) tempTile.handleInput(player, numberCase);
//            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//                pawnTime = true;
//                greenPawn = new Pawn("green");
//                greenPawn.setCase = tileList.get(0).caseList[0][0];
//                greenPawn.load();
//            }
//        }
        // Gestion du déplacement de la caméra
        updateCamera();
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Tile tile : tileList) {
            tile.dispose();
        }
        greenPawn.dispose();
    }
}
