package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Screen;


public class Menu  implements Screen{
    private Stage uiStage;
    private Game game;

    public Menu(Game g){
        game = g;
        create();
    }

    public void create(){
        uiStage = new Stage();

        BaseActor background = new BaseActor();
        background.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\menu.png")));
        uiStage.addActor(background);

        BaseActor titre = new BaseActor();
        titre.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\Haikyuu_Logo.jpg")));
        titre.setPosition(0,100);
        uiStage.addActor(titre);

        BitmapFont font = new BitmapFont();
        String text = "Press S to start, M for main menu";
        LabelStyle style = new LabelStyle(font, Color.WHITE);
        Label instructions = new Label(text, style);
        instructions.setFontScale(2);
        instructions.setPosition(100,50);
        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color (new Color(1,1,0,1), 0.5f),
                                Actions.delay(0.5f),
                                Actions.color (new Color(0.5f,0.5f,0,1),0.5f)
                        )
                )
        );
        uiStage.addActor(instructions);
    }

    public void render(float dt){
        if (Gdx.input.isKeyPressed(Keys.S)){
            game.setScreen(new HaikyuuLevel(game));
        }

        uiStage.act(dt);

        Gdx.gl.glClearColor(0.8f,0.8f,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        uiStage.draw();
    }

    public void resize(int width, int height){}
    public void pause() {}
    public void resume() {}
    public void dispose() {}
    public void show(){}
    public void hide(){}
}
