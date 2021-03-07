package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

public class MainMenu extends BaseScreen
{
    Button leftButton;

    private Table optionOverlay;

    public int currentAvatarNumber;
    public TextField usernameTextField;
    public String playerName;

    public MainMenu(BaseGame g)

    {
        super(g);
    }

    public void create()
    {
        currentAvatarNumber = 0;

        // passer audio volume en variable globale de MagicGame.java
        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/MusicMenu.wav"));
        audioVolume = 0.80f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();

        BaseActor background = new BaseActor();
        background.setTexture( new Texture(Gdx.files.internal("MenuAssets/BlurryMallBackground.jpg")) );
        uiStage.addActor( background );

        Texture titleText = new Texture(Gdx.files.internal("MenuAssets/MagicLogo.png"));
        titleText.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image titleImage = new Image(titleText);

        Texture genint = new Texture(Gdx.files.internal("MenuAssets/genintLogo.png"));
        genint.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image genintImage = new Image(genint);

        Texture group = new Texture(Gdx.files.internal("MenuAssets/MagicGroup.png"));
        group.setFilter(TextureFilter.Linear, TextureFilter.Linear);
        Image groupImage1 = new Image(group);
        Image groupImage2 = new Image(group);

        final Sound buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        TextButton startButton = new TextButton("Demarrer une partie", game.skin, "uiTextButtonStyle");
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
                playerName = usernameTextField.getText();
                game.setScreen( new MagicLevel(game, currentAvatarNumber, playerName, audioVolume) );
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

        TextButton joinButton = new TextButton("Rejoindre une partie", game.skin, "uiTextButtonStyle");
        joinButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        dispose();
                        playerName = usernameTextField.getText();
                        game.setScreen( new MagicLevel(game, currentAvatarNumber, playerName, audioVolume) );
                    }
                });

        joinButton.addListener(new InputListener()
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
        joinButton.getLabel().setTouchable(Touchable.disabled);

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
        leftButton = new Button( leftArrowStyle );
        leftButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {
                        return true;
                    }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        Cell<Image> cell = uiTable.getCell(currentAvatar);
                        cell.clearActor();

                        if (currentAvatarNumber != 0)
                        {
                            currentAvatarNumber -=1;
                        }
                        else {
                            currentAvatarNumber = 9;
                        }
                        currentAvatar = avatarImages[currentAvatarNumber];
                        cell.setActor(currentAvatar);

                    }
                });

        Texture rightArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_right.png"));
        game.skin.add("rightArrow", rightArrowTexture );
        Button.ButtonStyle rightArrowStyle = new Button.ButtonStyle();
        rightArrowStyle.up = game.skin.getDrawable("rightArrow");
        Button rightButton = new Button( rightArrowStyle );
        rightButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {
                        return true;
                    }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        Cell<Image> cell = uiTable.getCell(currentAvatar);
                        cell.clearActor();

                        if (currentAvatarNumber != 9)
                        {
                            currentAvatarNumber +=1;
                        }
                        else {
                            currentAvatarNumber = 0;
                        }
                        currentAvatar = avatarImages[currentAvatarNumber];
                        cell.setActor(currentAvatar);

                    }
                });

        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
        usernameTextField = new TextField("Pseudo...", uiSkin);
        Gdx.input.setInputProcessor(uiStage);

        uiTable.pad(20);
        uiTable.add(quitButton).colspan(5).right().expandX();
        uiTable.row();
        uiTable.add(titleImage).colspan(5).center();
        uiTable.row();
        uiTable.add(usernameTextField).center().colspan(5).padTop(20);
        uiTable.row();
        uiTable.add(groupImage1).left().expandX();
        uiTable.add(leftButton).left();
        uiTable.add(currentAvatar).center().pad(20, 20, 20, 20);
        uiTable.add(rightButton).right();
        uiTable.add(groupImage2).right().expandX();
        uiTable.row();
        uiTable.add(avatarLabel).colspan(5).center().padTop(20);
        uiTable.row();
        uiTable.add(optionButton).center().colspan(5).padTop(20);
        uiTable.row();
        uiTable.add(startButton).right().colspan(2).padTop(20);
        uiTable.add().center().padTop(20);
        uiTable.add(joinButton).left().colspan(2).padTop(20);
        uiTable.row();
        uiTable.add(genintImage).colspan(5).left().expandX();


    }

    public void update(float dt)
    {

    }

    public void dispose()
    {
        instrumental.dispose();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

}
