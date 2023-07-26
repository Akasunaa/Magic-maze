package tsp.genint.screens.game.hud;

//ça c'est réellement l'interface que j'ai fait

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import tsp.genint.multiplayer.messages.WantsToRestart;
import tsp.genint.screens.BaseScreen;
import tsp.genint.screens.GameScreens;
import tsp.genint.screens.MagicGame;
import tsp.genint.screens.game.BaseActor;
import tsp.genint.utils.Multiplayer;

import static tsp.genint.utils.FunctionsKt.quit;
import static tsp.genint.utils.Multiplayer.courrier;
import static tsp.genint.utils.TileAndCasesKt.getQueue;
import static tsp.genint.utils.TileAndCasesKt.tileSize;

public class GameInterface extends BaseScreen {
    private PlayerOnHUD[] avatars;

    //compteur de plaques restantes à placer
    private Label textTilesLeft;

    boolean isPhaseA;
    boolean voiceOn;

    //TextButton loadPawnButton;

    public static Logs logs;

    // Pour le "menu" de "pause" (en vrai à voir si c'est nécessaire et comment c'est géré avec le multi
    private Table pauseOverlay;

    private BaseActor ping;
    private Sound pingSound;
    public boolean needsToPing = false;
    private BaseActor redCross;

    private void ping() {
        ping.toFront();
        ping.addAction(Actions.sequence(
                Actions.color(new Color(1, 0, 0, 1), 0.20f),
                Actions.color(new Color(1, 0, 0, 0), 0.20f)));
        pingSound.play();
        needsToPing = false;
    }
    public void wantsToRestart(String pseudo) {
        for (PlayerOnHUD player : avatars) {
            if (player.getPseudo().equals(pseudo)) {
                player.wantsToRestart();
                return;
            }
        }
    }

    public void mute() {
        redCross.setVisible(true);
    }

    public void unmute() {
        redCross.setVisible(false);
    }

    //constructeur
    public GameInterface(MagicGame g) {
        super(g);
    }

    public void create() {
        uiStage = GameScreens.gameScreen.getUiStage();
        mainStage = GameScreens.gameScreen.getMainStage();

        //il faut créer tous les éléments de l'interface

        //d'abord les 2 indicateurs de phase en haut à droite qu'on a scanné (un seul est visible à la fois)
        // pour indiquer la phase
        BaseActor phaseA = new BaseActor(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setSize(tileSize / 2 + 10, phaseA.getHeight() * (tileSize / 2 + 10) / phaseA.getWidth());
        phaseA.setPosition(viewWidth - phaseA.getWidth(), viewHeight - phaseA.getHeight());
        isPhaseA = true;
        uiStage.addActor(phaseA);

        BaseActor phaseB = new BaseActor(new Texture(Gdx.files.internal("interface/phaseB.jpg")));
        phaseB.setSize(phaseA.getWidth(), phaseA.getHeight());
        phaseB.setPosition(viewWidth - phaseB.getWidth(), viewHeight - phaseB.getHeight());
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        //le bouton restart que j'ai pris sur internet, va falloir en refaire un
        BaseActor restart = new BaseActor(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setSize(100 * restart.getWidth() / restart.getHeight(), 100);
        restart.setPosition(10, 10);
        restart.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatars[0].wantsToRestart();
                        courrier.sendMessage(new WantsToRestart());
                        return true;
                    }
                }
        );
        uiStage.addActor(restart);

        logs = new Logs(game.skin);
        logs.setSize(restart.getWidth(), 600);
        logs.setPosition(10, restart.getHeight() + 20);
        uiStage.addActor(logs);

        //la c'est juste des faux bouton
        BaseActor hourglass = new BaseActor(new Texture(Gdx.files.internal("interface/sablier.png")));
        hourglass.setSize(phaseA.getWidth() / 2, hourglass.getHeight() * phaseA.getWidth() / (hourglass.getWidth() * 2));
        hourglass.setPosition(viewWidth - hourglass.getWidth(), viewHeight - phaseA.getHeight() - hourglass.getHeight());
        uiStage.addActor(hourglass);

