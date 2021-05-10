package com.menu;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.multiplayer.ServerNotReachedException;

public abstract class BaseScreen implements Screen {
    protected MagicGame game;
    protected Stage mainStage;
    public Stage getMainStage() {return mainStage;}
    protected Stage uiStage;
    public Stage getUiStage() {return uiStage;}

    public final int viewWidth = 1920;
    public final int viewHeight = 1080;

    private boolean paused;

    protected Table uiTable;

    public float audioVolume;
    public Music instrumental;

    public Sound buttonHover;
    public Slider audioSlider;

    public boolean hasBackground = true;
    public final static String[] animalNames = new String[] {"elephant","giraffe","hippo","monkey","panda","parrot","penguin","pig","rabbit","snake"};

    public BaseScreen(MagicGame g){
        game = g;
        mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        uiStage = new Stage(new FitViewport(viewWidth, viewHeight));

        paused = false;

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);

        // Création de la liste des avatars pour gérer les différents avatars
        Texture tempAvatar;
        for (int i = 0; i < animalNames.length; i++) {
            tempAvatar = new Texture(Gdx.files.internal("Avatars/" + animalNames[i] + ".png"));
            tempAvatar.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//            avatarImages[i] = new BaseActor();
//            avatarImages[i].setTexture(tempAvatar);
//            avatarImages[i].scaleBy(0.01f, 0.01f);
        }


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
    public void render(float dt) {
        update(dt);
        uiStage.act(dt);
        // only pause gameplay events, not UI events
        if ( !isPaused() ) {
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

    public void dispose() {
        try {
            instrumental.dispose();
        } catch(Exception e) {
            //e.printStackTrace();
        }
    }
    public void show() { }
    public void hide() { }
}



