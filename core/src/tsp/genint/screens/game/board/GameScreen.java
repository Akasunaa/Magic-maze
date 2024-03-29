package tsp.genint.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import tsp.genint.screens.BaseScreen;
import tsp.genint.screens.MagicGame;
import tsp.genint.screens.endings.VictoryScreen;
import tsp.genint.screens.game.BaseActor;
import tsp.genint.screens.game.hud.GameInterface;
import tsp.genint.screens.game.hud.MouseWheelChecker;
import tsp.genint.utils.Multiplayer;
import tsp.genint.utils.Color;
import tsp.genint.utils.TileAndCasesKt;

import java.util.ArrayList;

import static tsp.genint.screens.GameScreens.gameScreen;
import static tsp.genint.screens.GameScreens.victoryScreen;
import static tsp.genint.utils.FunctionsKt.mouseInput;
import static tsp.genint.utils.FunctionsKt.updateCamera;
import static tsp.genint.utils.MainConstants.*;
import static tsp.genint.utils.Multiplayer.cyclicBarrier;
import static tsp.genint.utils.Multiplayer.playerList;
import static tsp.genint.utils.TileAndCasesKt.tileSize;

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
        TileAndCasesKt.getPawnList().clear();
        TileAndCasesKt.getTileList().clear();
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

        background = new BaseActor(new Texture("UserInterface/backgroundCartoon.png"));
        background.setSize(1920,1080);

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = (OrthographicCamera) mainStage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 1;
        camera.position.set(1920f/2,1080f/2,0f);
        // Cette caméra nous sert à avoir le bon système de coordonées

//        Multiplayer.me.setPlayer(new Player(true, true, true, true, true,true, true));

        // Bon là c'est le batch et les trucs pour écrire, rien d'important
        batch = new SpriteBatch();
        BitmapFont font = getFontSize(40);
        Label.LabelStyle style = new Label.LabelStyle(font, com.badlogic.gdx.graphics.Color.BLACK);
        coordMouse = new Label(" ", style);
        coordMouse.setPosition(200, 50);
        coordMouse.setVisible(false);
        numberCase = new Label(" ", style);
        numberCase.setPosition(200,100);
        uiStage.addActor(coordMouse);
        uiStage.addActor(numberCase);

    }

    public void load() {
        for (Tile tile : TileAndCasesKt.getTileList()) {
            tile.load();
        }

        try {
            System.out.println("Client: Blocking in gameScreen.load()");
            cyclicBarrier.await();
            System.out.println("Client: Unblocking in gameScreen.load()");

        } catch (Exception e) {
            e.printStackTrace();
        }
        // On attends d'avoir recu la queue et les assignements

        gameInterface = new GameInterface(game);
        gameInterface.hasBackground = false;
        TileAndCasesKt.getQueue().setCoordinates(1920 - tileSize / 2 - 10, 10);
        TileAndCasesKt.getQueue().load();
        placeFirstTile();
    }

    private void placeFirstTile() {
        Tile temp = new Tile(1);
        temp.setX((1920 - tileSize) / 2f);
        temp.setY((1080 - tileSize) / 2f);
        TileAndCasesKt.getOrigin().add(temp.getX(), temp.getY());
        temp.load();
        TileAndCasesKt.getTileList().add(temp);
        for (Color color : Color.values()) {
            if (color == Color.NONE) return;
            Pawn tempPawn = new Pawn(color);
            Case newCase = temp.getCaseList().get(1 + (color.ordinal() / 2)).get(1 + (color.ordinal() % 2));
            tempPawn.setCase(newCase);
            tempPawn.load();
            TileAndCasesKt.getPawnList().add(tempPawn);
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
        updateCamera();
        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();
        background.draw(batch, 1);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        // On change le système de coordonées
        mainStage.draw();
        gameInterface.render(delta);

        batch.begin();
        //coordMouse.setText(stringMousePosition(camera) + "\n" + stringMousePosition((OrthographicCamera) uiStage.getCamera())); // On écrit les coordonées
        TileAndCasesKt.getQueue().handleInput(Multiplayer.me);

        for (Pawn pawn : TileAndCasesKt.getPawnList()) {
            if (pawn.getHasTarget()) pawn.interpolate(0.4f, Interpolation.bounce);
            pawn.handleInput(Multiplayer.me);
        }
        for (Pawn pawn : pawnToRemove) {
            TileAndCasesKt.getPawnList().remove(pawn);
        }
        if (TileAndCasesKt.getQueue().toRemove) TileAndCasesKt.getQueue().remove();
        batch.end();

        if (!TileAndCasesKt.isInPhaseB()) {
            // Avant il y avait des choses différentes ici, d'où le double if
            // Je le garde pour le else
            if (TileAndCasesKt.getNumberWeaponsRetrieved() == 4) {
                gameInterface.instrumental.dispose();
                instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/PhaseB.mp3"));
                instrumental.setLooping(true);
                instrumental.setVolume(game.audioVolume);
                instrumental.play();
                TileAndCasesKt.setInPhaseB(true);
                GameInterface.logs.clear();
                GameInterface.logs.newMessage("PASSAGE A LA PHASE B");
                //numberPawnsOut = 0;
                // S'ils ont tous leurs armes, on commence la phase B
                for (Pawn pawn : TileAndCasesKt.getPawnList()) {
                    pawn.unlock();
                    // Parce que les pions se bloquent lorsqu'ils récupérent leur arme
                }
            }
        }
        else {
            // Si on est en phase B
            // Once again, je pourrais faire ça de manière plus propre mais meh
            if (TileAndCasesKt.getNumberPawnsOut() == 4) {
                victoryScreen = new VictoryScreen(game);
                game.setScreen(victoryScreen);

            }
        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        gameInterface.dispose();
        for (Tile tile : TileAndCasesKt.getTileList()) {
            tile.dispose();
        }
        for (Pawn pawn : TileAndCasesKt.getPawnList()) {
            pawn.dispose();
        }
    }
}
