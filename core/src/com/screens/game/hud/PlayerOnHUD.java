package com.screens.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.multiplayer.messages.Ping;
import com.screens.game.BaseActor;
import com.screens.game.board.Player;
import com.utils.Multiplayer;

import java.util.ArrayList;

import static com.screens.GameScreens.game;
import static com.utils.Multiplayer.courrier;

/*
L'idée de cette classe, c'est de centraliser la gestion de l'affichage des avatars,
des pouvoirs, et des pseudos des joueurs, afin que ce soit plus simple
Ç'aurait dû être fait il y a longtemps, mais bon, à la base l'affichage des avatars n'était même
pas fait dans une boucle, c'était fait à la main, avec des avatar1, avatar2, avatar3, avatar4, etc.,
donc créer toute une classe pour gérer ça était peut être un peu... beaucoup...
Voilà donc cette classe, créée un peu in extremis quelques jours avant le 18, pour essayer
de remédier à la lourdeur du code dans GameInterface.java (et encore, il reste pas mal de truc
à nettoyer, cela peut notamment se voir avec les gros blocs de code commentés qu'il serait bien de retirer,
étant donné qu'ils sont pour la plupart obsolètes...)
Bref, tout ça pour dire que cette classe est essentiellement un sucre syntactique, elle n'est essentiel
qu'à la beauté du code et à ma santé mentale.
Elle n'est pas trop dure à comprendre, mais dans le doute, hésitez pas à me demander
Hadrien.
 */

public class PlayerOnHUD {
    private final BaseActor avatar;
    private final ArrayList<BaseActor> powers = new ArrayList<>();
    private final Label pseudoLabel;

    private final BaseActor wantsToRestart;

    PlayerOnHUD(final Player player, float size) {
        // On charge l'avatar et le panneau qui indique qu'on veut restart
        avatar = player.avatar;
        avatar.setSize(size,size);
//        avatar.debug();
//        pseudoLabel.debug();

        wantsToRestart = new BaseActor(new Texture(Gdx.files.internal("interface/restart-button.png")));

        avatar.addListener(new InputListener() {
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar.addAction(Actions.sequence(
                        Actions.color(new Color(1,0,0,1),0.20f),
                        Actions.color(new Color(1,1,1,1),0.20f)));
                courrier.sendMessage(new Ping(player.pseudo));
                return true;
            }
        });
        // C'est juste le code pour envoyer des pings

        // Et maintenant il faut regarder chacun des booléens pour savoir quel pouvoir notre joueur possède

        try {
            System.out.println(Multiplayer.mapper.writeValueAsString(player));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (player.north) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/upArrow.png"))));
        }

        if (player.west) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/leftArrow.png"))));
        }

        if (player.south) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/downArrow.png"))));
        }

        if (player.east) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/rightArrow.png"))));
        }
        if (player.escalatorTaker) {
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/escalator.png"))));
        }

        if (player.portalTaker){
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/portal.png"))));
        }

        if (player.cardChooser){
            powers.add(new BaseActor(new Texture(Gdx.files.internal("Game/Powers/magnifier.png"))));
        }

        // Et maintenant on ajuste la taille
        for (BaseActor power : powers) {
            power.setSize(30,30);
            power.setOrigin(15,15);
            power.setTouchable(Touchable.disabled);
        }

        pseudoLabel = new Label(player.pseudo, game.skin);
//        pseudoLabel.setFontScale(0.8f);
        pseudoLabel.setAlignment(Align.center);
    }

    float getWidth() {return avatar.getWidth();}
    float getHeight() {return avatar.getHeight();}

    void setPosition(float x, float y) {
        avatar.setPosition(x,y);
        int i = 0;
        for (BaseActor power : powers) {
            power.setPosition(avatar.getX() + (i+1)*(avatar.getWidth())/(powers.size()+2), avatar.getY());
            i++;
        }
        pseudoLabel.setPosition(avatar.getX()+(avatar.getWidth()- pseudoLabel.getWidth())/2, avatar.getY()-30);
        wantsToRestart.setPosition(avatar.getX() - 15, avatar.getY() + avatar.getHeight()*1.5f / 2);
    }

    void addToStage(Stage stage) {
        stage.addActor(avatar);
        for (BaseActor power : powers) {
            stage.addActor(power);
        }
        stage.addActor(pseudoLabel);
        stage.addActor(wantsToRestart);
        wantsToRestart.setVisible(false);
    }
    void wantsToRestart() {
        wantsToRestart.setVisible(true);
    }

}
