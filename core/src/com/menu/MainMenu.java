package com.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.multiplayer.ServerNotReachedException;
import com.tiles.MainScreen;
import com.tiles.Player;
import com.utils.Multiplayer;

import static com.utils.Functions.modulo;
import static com.utils.GameScreens.lobbyScreen;
import static com.utils.GameScreens.mainScreen;

public class MainMenu extends BaseScreen {

    private Table optionOverlay;
    private Table ruleTable;

    public int currentAvatarNumber;
    public TextField usernameTextField;
    public TextField ipAddress;

    public BaseActor[] avatarImages;
    public BaseActor currentAvatar;

    private Label warningLabel;
    private boolean pseudoValid;

    public MainMenu(MagicGame g) {
        super(g);
    }

    public void create() {
        // On met le bon InputProcessor
        InputMultiplexer inputMultiplexer = new InputMultiplexer(uiStage, mainStage);
        Gdx.input.setInputProcessor(inputMultiplexer);

        // Création de la liste des avatars pour gérer les différents avatars
        Texture tempAvatar;
        final String[] animalNames = new String[]{"elephant", "giraffe", "hippo", "monkey", "panda", "parrot", "penguin", "pig", "rabbit", "snake"};
        avatarImages = new BaseActor[animalNames.length];
        for (int i = 0; i < animalNames.length; i++) {
            tempAvatar = new Texture(Gdx.files.internal("Avatars/" + animalNames[i] + ".png"));
            tempAvatar.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            avatarImages[i] = new BaseActor(tempAvatar);
        }
        currentAvatar = avatarImages[0];
        Multiplayer.me.avatar = currentAvatar;
        currentAvatarNumber = 0;
        Multiplayer.me.avatarName = animalNames[currentAvatarNumber];

        // passer audio volume en variable globale de MagicGame.java
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/MusicMenu.wav"));
        audioVolume = 0.70f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();

        BaseActor background = new BaseActor();
        background.setTexture(new Texture(Gdx.files.internal("MenuAssets/BlurryMallBackground.jpg")));
        uiStage.addActor(background);

        Texture titleText = new Texture(Gdx.files.internal("MenuAssets/MagicLogo.png"));
        titleText.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image titleImage = new Image(titleText);
        titleImage.setScaling(Scaling.fit);

        Texture genint = new Texture(Gdx.files.internal("MenuAssets/genintLogo.png"));
        genint.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image genintImage = new Image(genint);
        genintImage.setScaling(Scaling.fit);

        Texture règles1 = new Texture(Gdx.files.internal("MenuAssets/regle_page-0001.jpg"));
        règles1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image règlesImage1 = new Image(règles1);
        règlesImage1.setScaling(Scaling.fit);

        Texture règles2 = new Texture(Gdx.files.internal("MenuAssets/regle_page-0002.jpg"));
        règles2.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image règlesImage2 = new Image(règles2);
        règlesImage2.setScaling(Scaling.fit);

        Texture règles3 = new Texture(Gdx.files.internal("MenuAssets/regle_page-0003.jpg"));
        règles3.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image règlesImage3 = new Image(règles3);
        règlesImage3.setScaling(Scaling.fit);

        Texture règles4 = new Texture(Gdx.files.internal("MenuAssets/regle_page-0004.jpg"));
        règles1.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image règlesImage4 = new Image(règles4);
        règlesImage4.setScaling(Scaling.fit);

        Texture règles5 = new Texture(Gdx.files.internal("MenuAssets/regle_page-0005.jpg"));
        règles5.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image règlesImage5 = new Image(règles5);
        règlesImage5.setScaling(Scaling.fit);

        Texture group = new Texture(Gdx.files.internal("MenuAssets/MagicGroup.png"));
        group.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image groupImage1 = new Image(group);
        groupImage1.setScaling(Scaling.fit);
        Image groupImage2 = new Image(group);
        groupImage2.setScaling(Scaling.fit);

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        Label ipLabel = new Label("Rentrez ici l'adresse ip du host de la partie :", game.skin, "uiLabelStyle");

        TextButton startButton = new TextButton("Créer une partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pseudoValid) {
                    Multiplayer.startServer();
                    lobbyScreen = new LobbyScreen(game, audioVolume);
                    try {
                        lobbyScreen.multiplayerShenanigans();
                        lobbyScreen.load();
                        game.setScreen(lobbyScreen);
                    } catch (ServerNotReachedException e) {
                        e.printError();
                        lobbyScreen.dispose();
                    }
                }
                else {
                    float delay = 0.02f; // seconds
                    warningLabel.setPosition(730,665);
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            warningLabel.setPosition(725,665);
                        }}, delay);
                }
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

        TextButton joinButton = new TextButton("Rejoindre une partie", game.skin, "uiTextButtonStyle");
        joinButton.getLabel().setTouchable(Touchable.disabled);
        joinButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pseudoValid) {
                    Multiplayer.stopServer();
                    lobbyScreen = new LobbyScreen(game, audioVolume);
                    try {
                        lobbyScreen.multiplayerShenanigans();
                        lobbyScreen.load();
                        game.setScreen(lobbyScreen);
                    } catch (ServerNotReachedException e) {
                        e.printError();
                        lobbyScreen.dispose();
                    }
                }
                else {
                    float delay = 0.02f; // seconds
                    warningLabel.setPosition(730,665);
                    System.out.println(",ik");
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            warningLabel.setPosition(725,665);
                        }}, delay);
                }
            }
        });

        joinButton.addListener(new InputListener() {
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

        TextButton quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        TextButton returnButton1 = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton1.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(false);
            }
        });

        TextButton returnButton2 = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton2.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                ruleTable.setVisible(false);
            }
        });

        TextButton ruleButton = new TextButton("Règles du jeu", game.skin, "uiTextButtonStyle");
        ruleButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                ruleTable.setVisible(true);
            }
        });

        final Table scrollTable = new Table();
        scrollTable.add(règlesImage1);
        scrollTable.row();
        scrollTable.add(règlesImage2);
        scrollTable.row();
        scrollTable.add(règlesImage3);
        scrollTable.row();
        scrollTable.add(règlesImage4);
        scrollTable.row();
        scrollTable.add(règlesImage5);

        final ScrollPane scroller = new ScrollPane(scrollTable);

        ruleTable = new Table();
        ruleTable.setVisible(false);
        ruleTable.setFillParent(true);
        ruleTable.add(returnButton2).padTop(25);
        ruleTable.row();
        ruleTable.add(scroller).padTop(25); //.expand(); //.fill()

        TextButton optionButton = new TextButton("Options et commandes", game.skin, "uiTextButtonStyle");
        optionButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(true);
            }
        });

        Label optionLabel = new Label("Options et aide :", game.skin, "uiLabelStyle");
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");
        Label helpLabel = new Label("Commandes", game.skin, "uiLabelStyle");
        Label helpLabel1 = new Label("zoomer : ", game.skin, "uiLabelStyle");
        Label helpLabel2 = new Label("dezoomer :", game.skin, "uiLabelStyle");
        Label helpLabel3 = new Label("poser une tuile/déplacer un pion :", game.skin, "uiLabelStyle");


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
        optionOverlay.add(optionLabel).pad(100).colspan(2);
        optionOverlay.row();
        optionOverlay.add(volumeLabel).padBottom(20).colspan(2);
        optionOverlay.row();
        optionOverlay.add(audioSlider).width(400).padBottom(50).colspan(2);
        optionOverlay.row();
        optionOverlay.add(helpLabel).padBottom(20).colspan(2);
        optionOverlay.row();
        optionOverlay.add(helpLabel1).padBottom(20);
        optionOverlay.add(helpLabel2).padBottom(20);
        optionOverlay.row();
        optionOverlay.add(helpLabel3).padBottom(50).colspan(2);
        optionOverlay.row();
        optionOverlay.add(returnButton1).colspan(2);

        Stack stacker = new Stack();
        stacker.setFillParent(true);
        uiStage.addActor(stacker);
        stacker.add(uiTable);
        stacker.add(optionOverlay);
        stacker.add(ruleTable);

        game.skin.add("white", new Texture(Gdx.files.internal("GameUIAssets/white4px.png")));
        Drawable blackBackground = game.skin.newDrawable("white", new Color(0, 0, 0, 0.8f));

        optionOverlay.setBackground(blackBackground);
        ruleTable.setBackground(blackBackground);

        background.toBack();

        warningLabel = new Label("Pseudo invalide : ' ' interdit", game.skin, "uiLabelStyle");
        uiStage.addActor(warningLabel);
        warningLabel.setVisible(false);
        warningLabel.setPosition(725,665);
