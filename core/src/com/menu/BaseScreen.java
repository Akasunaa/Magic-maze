package com.menu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.scenes.scene2d.ui.Table;

import static com.utils.Functions.release;
import static com.utils.MainConstants.camera;


public abstract class BaseScreen implements Screen, InputProcessor {
    protected MagicGame game;
    protected Stage mainStage;
    public Stage getMainStage() {return mainStage;}
    protected Stage uiStage;
    public Stage getUiStage() {return uiStage;}

    public final int viewWidth = 1920;
    public final int viewHeight = 1080;

    private boolean paused;

    protected Table uiTable;

    //public String pseudo;

    public BaseActor[] avatarImages;
    public BaseActor[] player0Avatars;
    public BaseActor[] player1Avatars;
    public BaseActor[] player2Avatars;
    public BaseActor[] player3Avatars;
    public BaseActor[][] playerList;

    public float audioVolume;
    public Music instrumental;

    public Sound buttonHover;
    public Slider audioSlider;

    public boolean hasBackground = true;

    public BaseScreen(MagicGame g) {
        game = g;
        mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        uiStage = new Stage(new FitViewport(viewWidth, viewHeight));

        paused = false;

        InputMultiplexer im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor(im);

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);

        // Création de la liste des avatars pour gérer les différents avatars
        Texture tempAvatar;
        final String[] animalNames = new String[] {"elephant","giraffe","hippo","monkey","panda","parrot","penguin","pig","rabbit","snake"};
//        avatarImages = new BaseActor[animalNames.length];
//        for (int i = 0; i < animalNames.length; i++) {
//            tempAvatar = new Texture(Gdx.files.internal("Avatars/" + animalNames[i] + ".png"));
//            tempAvatar.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//            avatarImages[i].setTexture(tempAvatar);
//            player0Avatars[i] = new BaseActor();
//            player0Avatars[i].setTexture(tempAvatar);
//            player1Avatars[i] = new BaseActor();
//            player1Avatars[i].setTexture(tempAvatar);
//            player2Avatars[i] = new BaseActor();
//            player2Avatars[i].setTexture(tempAvatar);
//            player3Avatars[i] = new BaseActor();
//            player3Avatars[i].setTexture(tempAvatar);
//            }
//
//        playerList = new BaseActor[][]{player0Avatars, player1Avatars, player2Avatars, player3Avatars};

        buttonHover = Gdx.audio.newSound(Gdx.files.internal("Music&Sound/buttonHover.mp3"));

        audioSlider = new Slider(0, 1, 0.005f, false, game.skin, "uiSliderStyle" );
        audioSlider.addListener(
                new ChangeListener()
                {
                    public void changed(ChangeEvent event, Actor actor)
                    {
                        audioVolume = audioSlider.getValue();
                        instrumental.setVolume(audioVolume);
                    }
                });

        //Toujours à la fin du constructeur sinon plein d'erreurs de merde
        create();
    }

    public abstract void create();
    public abstract void update(float dt);

    // gameloop code; update, then render.
    public void render(float dt)
    {
        update(dt);
        uiStage.act(dt);
        // only pause gameplay events, not UI events
        if ( !isPaused() )
        {
            mainStage.act(dt);
            update(dt);
        }
        // render
        if (hasBackground) {
            Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        mainStage.draw();
        uiStage.draw();
    }

    // pause methods
    public boolean isPaused()
    { return paused; }
    public void setPaused(boolean b)
    { paused = b; }
    public void togglePaused()
    { paused = !paused; }

    public void resize(int width, int height)
    {
        mainStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
    }

    // methods required by Screen interface
    public void pause() { }
    public void resume() { }
    public void dispose() { }
    public void show() { }
    public void hide() { }
    // methods required by InputProcessor interface
    public boolean keyDown(int keycode)
    { return false; }
    public boolean keyUp(int keycode)
    { return false; }
    public boolean keyTyped(char c)
    { return false; }
    public boolean mouseMoved(int screenX, int screenY)
    { return false; }
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    { return false; }
    public boolean touchDragged(int screenX, int screenY, int pointer)
    { return false; }
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    { if (button == Input.Buttons.MIDDLE) release();
    return false; }

}



