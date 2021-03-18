package com.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tiles.MainScreen;

import static com.utils.Functions.modulo;
import static com.utils.GameScreens.mainScreen;

public class LobbyScreen extends BaseScreen{

    //Button leftButton;

    private Table optionOverlay;

    public int[] AvatarNumbers;
    public TextField[] usernameTextFields;
    public TextField usernameTextField0;
    public TextField usernameTextField1;
    public TextField usernameTextField2;
    public TextField usernameTextField3;
    public String playerNames[];

    public LobbyScreen(MagicGame g, float audioVolume)
    {
        super(g);

        //instrumental.setVolume(audioVolume);
        //audioSlider.setValue( audioVolume );
    }

    public void create()
    {
        AvatarNumbers = new int[]{0, 1, 2, 3};

        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
//        for (int i =0; i<3; i++) {
//            usernameTextFields[i] = new TextField("Pseudo...", uiSkin);
//        }
        usernameTextField0 = new TextField("Pseudo...", uiSkin);
        usernameTextField1 = new TextField("Pseudo...", uiSkin);
        usernameTextField2 = new TextField("Pseudo...", uiSkin);
        usernameTextField3 = new TextField("Pseudo...", uiSkin);

        Gdx.input.setInputProcessor(uiStage);


        // passer audio volume en variable globale de MagicGame.java
//        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/MusicMenu.wav"));
//        audioVolume = 0.70f;
//        instrumental.setLooping(true);
//        instrumental.setVolume(audioVolume);
//        instrumental.play();

        BaseActor background = new BaseActor();
        background.setTexture( new Texture(Gdx.files.internal("MenuAssets/BlurryMallBackground.jpg")) );
        uiStage.addActor( background );

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        TextButton startButton = new TextButton("Demarrer la partie", game.skin, "uiTextButtonStyle");
        startButton.addListener(new InputListener()
        {
            public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                      int button)
            {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
                dispose();


//                    playerNames[0] = usernameTextField0.getText();
//                    playerNames[1] = usernameTextField1.getText();
//                    playerNames[2] = usernameTextField2.getText();
//                    playerNames[3] = usernameTextField3.getText();

                mainScreen = new MainScreen(game, AvatarNumbers, playerNames, audioVolume);



                mainScreen.load();
                game.setScreen( mainScreen );
            }
        });

        startButton.addListener(new InputListener()
        {
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
        quitButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        Gdx.app.exit();
                    }
                });

        Label avatarLabel = new Label("Choix de l'avatar :", game.skin, "uiLabelStyle");

        TextButton optionButton = new TextButton("Options", game.skin, "uiTextButtonStyle");
        optionButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        togglePaused();
                        optionOverlay.setVisible( true );
                    }
                });

        Label optionLabel = new Label("Options :", game.skin, "uiLabelStyle");
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");

        TextButton returnButton = new TextButton("Return", game.skin, "uiTextButtonStyle");
        returnButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        togglePaused();
                        optionOverlay.setVisible( false );
                    }
                });

        final Slider audioSlider = new Slider(0, 1, 0.005f, false, game.skin, "uiSliderStyle" );
        audioSlider.setValue( audioVolume );
        audioSlider.addListener(
                new ChangeListener()
                {
                    public void changed(ChangeEvent event, Actor actor)
                    {
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

        game.skin.add("white", new Texture( Gdx.files.internal("GameUIAssets/white4px.png")) );
        Drawable optionBackground = game.skin.newDrawable("white", new Color(0,0,0,0.8f) );

        optionOverlay.setBackground(optionBackground);

        background.toBack();

        Texture leftArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_left.png"));
        game.skin.add("leftArrow", leftArrowTexture );
        Button.ButtonStyle leftArrowStyle = new Button.ButtonStyle();
        leftArrowStyle.up = game.skin.getDrawable("leftArrow");
        Button[] leftButtonList = new Button[4];

        Texture rightArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_right.png"));
        game.skin.add("rightArrow", rightArrowTexture );
        Button.ButtonStyle rightArrowStyle = new Button.ButtonStyle();
        rightArrowStyle.up = game.skin.getDrawable("rightArrow");
        Button[] rightButtonList= new Button[4];

        for ( int i=0; i<4; i++){

            leftButtonList[i] = new Button( leftArrowStyle );
            final int finalI = i;

            leftButtonList[i].addListener(
                    new InputListener()
                    {
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                        {
                            return true;
                        }
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                        {
//                            Cell<BaseActor> cell = uiTable.getCell(playerList[finalI][AvatarNumbers[finalI]]);
//                            cell.clearActor();
//
//                            AvatarNumbers[finalI] = modulo(AvatarNumbers[finalI] - 1, 10);
//                            cell.setActor(playerList[finalI][AvatarNumbers[finalI]]);

                        }
                    });


            rightButtonList[i] = new Button( rightArrowStyle );
            rightButtonList[i].addListener(
                    new InputListener()
                    {
                        public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                        {
                            return true;
                        }
                        public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                        {
//                            Cell<BaseActor> cell = uiTable.getCell(playerList[finalI][AvatarNumbers[finalI]]);
//                            cell.clearActor();
//
//                            AvatarNumbers[finalI] = modulo(AvatarNumbers[finalI] + 1, 10);
//                            cell.setActor(playerList[finalI][AvatarNumbers[finalI]]);

                        }
                    });
        }

        uiTable.pad(20);
        uiTable.add(quitButton).colspan(3).right().expandX();;
        uiTable.row();
        uiTable.add(usernameTextField0).center().padTop(20);
        uiTable.add(usernameTextField1).center().padTop(20);
        uiTable.add(usernameTextField2).center().padTop(20);
        uiTable.add(usernameTextField3).center().padTop(20);
        for (int i=0; i<4; i++){
            uiTable.row();
            uiTable.add(leftButtonList[i]).left();
            //uiTable.add(avatarImages[i]).center().pad(20, 20, 20, 20);
            uiTable.add(rightButtonList[i]).right();
        }
//        for (int i=0; i<4; i++) {
//            uiTable.row();
//            uiTable.add(avatarLabel).colspan(3).center().padTop(20);
//        }
        uiTable.row();
        uiTable.add(optionButton).center().colspan(3).padTop(20);
        uiTable.row();
        uiTable.add(startButton).center().colspan(3).padTop(20);
        uiTable.add().center().padTop(20);
        uiTable.row();

    }

    @Override
    public void update(float dt)
    {

    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
