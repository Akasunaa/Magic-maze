package com.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.multiplayer.ServerNotReachedException;
import com.screens.BaseScreen;
import com.screens.GameScreens;
import com.screens.MagicGame;
import com.screens.game.BaseActor;
import com.utils.Multiplayer;

import static com.screens.GameScreens.lobbyScreen;

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

    private TextButton returnButton2;
    private TextButton returnButton1;
    private TextButton optionButton;
    private TextButton ruleButton;

    public MainMenu(MagicGame g) {
        super(g);
        GameScreens.mainMenu = this;
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
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Menu.mp3"));
        instrumental.setLooping(true);
        instrumental.setVolume(game.audioVolume);
        instrumental.play();

        BaseActor background = new BaseActor(new Texture(Gdx.files.internal("MenuAssets/BlurryMallBackground.jpg")));
        uiStage.addActor(background);

        Texture titleText = new Texture(Gdx.files.internal("MenuAssets/MagicLogo.png"));
        titleText.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image titleImage = new Image(titleText);
        titleImage.setScaling(Scaling.fit);

        Texture genint = new Texture(Gdx.files.internal("MenuAssets/genintLogo.png"));
        genint.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image genintImage = new Image(genint);
        genintImage.setScaling(Scaling.fit);

        Texture group = new Texture(Gdx.files.internal("MenuAssets/MagicGroup.png"));
        group.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image groupImage1 = new Image(group);
        groupImage1.setScaling(Scaling.fit);
        Image groupImage2 = new Image(group);
        groupImage2.setScaling(Scaling.fit);

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        Label ipLabel = new Label("Rentrez ici l'adresse IP de l'host de la partie :", game.skin, "uiLabelStyle");

        final TextButton startButton = new TextButton("Créer une partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pseudoValid) {
                    Multiplayer.startServer();
                    lobbyScreen = new LobbyScreen(game);
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

        final TextButton joinButton = new TextButton("Rejoindre une partie", game.skin, "uiTextButtonStyle");
        joinButton.getLabel().setTouchable(Touchable.disabled);
        joinButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (pseudoValid) {
                    Multiplayer.stopServer();
                    lobbyScreen = new LobbyScreen(game);
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

        final TextButton quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        ruleButton = new TextButton("Règles du jeu", game.skin, "uiTextButtonStyle");
        ruleButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) { return true; }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                ruleTable.setVisible(true);
                quitButton.setTouchable(Touchable.disabled);

            }
        });

        returnButton1 = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton1.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(false);
                startButton.setTouchable(Touchable.enabled);
                joinButton.setTouchable(Touchable.enabled);
                ruleButton.setTouchable(Touchable.enabled);
                quitButton.setTouchable(Touchable.enabled);
                optionButton.setTouchable(Touchable.enabled);
            }
        });

        returnButton2 = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton2.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                ruleTable.setVisible(false);
                quitButton.setTouchable(Touchable.enabled);

            }
        });

        optionButton = new TextButton("Options et commandes", game.skin, "uiTextButtonStyle");
        optionButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                togglePaused();
                optionOverlay.setVisible(true);
                startButton.setTouchable(Touchable.disabled);
                joinButton.setTouchable(Touchable.disabled);
                ruleButton.setTouchable(Touchable.disabled);
                quitButton.setTouchable(Touchable.disabled);
                optionButton.setTouchable(Touchable.disabled);
            }
        });

        final Table scrollTable = new Table();
        Image[] ruleBook = new Image[5];
        for (int i = 0; i < 5; i ++) {
            Texture temp = new Texture(Gdx.files.internal("MenuAssets/regle_page-000"+(i+1)+".jpg"));
            temp.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            ruleBook[i] = new Image(temp);
            ruleBook[i].setScaling(Scaling.fit);
            scrollTable.add(ruleBook[i]);
            scrollTable.row();
        }

        final ScrollPane scroller = new ScrollPane(scrollTable);

        ruleTable = new Table();
        ruleTable.setVisible(false);
        ruleTable.setFillParent(true);
        ruleTable.add(returnButton2).padTop(25);
        ruleTable.row();
        ruleTable.add(scroller).padTop(25); //.expand(); //.fill()

        Label optionLabel = new Label("Options et aide :", game.skin, "uiLabelStyle");
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");
        Label helpLabel = new Label("Commandes", game.skin, "uiLabelStyle");
        Label helpLabel1 = new Label("zoomer : ", game.skin, "uiLabelStyle");
        Label helpLabel2 = new Label("dezoomer :", game.skin, "uiLabelStyle");
        Label helpLabel3 = new Label("poser une tuile/déplacer un pion :", game.skin, "uiLabelStyle");

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



        pseudoValid = true;
        usernameTextField = new TextField("Pseudo", game.skin);
        usernameTextField.setMaxLength(15);
        usernameTextField.setAlignment(Align.center);

        usernameTextField.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Multiplayer.me.pseudo = usernameTextField.getText();
            }
        });
        Multiplayer.me.pseudo = usernameTextField.getText();

        ipAddress = new TextField(Multiplayer.ip, game.skin);
        ipAddress.setMaxLength(15);
        ipAddress.setAlignment(Align.center);
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
        padTable.add(ipAddress).padTop(25).minWidth(400); //.fill(1,0.1f)
        //padTable.debugCell();

        Table buttonTable = new Table();
        buttonTable.add(optionButton).padTop(20).right().padRight(40).fillX();
        buttonTable.add(ruleButton).padTop(20).left().padLeft(0).fillX();
        buttonTable.row();
        buttonTable.add(startButton).padTop(20).right().padRight(40).fillX();
        buttonTable.add(joinButton).padTop(20).left().padLeft(0).fillX();
        //buttonTable.debugCell();

        uiTable.pad(20);
        uiTable.add(quitButton).right().colspan(3).expandX().padTop(13);
        // Le pad c'est pour l'aligner avec celui de l'écran suivant
        uiTable.row();
        uiTable.add(titleImage).center().height(250).colspan(3);
        uiTable.row();
        uiTable.add(groupImage1).right().padRight(25); //.left() .maxWidth(400)
        uiTable.add(padTable); //.fill()
        uiTable.add(groupImage2).left().padLeft(25); //.right() .maxWidth(400)
        uiTable.row();
        uiTable.add(buttonTable).center().colspan(3);
        uiTable.row();
        uiTable.add(genintImage).left().colspan(3).padTop(50);

        //uiTable.debugCell();


    }

    public void update(float dt) {
        pseudoValid = true;
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
        //pseudoValid = true;
    }
}