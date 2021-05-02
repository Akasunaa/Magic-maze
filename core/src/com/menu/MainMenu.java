package com.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Scaling;
import com.multiplayer.ServerNotReachedException;
import com.tiles.MainScreen;
import com.tiles.Player;
import com.utils.Multiplayer;

import static com.utils.Functions.modulo;
import static com.utils.GameScreens.lobbyScreen;
import static com.utils.GameScreens.mainScreen;

public class MainMenu extends BaseScreen {

    private Table optionOverlay;

    public int currentAvatarNumber;
    public TextField usernameTextField;
    public TextField ipAddress;

    public BaseActor[] avatarImages;
    public BaseActor currentAvatar;


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

        Texture group = new Texture(Gdx.files.internal("MenuAssets/MagicGroup.png"));
        group.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image groupImage1 = new Image(group);
        groupImage1.setScaling(Scaling.fit);
        Image groupImage2 = new Image(group);
        groupImage2.setScaling(Scaling.fit);

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        TextButton startButton = new TextButton("Créer une partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer,
                                     int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
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


        TextButton optionButton = new TextButton("Options et aide", game.skin, "uiTextButtonStyle");
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
        Label helpLabel = new Label("Voici comment rejoindre une partie et créer une partie : ", game.skin, "uiLabelStyle");

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
        optionOverlay.add(helpLabel).padBottom(50);
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

        background.toBack();

        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
        usernameTextField = new TextField("Pseudo...", uiSkin);
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
        padTable.add(usernameTextField).fill(1,0.1f).expandX().padBottom(25);
        padTable.row();
        padTable.add(ipAddress).fill(1,0.1f).expandX().padTop(25);

        uiTable.pad(20);
        uiTable.add(quitButton).right().colspan(4).expandX();
        uiTable.row();
        uiTable.add(titleImage).center().height(250).colspan(4);
        uiTable.row();
        uiTable.add(groupImage1).left();
        uiTable.add(padTable).colspan(2).fill();
        uiTable.add(groupImage2).right();
        uiTable.row();
        uiTable.add(optionButton).center().padTop(70).colspan(4);
        uiTable.row();
        uiTable.add(startButton).padTop(20).colspan(2).right().padRight(40);
        uiTable.add(joinButton).padTop(20).colspan(2).left().padLeft(0);
        uiTable.row();
        uiTable.add(genintImage).left().colspan(4).padTop(50);

        uiTable.debugCell();


    }

    public void update(float dt) {

    }
}