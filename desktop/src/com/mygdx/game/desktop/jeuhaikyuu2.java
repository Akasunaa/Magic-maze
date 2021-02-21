package com.mygdx.game.desktop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class jeuhaikyuu2 extends Game{
    private SpriteBatch batch;
    private Sprite oikawasprite;
    private Sprite goalsprite;
    private Sprite fondsprite;
    private Sprite winsprite;
    private boolean win;

    public void create(){
        batch = new SpriteBatch();

        oikawasprite = new Sprite(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\joueurtete1.png")));
        oikawasprite.setPosition(20,20);

        goalsprite = new Sprite(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\goal1.png")));
        goalsprite.setPosition(400,300);

        fondsprite = new Sprite(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\fond1.jpg")));
        fondsprite.setPosition(0,0);

        winsprite = new Sprite(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\win.jpg")));
        winsprite.setPosition(170,60);

        win = false;
    }

    public void render(){

        //process input
        if (Gdx.input.isKeyPressed(Keys.LEFT)){
            oikawasprite.translateX(-1);
        }
        if (Gdx.input.isKeyPressed(Keys.RIGHT)){
            oikawasprite.translateX(1);
        }
        if (Gdx.input.isKeyPressed(Keys.UP)){
            oikawasprite.translateY(1);
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)){
            oikawasprite.translateY(-1);
        }

        //check win condition
        Rectangle goalRectangle = goalsprite.getBoundingRectangle();
        Rectangle oikawaRectangle = oikawasprite.getBoundingRectangle();
        if (goalRectangle.overlaps(oikawaRectangle)){
            //contains pour qu'il soit entièrement dedans
            win = true;
        }

        //draw graphics
        Gdx.gl.glClearColor(0.8f, 0.8f,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        fondsprite.draw(batch);
        goalsprite.draw(batch);
        oikawasprite.draw(batch);
        if(win){
            winsprite.draw(batch);
        }
        batch.end();
    }
}
