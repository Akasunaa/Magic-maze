package com.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Timer;
import com.multiplayer.ServerMaker;
import com.multiplayer.ServerNotReachedException;
import com.multiplayer.messages.Courrier;
import com.screens.BaseScreen;
import com.screens.MagicGame;
import com.screens.game.BaseActor;
import com.screens.game.board.GameScreen;
import com.screens.game.board.Player;
import com.utils.FunctionsKt;
import com.utils.Multiplayer;

import java.util.ArrayList;

import static com.screens.GameScreens.gameScreen;
import static com.utils.Multiplayer.*;

public class LobbyScreen extends BaseScreen {

    //Button leftButton;

    private Table optionOverlay;
    private ArrayList<PlayerMaker> playerMakerList;
    private Skin uiSkin;

    private TextButton startButton;
    private TextButton quitButton;
    private TextButton optionButton;


    public LobbyScreen(MagicGame g) {
        super(g);
    }

    public boolean shouldPassScreen = false;

    public void setToPassToGameScreen() {
        shouldPassScreen = true;
    }

    private void passToGameScreen() {
        // Là on va mettre en place le fading
        final BaseActor transparentForeground = new BaseActor(new Texture(Gdx.files.internal("Menu/Black.gif")));
        transparentForeground.setSize(1920, 1080);
        transparentForeground.setColor(0, 0, 0, 0);
        transparentForeground.setTouchable(Touchable.disabled);
        transparentForeground.toFront();
        uiStage.addActor(transparentForeground);
        // Je met ça là, car il y a un cyclicBarrier.await() dans gameScreen
        // Comme ça on est à peu près sur que tout le monde lance en même temps

        Action fadeToBlack = Actions.sequence(
                //Actions.alpha(1f), // set transparency value
                Actions.show(), // set visible to true
                Actions.forever(
                        Actions.sequence(
                                // Fondu de transition
                                Actions.color(new Color(0, 0, 0, 1), 2)
                        )
                )
        );
        transparentForeground.addAction(fadeToBlack);

        gameScreen = new GameScreen(game);
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                gameScreen.load();
                game.setScreen(gameScreen);
            }
        }, 2);

        shouldPassScreen = false;
    }
    public void create() {
        // pour le son
        final BaseActor background = new BaseActor(new Texture(Gdx.files.internal("Menu/BlurryMallBackground.jpg")));
        uiStage.addActor(background);



        background.toBack();

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        startButton = new TextButton("Demarrer la partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                serverMaker.quitLobby();
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
        startButton.setVisible(isServer);

        quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                FunctionsKt.quit();
            }
        });

        optionButton = new TextButton("Options", game.skin, "uiTextButtonStyle");
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
        audioSlider.setValue(game.audioVolume);
        audioSlider.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                game.audioVolume = audioSlider.getValue();
                instrumental.setVolume(game.audioVolume);
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

        game.skin.add("white", new Texture(Gdx.files.internal("UserInterface/backgroundBlack.png")));
        Drawable optionBackground = game.skin.newDrawable("white", new Color(0, 0, 0, 0.8f));

        optionOverlay.setBackground(optionBackground);

        uiSkin = new Skin(Gdx.files.internal("UserInterface/uiskin.json"));
        playerMakerList = new ArrayList<PlayerMaker>();
        playerMakerList.add(new PlayerMaker(Multiplayer.me, uiSkin, true));


        makeUiTable();





        /*for (int i = 0; i < Multiplayer.playerList.size(); i++) {
            avatars[i] = Multiplayer.playerList.get(i).avatar;
            avatars[i].setSize(90, 90);
            avatars[i].setPosition(viewWidth - avatars[i].getWidth() - 45, viewHeight - avatars[i].getHeight() - 225 - 135 * i);
            uiStage.addActor(avatars[i]);
            final int temp = i;
            avatars[i].addListener(new InputListener() {
                public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                    avatars[temp].addAction(Actions.sequence(
                            Actions.color(new Color(1,0,0,1),(float)0.20),
                            Actions.color(new Color(1,1,1,1),(float)0.20)));*/
    }

    public void multiplayerShenanigans() throws ServerNotReachedException {
        if (Multiplayer.isServer) {
            Multiplayer.serverMaker = new ServerMaker(Multiplayer.port, Multiplayer.clientList);
            Multiplayer.serverMaker.startThread();
            Multiplayer.serverMaker.enterLobby();
        }
        Multiplayer.courrier = new Courrier(Multiplayer.me.pseudo, Multiplayer.port, Multiplayer.serverIP);
        try {
            System.out.println("Client: Blocking in lobbyScreen");
            cyclicBarrier.await();
            System.out.println("Client: Unblocking in lobbyScreen");

        } catch (Exception e) {
        }
        System.out.println("Client: Launching Lobby");

        // On met ça ici parce que sinon problèmes lors de l'appel
        Gdx.input.setInputProcessor(uiStage);
        uiStage.addListener(new InputListener() {
            int count = 0;
            final String[] nameList =  new String[]{"Boris","Naruto","Babar"};
            // J'étais inspiré pour les noms
            public boolean keyDown(InputEvent event, int keyCode) {
                return true;
            }

            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Input.Keys.SPACE) {
                    // Exécute l'action quand on appuie sur la touche espace
                    if (count <= 2) {
                        final Player temp = new Player();
                        temp.pseudo = nameList[count];
                        temp.avatarName = animalNames[count];
                        temp.load();
                        addPlayer(temp);
                        count++;
                    }
                    else if (count == 3) {
                        removePlayer("Babar");
                        uiStage.removeListener(this);
                    }
                }
                return true;
            }
        });
    }

    public void load() {
        // On charge les ressources importantes, classique
        audioSlider.setValue(game.audioVolume);
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Lobby.mp3"));
        instrumental.setLooping(true);
        instrumental.setVolume(game.audioVolume);
        instrumental.play();
    }
    private boolean hasPlayerToAdd = false;
    private final ArrayList<Player> playersToAdd = new ArrayList<Player>();
    private boolean hasPlayerToRemove = false;
    private final ArrayList<String> playersToRemove = new ArrayList<String>();
    @Override
    public void update(float dt) {
        if (hasPlayerToAdd) {
            uiTable.clear();
            for (Player playerToAdd : playersToAdd) {
                playerToAdd.load();
                playerMakerList.add(new PlayerMaker(playerToAdd, uiSkin, false));
            }
            makeUiTable();
            hasPlayerToAdd = false;
            playersToAdd.clear();
        }
        if (hasPlayerToRemove) {
            uiTable.clear();
            for (String playerToRemove : playersToRemove) {
                for (PlayerMaker playerMaker : playerMakerList) {
                    if (playerMaker.getPseudo().equals(playerToRemove)) {
                        playerMakerList.remove(playerMaker);
                        break;
                    }
                }
                for (Player player : playerList) {
                    if (player.pseudo.equals(playerToRemove)) {
                        playerList.remove(player);
                        break;
                    }
                }
            }
            makeUiTable();
            hasPlayerToRemove = false;
            playersToRemove.clear();
        }
        if (hasChangedPseudo) {
            updateNames();
            hasChangedPseudo = false;
        }
        if (hasPlayerToUpdate) {
            for (PlayerMaker playerMaker : toUpdate) {
                playerMaker.updateAvatar();
            }
            hasPlayerToUpdate = false;
        }
        if (shouldPassScreen) passToGameScreen();
    }

    public boolean hasChangedPseudo = false;

    public void addPlayer(Player player) {
        hasPlayerToAdd = true;
        playersToAdd.add(player);
        playerList.add(player);
    }

    public void removePlayer(String player) {
        hasPlayerToRemove = true;
        playersToRemove.add(player);
        Player temp = null;
        for (Player playerMaker : playerList) {
            if (playerMaker.pseudo.equals(player)) {
                temp = playerMaker;
                break;
            }
        }
        playerList.remove(temp);
    }

    private void makeUiTable() {
        //uiTable.reset();
        int numberOfColumns = 24;
        uiTable.pad(20);
        uiTable.add(quitButton).right().expandX();
        uiTable.row();

        Table playerTable = new Table();
        for (PlayerMaker temp : playerMakerList) temp.addTextField(playerTable);
        playerTable.row();
        for (PlayerMaker temp : playerMakerList) {
            temp.load(uiSkin, playerTable);
        }
        // On fait ça pour se faciliter la tache, sinon ça devient trop mystique

        uiTable.add(playerTable).center().padTop(150).padBottom(100);
        uiTable.row();
        uiTable.add(optionButton).center().padTop(50).padBottom(50);
        uiTable.row();
        uiTable.add(startButton).center().padBottom(50);
        uiTable.row();
        //uiTable.debugCell();
        //playerTable.debugCell();
    }

    public void updateNames() {
        for (PlayerMaker temp : playerMakerList) {
            temp.updateName();
        }
    }

    private boolean hasPlayerToUpdate = false;
    private final ArrayList<PlayerMaker> toUpdate = new ArrayList<>();
    public void setToUpdateAvatar(String pseudo) {
        hasPlayerToUpdate = true;
        for (PlayerMaker playerMaker : playerMakerList) {
            if (playerMaker.getPseudo().equals(pseudo)) {
                toUpdate.add(playerMaker);
            }
        }
    }
}

