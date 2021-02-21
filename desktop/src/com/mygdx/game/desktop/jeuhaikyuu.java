package com.mygdx.game.desktop;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Game;

public class jeuhaikyuu extends Game {
    private SpriteBatch batch;

    private Texture joueurTexture;
    private float joueurX;
    private float joueurY;

    private Texture goalTexture;
    private float goalX;
    private float goalY;

    private Texture floorTexture;
    private Texture winMessage;

    private boolean win;

    public void create(){
        batch = new SpriteBatch();

        joueurTexture = new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\joueurtete1.png"));
        joueurX = 20;
        joueurY = 20;

        goalTexture = new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\goal1.png"));
        goalX = 400;
        goalY = 300;

        floorTexture = new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\fond1.jpg"));
        winMessage = new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\win.jpg"));

        win = false;
    }

    public void render(){
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            joueurX--;
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            joueurX++;
        }
        if (Gdx.input.isKeyPressed(Keys.UP)){
            joueurY++;
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)){
            joueurY--;
        }

        if ((joueurX > goalX) && (joueurX+joueurTexture.getWidth() < goalX + goalTexture.getWidth()) && (joueurY > goalY) && (joueurY + joueurTexture.getHeight() < goalY + goalTexture.getHeight())){
            win = true;
        }

        Gdx.gl.glClearColor(0.8f,0.8f,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(floorTexture,0 ,0);
        batch.draw(goalTexture, goalX, goalY);
        batch.draw(joueurTexture, joueurX, joueurY);
        if (win){
            batch.draw(winMessage, 170, 60);
        }
        batch.end();
    }
}
