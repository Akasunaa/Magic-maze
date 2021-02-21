package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.InputMultiplexer;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public abstract class BaseScreen implements Screen, InputProcessor {
    protected BaseGame game;
    protected Stage mainStage;
    protected Stage uiStage;

    public final int viewWidth = 1920;
    public final int viewHeight = 1080;

    private boolean paused;

    protected Table uiTable;

    public String pseudo;

    public Image[] avatarImages;
    public Image currentAvatar;

    public BaseScreen(BaseGame g) {
        game = g;
        mainStage = new Stage(new FitViewport(viewWidth, viewHeight));
        uiStage = new Stage(new FitViewport(viewWidth, viewHeight));

        paused = false;

        InputMultiplexer im = new InputMultiplexer(this, uiStage, mainStage);
        Gdx.input.setInputProcessor(im);

        uiTable = new Table();
        uiTable.setFillParent(true);
        uiStage.addActor(uiTable);

        Texture avatar1 = new Texture(Gdx.files.internal("Avatars/elephant.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage1 = new Image(avatar1);
        Texture avatar2 = new Texture(Gdx.files.internal("Avatars/giraffe.png"));
        avatar2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        final Image avatarImage2 = new Image(avatar2);
        Texture avatar3 = new Texture(Gdx.files.internal("Avatars/hippo.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage3 = new Image(avatar3);
        Texture avatar4 = new Texture(Gdx.files.internal("Avatars/monkey.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage4 = new Image(avatar4);
        Texture avatar5 = new Texture(Gdx.files.internal("Avatars/panda.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage5 = new Image(avatar5);
        Texture avatar6 = new Texture(Gdx.files.internal("Avatars/parrot.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage6 = new Image(avatar6);
        Texture avatar7 = new Texture(Gdx.files.internal("Avatars/penguin.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage7 = new Image(avatar7);
        Texture avatar8 = new Texture(Gdx.files.internal("Avatars/pig.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage8 = new Image(avatar8);
        Texture avatar9 = new Texture(Gdx.files.internal("Avatars/rabbit.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage9 = new Image(avatar9);
        Texture avatar10 = new Texture(Gdx.files.internal("Avatars/snake.png"));
        avatar1.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        Image avatarImage10 = new Image(avatar10);

        avatarImages = new Image[]{avatarImage1, avatarImage2, avatarImage3, avatarImage4, avatarImage5, avatarImage6, avatarImage7, avatarImage8, avatarImage9, avatarImage10 };
        currentAvatar = avatarImages[0];

        //Toujours à la fin du constructeur sinon plein d'erreurs de merde
        create();
    }

    public abstract void create();
    public abstract void update(float dt);

    // gameloop code; update, then render.
    public void render(float dt)
    {
        uiStage.act(dt);
        // only pause gameplay events, not UI events
        if ( !isPaused() )
        {
            mainStage.act(dt);
            update(dt);
        }
        // render
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
    public boolean scrolled(int amount)
    { return false; }
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    { return false; }
    public boolean touchDragged(int screenX, int screenY, int pointer)
    { return false; }
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    { return false; }

}



