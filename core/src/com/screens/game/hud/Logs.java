package com.screens.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.screens.game.BaseActor;

public class Logs extends BaseActor {
    public Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
    public BaseActor logBackground = new BaseActor();
    public TextArea logArea = new TextArea("", uiSkin);

    public Logs(int width, int height, int x, int y){
        logBackground.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        logBackground.setSize(width,height);
        logBackground.setPosition(x,y);
        logArea.setSize(width, height);
        logArea.setPosition(x,y);
        logArea.setText("Debut de la partie!");
    }

    public void newMessage(String message){
        logArea.appendText("\n" + message);
    }

}
