package com.screens.game.hud;

import com.badlogic.gdx.graphics.Texture;
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
        logBackground = new BaseActor(texture);
        logBackground.setVisible(false);
        logArea = new TextArea("", skin, "logStyle");
        logArea.setVisible(true);
        // Les textArea on déjà un background
//        logBackground.setColor(new Color(1,1,1,1f));
        logArea.setText("Début de la partie!");
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
