package com.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.screens.game.BaseActor;
import com.screens.game.hud.GameInterface;
import com.screens.game.hud.MouseWheelChecker;
import com.screens.BaseScreen;
import com.screens.MagicGame;
import com.utils.Colors;
import com.utils.Functions;
import com.utils.Multiplayer;

import java.util.ArrayList;

import static com.screens.GameScreens.gameScreen;
import static com.utils.Functions.mouseInput;
import static com.utils.MainConstants.*;
import static com.utils.Multiplayer.cyclicBarrier;
import static com.utils.Multiplayer.playerList;
import static com.utils.TileAndCases.*;

// btw, le fait que MainScreen soit dans le dossier multi c'est un peu chelou niveau orga non?

public class GameScreen extends BaseScreen {
    // Trucs de déboguages pour afficher les coordonées de la souris
    Label coordMouse;
    Label numberCase;

    GameInterface gameInterface;
    public GameInterface getInterface() {
        return gameInterface;
    }

    private final ArrayList<Pawn> pawnToRemove = new ArrayList<>();
    public void removePawn(Pawn pawn) {
        pawnToRemove.add(pawn);
    }


    public String stringMousePosition(OrthographicCamera camera) {
        return "x = " + (int) mouseInput(camera).x + "; y = " + (int) mouseInput(camera).y;
    }

    public GameScreen(MagicGame g) {
        super(g);
    }

    private boolean restart = false;
    public void setToRestart() {
        restart = true;
    }

    private void restart() {
        tileList.clear();
        pawnList.clear();
        for (Player player : playerList) {
            if (player.pawn != null) player.dropsPawn(player.pawn);
        }
        gameScreen = new GameScreen(game);
        gameScreen.load();
        game.setScreen(gameScreen);
    }
    BaseActor background;

    public void create(){
        InputMultiplexer inputMultiplexer = new InputMultiplexer(uiStage, mainStage, new MouseWheelChecker());
        Gdx.input.setInputProcessor(inputMultiplexer);

        background = new BaseActor(new Texture("GameUIAssets/floorboard.png"));

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = (OrthographicCamera) mainStage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1;
        camera.position.set(1920f/2,1080f/2,0f);
        // Cette caméra nous sert à avoir le bon système de coordonées

        tileList = new ArrayList<Tile>();

        Multiplayer.me.setPlayer(new Player(true, true, true, true, true,true, true));

        // Bon là c'est le batch et les trucs pour écrire, rien d'important
        batch = new SpriteBatch();
        BitmapFont font = getFontSize(40);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);
        coordMouse = new Label(" ", style);
        coordMouse.setPosition(200, 50);
        numberCase = new Label(" ", style);
        numberCase.setPosition(200,100);
        uiStage.addActor(coordMouse);
        uiStage.addActor(numberCase);

    }

    public void load() {
        for (Tile tile : tileList) {
            tile.load();
        }

        try {
            cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // On attends d'avoir la queue

        gameInterface = new GameInterface(game);
        gameInterface.hasBackground = false;
        queue.setCoordinates(1920 - tileSize / 2 - 20, 20);
        queue.load();
        placeFirstTile();
    }

    private void placeFirstTile() {
        Tile temp = new Tile(1);
        temp.x = (1920 - tileSize)/2f;
        temp.y = (1080 - tileSize)/2f;
        origin.add(temp.x, temp.y);
        temp.load();
        tileList.add(temp);
        for (int color : Colors.colors) {
            Pawn tempPawn = new Pawn(color);
            tempPawn.setCase(temp.caseList[1+(color/2)][1+(color%2)]);
            tempPawn.load();
            pawnList.add(tempPawn);
        }
    }

    @Override
    public void update(float dt) {
    }


    @Override
    public void render(float delta) {
        if (restart) {
            restart();
            return;
        }
        //Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Couleur d'arrière plan, et on clear tout
        Functions.updateCamera();
        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();
        background.draw(batch, 1);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        // On change le système de coordonées
        mainStage.draw();
        gameInterface.render(delta);

        batch.begin();
        coordMouse.setText(stringMousePosition(camera) + "\n" + stringMousePosition((OrthographicCamera) uiStage.getCamera())); // On écrit les coordonées
        queue.handleInput();

        for (Pawn pawn : pawnList) {
            if (pawn.hasTarget) pawn.interpolate(0.3f, Interpolation.bounce);
            pawn.handleInput(Multiplayer.me);
        }
        for (Pawn pawn : pawnToRemove) {
            pawnList.remove(pawn);
        }
        if (queue.toRemove) queue.remove();
        batch.end();

        if (!isInPhaseB) {
            // Avant il y avait des choses différentes ici, d'où le double if
            // Je le garde pour le else
            if (numberWeaponsRetrieved == 4) {
                gameInterface.instrumental.dispose();
                instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/PhaseB.mp3"));
                instrumental.setLooping(true);
                instrumental.setVolume(game.audioVolume);
                instrumental.play();
                isInPhaseB = true;
                //numberPawnsOut = 0;
                // S'ils ont tous leurs armes, on commence la phase B
                for (Pawn pawn : pawnList) {
                    pawn.unlock();
                    // Parce que les pions se bloquent lorsqu'ils récupérent leur arme
                }
            }
        }
        else {
            // Si on est en phase B
            // Once again, je pourrais faire ça de manière plus propre mais meh
            if (numberPawnsOut == 4) {
                //TODO Le Jeu est gagné
            }
        }


//            for (Tile tile : tileList) {
//                for (case : tile.caseList ) {
//                    if (case)
//                }
//            }


    }


    @Override
    public void dispose() {
        batch.dispose();
        gameInterface.dispose();
        for (Tile tile : tileList) {
            tile.dispose();
        }
        for (Pawn pawn : pawnList) {
            pawn.dispose();
        }
    }
}
