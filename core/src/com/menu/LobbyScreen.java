package com.menu;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.ai.btree.Task;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Timer;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.multiplayer.Client;
import com.multiplayer.Courrier;
import com.multiplayer.ServerMaker;
import com.multiplayer.ServerNotReachedException;
import com.tiles.MainScreen;
import com.tiles.MouseWheelChecker;
import com.tiles.Queue;
import com.utils.Multiplayer;

import static com.utils.Functions.modulo;
import static com.utils.GameScreens.mainScreen;
import static com.utils.TileAndCases.queue;

public class LobbyScreen extends BaseScreen {

    //Button leftButton;

    private Table optionOverlay;
    private PlayerMaker[] playerMakerList;

    public LobbyScreen(MagicGame g, float audioVolume) {
        super(g);

        instrumental.setVolume(audioVolume);
        audioSlider.setValue(audioVolume);
    }

    public void create() {
        // pour le son
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/MusicMenu.wav"));
        //audioVolume = 0.70f;
        //instrumental.setLooping(true);
        //instrumental.setVolume(audioVolume);
        //instrumental.play();

        final BaseActor background = new BaseActor();
        background.setTexture(new Texture(Gdx.files.internal("MenuAssets/BlurryMallBackground.jpg")));
        uiStage.addActor(background);

        final BaseActor transparentForeground = new BaseActor();
        transparentForeground.setTexture(new Texture(Gdx.files.internal("MenuAssets/Black.gif")));
        transparentForeground.setSize(1920, 1080);
        transparentForeground.setColor(0, 0, 0, 0);
        transparentForeground.setTouchable(Touchable.disabled);
        uiStage.addActor(transparentForeground);

        background.toBack();

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        TextButton startButton = new TextButton("Demarrer la partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                transparentForeground.setTouchable(Touchable.enabled);

                Action fadeToBlack = Actions.sequence(
                        //Actions.alpha(1f), // set transparency value
                        Actions.show(), // set visible to true
                        Actions.forever(
                                Actions.sequence(
                                        // color shade to approach, duration
                                        Actions.color(new Color(0, 0, 0, 1), 2)

                                )
                        )
                );
                transparentForeground.addAction(fadeToBlack);

                float delay = 2; // seconds

                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        dispose();
                        //mainScreen = new MainScreen(game, AvatarNumbers, playerNames, audioVolume);
                        //TODO("Fix this shit")
                        //mainScreen.load();
                        //game.setScreen( mainScreen );
                    }
                }, delay);
            }
        });

        startButton.addListener(new InputListener() {
            boolean playing = false;

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);

                if (!playing) {
                    buttonHover.play();
                    playing = true;
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                playing = false;
            }
        });
        startButton.getLabel().setTouchable(Touchable.disabled);

        TextButton quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        TextButton optionButton = new TextButton("Options", game.skin, "uiTextButtonStyle");
        optionButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(true);
            }
        });

        Label optionLabel = new Label("Options :", game.skin, "uiLabelStyle");
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");

        TextButton returnButton = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(false);
            }
        });

        final Slider audioSlider = new Slider(0, 1, 0.005f, false, game.skin, "uiSliderStyle");
        audioSlider.setValue(audioVolume);
        audioSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                audioVolume = audioSlider.getValue();
                instrumental.setVolume(audioVolume);
            }
        });

        optionOverlay = new Table();
        optionOverlay.setFillParent(true);
        optionOverlay.setVisible(false);
        optionOverlay.add(optionLabel).pad(100);
        optionOverlay.row();
        optionOverlay.add(volumeLabel).padBottom(20);
        optionOverlay.row();
        optionOverlay.add(audioSlider).width(400).padBottom(50);
        optionOverlay.row();
        optionOverlay.add(returnButton);

        Stack stacker = new Stack();
        stacker.setFillParent(true);
        uiStage.addActor(stacker);
        stacker.add(uiTable);
        stacker.add(optionOverlay);

        game.skin.add("white", new Texture(Gdx.files.internal("GameUIAssets/white4px.png")));
        Drawable optionBackground = game.skin.newDrawable("white", new Color(0, 0, 0, 0.8f));

        optionOverlay.setBackground(optionBackground);

        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
        playerMakerList = new PlayerMaker[]{
                new PlayerMaker("Joueur 1", animalNames[0], uiSkin, true),
                new PlayerMaker("Joueur 2", animalNames[1], uiSkin, false),
                new PlayerMaker("Joueur 3", animalNames[2], uiSkin, false),
                new PlayerMaker("Joueur 4", animalNames[3], uiSkin, false)
        };

        uiTable.pad(20);
        uiTable.add(quitButton).colspan(12).right().expandX();
        ;
        uiTable.row();
        for (PlayerMaker temp : playerMakerList) temp.addTextField(uiTable);
        uiTable.row();
        for (PlayerMaker temp : playerMakerList) {
            temp.load(uiSkin, uiTable);
        }
        uiTable.row();
        uiTable.add(optionButton).center().colspan(12).padTop(150);
        uiTable.row();
        uiTable.add(startButton).center().colspan(12).padTop(100);
        uiTable.add().center().padTop(20);
        uiTable.row();

        transparentForeground.toFront();
    }

    public void multiplayerShenanigans() throws ServerNotReachedException {
        if (Multiplayer.isServer) {
            new ServerMaker(Multiplayer.port, Multiplayer.clientList).startThread();
        }
        Multiplayer.courrier = new Courrier(Multiplayer.me.pseudo, Multiplayer.port, Multiplayer.serverIP);
        try {
            Multiplayer.cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (Multiplayer.isServer) {
            queue = new Queue(9); // J'ai fait les cases uniquement jusqu'à la 9
            for (Client client : Multiplayer.clientList.clientList) {
                client.sendMessage("sending Queue");
                client.sendClearMessage(queue.serialize());
            }
        }
        try {
            Multiplayer.cyclicBarrier.await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Getting over it");

        // On met ça ici parce que sinon problèmes lors de l'appel
        Gdx.input.setInputProcessor(uiStage);
    }

    public void load() {
        //TODO(Load)
    }

    @Override
    public void update(float dt) {

    }

    public void dispose() {
        instrumental.dispose();
    }
}

