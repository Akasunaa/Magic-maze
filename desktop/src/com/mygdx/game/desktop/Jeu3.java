package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Jeu3 extends Game{

    public Stage mainStage;
    private BaseActor oikawa;
    private BaseActor goal;
    private BaseActor fond;
    private BaseActor winText;

    public void create(){
        mainStage = new Stage();

        fond = new BaseActor();
        fond.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\fond1.jpg")));
        fond.setPosition(0,0);
        mainStage.addActor(fond);

        goal = new BaseActor();
        goal.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\goal1.png")));
        goal.setPosition(400,300);
        mainStage.addActor(goal);

        oikawa = new BaseActor();
        oikawa.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\joueurtete1.png")));
        oikawa.setPosition(20,20);
        mainStage.addActor(oikawa);

        winText = new BaseActor();
        winText.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\win.jpg")));
        winText.setPosition(170,60);
        winText.setVisible(false);
        mainStage.addActor(winText);
    }

    public void render(){
        oikawa.velocityX = 0;
        oikawa.velocityY = 0;

        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            oikawa.velocityX -= 100;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            oikawa.velocityX += 100;
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            oikawa.velocityX -= 100;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)){
            oikawa.velocityY -= 100;
        }
        if (Gdx.input.isKeyPressed(Keys.UP)){
            oikawa.velocityY += 100;
        }

        float dt = Gdx.graphics.getDeltaTime();
        mainStage.act(dt);

        Rectangle goalRectangle = goal.getBoundingRectangle();
        Rectangle oikawaRectangle = oikawa.getBoundingRectangle();

        if (goalRectangle.contains(oikawaRectangle)){
            winText.setVisible(true);
        }

        Gdx.gl.glClearColor(0.8f,0.8f,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
    }
}
