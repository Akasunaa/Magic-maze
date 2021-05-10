package com.tiles;

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
import com.menu.BaseActor;
import com.menu.BaseScreen;
import com.menu.GameInterface;
import com.menu.MagicGame;
import com.utils.Functions;
import com.utils.Multiplayer;

import java.util.ArrayList;

import static com.utils.Functions.mouseInput;
import static com.utils.MainConstants.*;
import static com.utils.TileAndCases.*;

// btw, le fait que MainScreen soit dans le dossier multi c'est un peu chelou niveau orga non?

public class MainScreen extends BaseScreen {
    // Trucs de déboguages pour afficher les coordonées de la souris
    Label coordMouse;
    Label numberCase;

    // Le pion
    Pawn greenPawn;
    boolean pawnTime = false;


    GameInterface gameInterface;

    public String stringMousePosition(OrthographicCamera camera) {
        return "x = " + (int) mouseInput(camera).x + "; y = " + (int) mouseInput(camera).y;
    }

    public MainScreen(MagicGame g) {
        super(g);


    }

    BaseActor background;

    public void create(){
        InputMultiplexer inputMultiplexer = new InputMultiplexer(uiStage, mainStage, new MouseWheelChecker());
        Gdx.input.setInputProcessor(inputMultiplexer);

        background = new BaseActor();
        background.setTexture(new Texture("GameUIAssets/floorboard.png"));

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = (OrthographicCamera) mainStage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 2;
        camera.position.set(1920f/2,1080f/2,0f);
        // Cette caméra nous sert à avoir le bon système de coordonées

        tileList = new ArrayList<Tile>();

        Multiplayer.me.setPlayer(new Player(true, true, true, true, false, true));

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

    public void load(float audioVolume) {
        for (Tile tile : tileList) {
            tile.load();
        }
        gameInterface = new GameInterface(game, audioVolume);
        gameInterface.hasBackground = false;
        queue.setCoordinates(1920 - tileSize / 2 - 20, 20);
        queue.load();
    }

    @Override
    public void update(float dt) {
    }


    @Override
    public void render(float delta) {
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
        if (queue.toRemove) queue.remove();
        batch.end();

        if (!isInPhaseB) {
            // Si on est encore en phase A
            int numberOfPawnReady = 0;
            for (Pawn pawn : pawnList) {
                if (pawn.hasWeapon) {
                    numberOfPawnReady += 1;
                }
            }
            // On check le nombre de pions qui ont leur armes

            if (numberOfPawnReady == 4) {
                gameInterface.instrumental.dispose();
                instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Musique_jeu_principal_phase_B.mp3"));
                instrumental.setLooping(true);
                instrumental.play();
                isInPhaseB = true;
            }
            // S'ils ont tous leurs armes, on commence la phase B
        }
        else {
            // Si on est en phase B
            int numberOfPawnOnExit = 0;
            for (Pawn pawn: pawnList) {
                if (pawn.onExit) {
                    numberOfPawnOnExit ++;
                }
            }
            // On check si les personnages sont sur la sortie finale
            if (numberOfPawnOnExit == 4) {
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
