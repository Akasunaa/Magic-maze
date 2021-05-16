package com.screens.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.multiplayer.messages.Ping;
import com.screens.game.BaseActor;
import com.screens.game.board.Case;
import com.screens.game.board.Player;
import com.utils.Multiplayer;
import com.utils.TileAndCases;

import java.util.ArrayList;

import static com.screens.GameScreens.gameScreen;
import static com.utils.Directions.*;
import static com.utils.Directions.numberDirections;
import static com.utils.Functions.modulo;
import static com.utils.MainConstants.getFontSize;
import static com.utils.Multiplayer.courrier;

public class PlayerOnHUD {
    private final BaseActor avatar;
    private final ArrayList<BaseActor> powers = new ArrayList<>();
    private final Label pseudoLabel;

    private final BaseActor wantsToRestart;

    PlayerOnHUD(final Player player) {
        // On charge l'avatar et le panneau qui indique qu'on veut restart
        avatar = player.avatar;
        avatar.setSize(90,90);
        pseudoLabel = new Label(player.pseudo, new Label.LabelStyle(getFontSize(18*2), Color.WHITE));
        pseudoLabel.setFontScale(0.5f);
        pseudoLabel.setOrigin(Align.center);

        wantsToRestart = new BaseActor(new Texture(Gdx.files.internal("interface/restart-button.png")));

        avatar.addListener(new InputListener() {
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar.addAction(Actions.sequence(
                        Actions.color(new Color(1,0,0,1),(float)0.20),
                        Actions.color(new Color(1,1,1,1),(float)0.20)));
                courrier.sendMessage(new Ping(player.pseudo));
                return true;
            }
        });
        // C'est juste le code pour envoyer des pings

        // Et maintenant il faut regarder chacun des booléens pour savoir quel pouvoir notre joueur possède
        if (player.north) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/upArrow.png"))));
        }

        if (player.west) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/leftArrow.png"))));
        }

        if (player.south) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/downArrow.png"))));
        }

        if (player.east) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/rightArrow.png"))));
        }
        if (player.escalatorTaker) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/escalator.png"))));
        }

        if (player.shortcutTaker) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/shortcut.png"))));
        }

        if (player.portalTaker){
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Powers/portal.png"))));
        }

        // Et maintenant on ajuste la taille
        for (BaseActor power : powers) {
            power.setSize(30,30);
        }
    }

    float getWidth() {return avatar.getWidth();}
    float getHeight() {return avatar.getHeight();}

    void setPosition(float x, float y) {
        avatar.setPosition(x,y);
        pseudoLabel.setPosition(avatar.getX(), avatar.getY()-30);
        int i = 0;
        for (BaseActor power : powers) {
            power.setPosition(avatar.getX() + i*(avatar.getWidth()-15)/powers.size(), avatar.getY());
            i++;
        }
        wantsToRestart.setPosition(avatar.getX() - 15, avatar.getY() + avatar.getHeight()*1.5f / 2);
    }

    void addToStage(Stage stage) {
        stage.addActor(avatar);
        stage.addActor(pseudoLabel);
        for (BaseActor power : powers) {
            stage.addActor(power);
        }
        stage.addActor(wantsToRestart);
        wantsToRestart.setVisible(false);
    }
    void wantsToRestart() {
        wantsToRestart.setVisible(true);
    }

}