        BaseActor volume = new BaseActor(new Texture(Gdx.files.internal("interface/audioOn.png")));
        volume.setSize(phaseA.getWidth() / 2, volume.getHeight() * phaseA.getWidth() / (volume.getWidth() * 2));
        volume.setPosition(viewWidth - hourglass.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        voiceOn = true;
        uiStage.addActor(volume);

        redCross = new BaseActor(new Texture(Gdx.files.internal("interface/croix.png")));
        redCross.setSize(redCross.getWidth() * volume.getHeight() / redCross.getHeight(), volume.getHeight());
        redCross.setPosition(volume.getX() - (redCross.getWidth() - volume.getWidth()) / 2, volume.getY());
        redCross.setVisible(false);
        uiStage.addActor(redCross);

        //Liste des joueurs, du moins leur affichage sur le HUD
        avatars = new PlayerOnHUD[Multiplayer.playerList.size()];

        float playerSize = 130;
        float rightPadding = 20;
        float originX = viewWidth - playerSize - rightPadding;
        float xStep = (phaseA.getWidth() - rightPadding) / 2;

        float upPadding = (viewHeight - phaseA.getHeight() - volume.getHeight() - tileSize / 2 - playerSize * 2) / 4;
        // En fait, c'est un calcul à la louche de l'espace restant
        // Je dit à la louche parce qu'on prends pas en compte la taille des sone de texte par exemple
        float originY = viewHeight - phaseA.getHeight() - volume.getHeight() - playerSize - upPadding;
        float yStep = upPadding + playerSize;

        for (int i = 0; i < Multiplayer.playerList.size(); i++) {
            avatars[i] = new PlayerOnHUD(Multiplayer.playerList.get(i), playerSize);
            avatars[i].setPosition(originX - xStep * ((float) i / 2), originY - yStep * (i % 2));
            avatars[i].addToStage(uiStage);
        }

        // L'acteur qui va faire les pings
        ping = new BaseActor(new Texture(Gdx.files.internal("UserInterface/pingTransparentOverlay.png")));
        ping.setColor(1, 1, 1, 0);
        ping.setTouchable(Touchable.disabled);
        uiStage.addActor(ping);
        pingSound = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/Ping.mp3"));

        textTilesLeft = new Label(getQueue().textTileLeft, game.skin, "tilesLeftStyle");
        textTilesLeft.setPosition(viewWidth - tileSize - textTilesLeft.getWidth() / 2 - 50, 5);
        uiStage.addActor(textTilesLeft);

        // Gestion de l'horloge
        Clock.clock = new Clock(game.skin);
        uiStage.addActor(Clock.clock);

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/PhaseA.mp3"));
        instrumental.setLooping(true);
        instrumental.setVolume(game.audioVolume);
        instrumental.play();

        Texture pauseTexture = new Texture(Gdx.files.internal("UserInterface/barsHorizontal.png"));
        game.skin.add("pauseImage", pauseTexture);
        Button.ButtonStyle pauseStyle = new Button.ButtonStyle();
        pauseStyle.up = game.skin.getDrawable("pauseImage");
        Button pauseButton = new Button(pauseStyle);
        pauseButton.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        togglePaused();
                        pauseOverlay.setVisible(isPaused());
                        return true;
                    }
                });
        pauseButton.setSize(pauseButton.getWidth() * phaseA.getHeight() / pauseButton.getHeight(), phaseA.getHeight());
        pauseButton.setPosition(phaseA.getX() - pauseButton.getWidth() - 10, 1080 - pauseButton.getHeight());
        uiStage.addActor(pauseButton);

        pauseOverlay = new Table();
        pauseOverlay.setFillParent(true);

        Stack stacker = new Stack();
        stacker.setFillParent(true);
        uiStage.addActor(stacker);
        stacker.add(uiTable);
        stacker.add(pauseOverlay);

        game.skin.add("white", new Texture(Gdx.files.internal("UserInterface/backgroundBlack.png")));
        Drawable pauseBackground = game.skin.newDrawable("white", new Color(0, 0, 0, 0.8f));

        Label pauseLabel = new Label("Paused", game.skin, "uiLabelStyle");
        TextButton resumeButton = new TextButton("Resume", game.skin, "uiTextButtonStyle");
        resumeButton.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                             int button) {
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        togglePaused();
                        pauseOverlay.toFront();
                        pauseOverlay.setVisible(isPaused());
                    }
                });
        TextButton quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                             int button) {
                        return true;
                    }

                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        dispose();
                        quit();
                    }
                });
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");

        // Pause overlay
        float w = resumeButton.getWidth();
        pauseOverlay.setBackground(pauseBackground);
        pauseOverlay.add(pauseLabel).pad(20);
        pauseOverlay.row();
        pauseOverlay.add(resumeButton);
        pauseOverlay.row();
        pauseOverlay.add(quitButton).width(w);
        pauseOverlay.row();
        pauseOverlay.add(volumeLabel).padTop(100);
        pauseOverlay.row();
        pauseOverlay.add(audioSlider).width(400);

        pauseOverlay.setVisible(false);
    }

    public void setText(String text) {
        if (text.equals("stop")) textTilesLeft.remove();
        else textTilesLeft.setText(text);
    }

    public void update(float dt) {
        Clock.clock.update(game);
        if (needsToPing) {
            ping();
        }
    }
}


