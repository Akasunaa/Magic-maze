package com.screens.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.screens.game.BaseActor;

public class Logs extends BaseActor{
    public BaseActor logBackground;
    public TextArea logArea;
    public int width;
    public int height;

    public Logs(Skin skin, Texture texture){
        super(texture);
        logArea = new TextArea("", skin);
        logBackground = new BaseActor(texture);
        logArea.setText("Debut de la partie!");
    }

    public void setSize(int a, int b) {
        width = a;
        height = b;
        logBackground.setSize(width,height);
        logArea.setSize(width, height);
    }

    public void setPosition(int x, int y){
        logBackground.setPosition(x,y);
        logArea.setPosition(x,y);
    }

    public void newMessage(String message){
        logArea.appendText("\n" + message);
    }

}
