package com.screens.game.hud;

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
import com.multiplayer.messages.TextMessage;
import com.screens.BaseScreen;
import com.screens.GameScreens;
import com.screens.MagicGame;
import com.screens.game.BaseActor;
import com.utils.Functions;
import com.utils.Multiplayer;

import static com.utils.Multiplayer.courrier;
import static com.utils.TileAndCases.queue;
import static com.utils.TileAndCases.tileSize;

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

    public void mute() {
        redCross.setVisible(true);
    }
    public void unmute() {
        redCross.setVisible(false);
    }

    //constructeur
    public GameInterface(MagicGame g){
        super(g);
    }

    public void create() {
        uiStage = GameScreens.gameScreen.getUiStage();
        mainStage = GameScreens.gameScreen.getMainStage();

        //il faut créer tous les éléments de l'interface

        //d'abord les 2 indicateurs de phase en haut à droite qu'on a scanné (un seul est visible à la fois)
        // pour indiquer la phase
        BaseActor phaseA = new BaseActor(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setSize(250,phaseA.getHeight()*250/ phaseA.getWidth());
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
        restart.setSize(100*restart.getWidth()/restart.getHeight(), 100);
        restart.setPosition(10,10);
        restart.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatars[0].wantsToRestart();
                        courrier.sendMessage(new TextMessage("wantsToRestart"));
                        return true;
                    }
                }
        );
        uiStage.addActor(restart);

        logs = new Logs(game.skin);
        logs.setSize(restart.getWidth(),600);
        logs.setPosition(10, restart.getHeight()+20);
        uiStage.addActor(logs);

        //la c'est juste des faux bouton
        BaseActor hourglass = new BaseActor(new Texture(Gdx.files.internal("interface/sablier.jpg")));
        hourglass.setSize(60,45);
        hourglass.setPosition(viewWidth - hourglass.getWidth(), viewHeight - phaseA.getHeight() - hourglass.getHeight());
        uiStage.addActor(hourglass);

        BaseActor volume = new BaseActor(new Texture(Gdx.files.internal("interface/haut-parleur.png")));
        volume.setSize(45,45);
        volume.setPosition(viewWidth - hourglass.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        voiceOn = true;
        uiStage.addActor(volume);

        redCross = new BaseActor(new Texture(Gdx.files.internal("interface/croix.png")));
        redCross.setSize(45,45);
        redCross.setPosition(viewWidth - hourglass.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        redCross.setVisible(false);
        uiStage.addActor(redCross);

        //Liste des joueurs, du moins leur affichage sur le HUD
        avatars = new PlayerOnHUD[Multiplayer.playerList.size()];

        for (int i = 0; i < Multiplayer.playerList.size(); i++) {
            avatars[i] = new PlayerOnHUD(Multiplayer.playerList.get(i));
            avatars[i].setPosition(viewWidth - avatars[i].getWidth() - 45, viewHeight - avatars[i].getHeight() - phaseA.getHeight() - volume.getHeight() - 10 - 135 * i);
            avatars[i].addToStage(uiStage);
        }

        // L'acteur qui va faire les pings
        ping = new BaseActor(new Texture(Gdx.files.internal("UserInterface/pingTransparentOverlay.png")));
        ping.setColor(1,1,1,0);
        ping.setTouchable(Touchable.disabled);
        uiStage.addActor(ping);
        pingSound = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/Ping.mp3"));
        // Vestige de l'époque où les joueurs sur les HUD étaient gérés à la main
        // ça c'était pour les placeholders
//        for (int i = Multiplayer.playerList.size(); i < 4; i++) {
//            avatars[i] = new BaseActor(new Texture(Gdx.files.internal("interface/kuro" + i + ".png")));
//            avatars[i].setSize(90, 90);
//            avatars[i].setPosition(viewWidth - avatars[i].getWidth() - 45, viewHeight - avatars[i].getHeight() - 225 - 135 * i);
//            uiStage.addActor(avatars[i]);
//            final int temp = i;
//            avatars[i].addListener(new InputListener() {
//                public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
//                    avatars[temp].addAction(Actions.sequence(
//                            Actions.color(new Color(1,0,0,1),(float)0.20),
//                            Actions.color(new Color(1,1,1,1),(float)0.20)));
//                    // What
//                    // the
//                    // fuck
//                    // LibGDX c'est cool jusqu'à ce que tu soit obligé de faire des trucs comme ça
//                    // Pour l'expliquer simplement: plutôt que de créer l'action en final en dehors de toute ça,
//                    // Il faut la créer nous même à chaque fois que l'inputListener est appellé
//                    // Désolé d'avoir craché sur les InputListener de LibGDX, ils sont très bien.
//                    return true;
//                }
//            });
//        }

        //la c'est le compteur de plaques restantes en bas a droite

        textTilesLeft = new Label(queue.textTileLeft, game.skin, "tilesLeftStyle");
//        textTilesLeft.setFontScale(0.8f);
        textTilesLeft.setPosition(viewWidth - tileSize - textTilesLeft.getWidth()/2 - 50, 5);
        uiStage.addActor(textTilesLeft);

        // Vestige de l'époque où on devait charger les pions à la main

//        loadPawnButton = new TextButton("Afficher le pion "+ getColor(currentColor), game.skin, "uiTextButtonStyle");
//        loadPawnButton.addListener(new InputListener() {
//            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
//                return true;
//            }
//            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
//                if (!tileList.isEmpty()) {
//                    pawnList.add(new Pawn(currentColor));
//                    pawnList.get(currentColor).setFirstCase();
//                    pawnList.get(currentColor).load();
//                    currentColor ++;
//                    loadPawnButton.setText("Afficher le pion "+ getColor(currentColor));
//                    if (currentColor >= 4) {
//                        loadPawnButton.remove();
//                    }
//                }
//
//            }
//        });
//        uiStage.addActor(loadPawnButton);
//        loadPawnButton.setPosition(10,1000);

        //Ici c'est le bordel rajouté par Nathan
        // Pour l'instant on touche pas à ça!!!!
        // ça fait des mois que c'est là, il faudrait songer à en faire quelque chose lol
//        Animatedhourglass = new AnimatedActor();
//        TextureRegion[] hourglassFrames = new TextureRegion[118];
//        for (int m = 1; m < 119; m++) {
//            String hourglassFileName = "GameUIAssets/hourglassAssets/frame(" + m +").gif";
//            Texture hourglassTex = new Texture(Gdx.files.internal(hourglassFileName));
//            hourglassTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//            hourglassFrames[m-1] = new TextureRegion(hourglassTex);
//        }
//        Array<TextureRegion> hourglassFramesArray = new Array<TextureRegion>(hourglassFrames);
//        anim = new Animation(0.1f, hourglassFramesArray, Animation.PlayMode.LOOP);
//        Animatedhourglass.setAnimation(anim);
//        Animatedhourglass.setOrigin(hourglass.getWidth() / 2, hourglass.getHeight() / 2);
//        Animatedhourglass.setPosition(200, 600);
//        mainStage.addActor(Animatedhourglass);

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
        pauseButton.setSize(pauseButton.getWidth()*phaseA.getHeight()/pauseButton.getHeight(), phaseA.getHeight());
        pauseButton.setPosition(phaseA.getX()-pauseButton.getWidth() - 10, 1080 - pauseButton.getHeight());
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
                        pauseOverlay.setVisible( isPaused() );
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
                        Functions.quit();
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

        // Overlay
//        uiTable.pad(10);
//        uiTable.add(pseudoLabel);
//        uiTable.add(currentAvatar).padLeft(50);
//        uiTable.add(pauseButton).expandX();
//        uiTable.row();
//        uiTable.add().colspan(3).expandY();


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


