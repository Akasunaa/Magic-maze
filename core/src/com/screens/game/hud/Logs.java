package com.screens.game.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class Logs extends TextArea{
    public Logs(Skin skin){
        super("", skin, "logStyle");
        setVisible(true);
        setText("Début de la partie!");
    }

    public void newMessage(String message){
        appendText("\n" + message);
    }

}