//        warningLabel.setColor(1,0,0,1);
//        System.out.println(warningLabel.getColor());


        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));

        pseudoValid = true;
        usernameTextField = new TextField("Pseudo", uiSkin);
        usernameTextField.setMaxLength(13);

        usernameTextField.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Multiplayer.me.pseudo = usernameTextField.getText();
            }
        });
        Multiplayer.me.pseudo = usernameTextField.getText();

        ipAddress = new TextField(Multiplayer.ip, uiSkin);
        ipAddress.setMaxLength(15);
        ipAddress.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Multiplayer.serverIP = ipAddress.getText();
            }
        });

        Table padTable = new Table();
        padTable.add(usernameTextField).expandX().padBottom(25).minWidth(415); //.fill(1,0.1f)
        padTable.row();
        padTable.add(ipLabel);
        padTable.row();
        padTable.add(ipAddress).expandX().padTop(25).minWidth(200); //.fill(1,0.1f)

        uiTable.pad(20);
        uiTable.add(quitButton).right().colspan(4).expandX().padRight(200);
        uiTable.row();
        uiTable.add(titleImage).center().height(250).colspan(4);
        uiTable.row();
        uiTable.add(groupImage1).right().padRight(25); //.left() .maxWidth(400)
        uiTable.add(padTable).colspan(2); //.fill()
        uiTable.add(groupImage2).left().padLeft(25); //.right() .maxWidth(400)
        uiTable.row();
        uiTable.add(optionButton).padTop(20).colspan(2).right().padRight(40);
        uiTable.add(ruleButton).padTop(20).colspan(2).left().padLeft(0).minSize(419,5);
        uiTable.row();
        uiTable.add(startButton).padTop(20).colspan(2).right().padRight(40).minSize(475,5);
        uiTable.add(joinButton).padTop(20).colspan(2).left().padLeft(0);
        uiTable.row();
        uiTable.add(genintImage).left().colspan(4).padTop(50).padLeft(200);

        uiTable.debugCell();


    }

    public void update(float dt) {
        if (usernameTextField.getText().contains(" ")){
            pseudoValid = false;
            warningLabel.setVisible(true);

            float delay = 0.5f; // seconds

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    warningLabel.setVisible(false);
                }
            }, delay);
        }
        else pseudoValid = true;
    }
}