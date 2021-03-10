package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

// La caméra
import static com.utils.Functions.*;
import static com.utils.MainConstants.camera;

// La liste des tuiles affichées sur l'écran
import static com.utils.TileAndCases.tileList;
import static com.utils.TileAndCases.tileSize;

// le batch pour dessiner
import static com.utils.MainConstants.batch;

public class MainScreen implements Screen, InputProcessor {
    // Trucs de déboguages pour afficher les coordonées de la souris
    BitmapFont coordMouse;
    BitmapFont numberCase;

    // La liste des tuiles qu'on va afficher, et la pile de cartes
    Queue queue;

    // Le pion
    Pawn greenPawn;
    boolean pawnTime = false;

    // Le joueur
    Player player;

    // Pour la sérialisation
    ObjectMapper mapper = new ObjectMapper();


    public String stringMousePosition() {
        return "x = " + (int) mouseInput().x + "; y = " + (int) mouseInput().y;
    }

    MainScreen() {
        create();
    }

    Tile tempTile;
    // une tempTile on en aura besoin plus tard

    public void create() {
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Cette caméra nous sert à avoir le bon système de coordonées
        Gdx.input.setInputProcessor(this);
        // Pour le système de zoom, il écoute les actions de la souris
        // Le niveau est un InputListener, weirdly enough
        // Je ne vais pas critiquer les actions de Nathan, il doit y avoir une raison à cela

        tileList = new ArrayList();
        for (Tile tile : tileList) {
            tile.load();
        }

        player = new Player(true, true, false, false, false, false);

        queue = new Queue(9); // J'ai fait les cases uniquement jusqu'à la 9
        queue.load();
        queue.setCoordinates(1280f - tileSize - 50f, 50f);

        // Bon là c'est le batch et les trucs pour écrire, rien d'important
        batch = new SpriteBatch();
        coordMouse = new BitmapFont();
        numberCase = new BitmapFont();
        coordMouse.setColor(0, 0, 0, 1);
        numberCase.setColor(0, 0, 0, 1);
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
        Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Couleur d'arrière plan, et on clear tout
        batch.setProjectionMatrix(camera.combined);
        // On change le système de coordonées

        batch.begin();
        coordMouse.draw(batch, stringMousePosition() + "\n$origin", 700f, 150f); // On écrit les coordonées
        queue.draw();
        queue.handleInput();
        for (Tile tile : tileList) {
            tile.draw(); // On dessine la tuile
        }

        if (pawnTime) {
            greenPawn.draw();
            greenPawn.handleInput(player);
        } else {
            tempTile = getTile();
            if (tempTile != null) tempTile.handleInput(player, numberCase);
            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                pawnTime = true;
                greenPawn = new Pawn("green");
                greenPawn.setCase = tileList.get(0).caseList[0][0];
                greenPawn.load();
            }
        }

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

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }
}
