package com.screens.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.multiplayer.messages.TextMessage;
import com.screens.BaseScreen;
import com.screens.game.board.Player;
import com.utils.Multiplayer;

import static com.utils.Functions.modulo;

public class PlayerMaker {
    private int avatarNum = 0;
    // pour pouvoir accéder facilement à la référence des avatars
    // Pas besoin de flèches pour modifier les avatars des autres, ça n'a aucun sens ?
    private final TextField textField;
    private final boolean isModifiable;
    private final Player player;

    String getPseudo() {
        return player.pseudo;
    }
    PlayerMaker(final Player player, Skin uiSkin, boolean isModifiable) {
        // Pour faire un de ces trucs à partir d'un Player
        this.isModifiable = isModifiable;
        this.player = player;
        textField = new TextField(player.pseudo,uiSkin);
        textField.setDisabled(true);
        player.avatar.setOrigin(player.avatar.getWidth()/2, player.avatar.getHeight()/2);
        for (int i = 0; i < BaseScreen.animalNames.length; i++) {
            if (BaseScreen.animalNames[i].equals(player.avatarName)) {
                avatarNum = i;
                break;
            }
        }
        textField.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Multiplayer.courrier.sendMessage(new TextMessage("changePseudo", textField.getText()));
                player.pseudo = textField.getText();
            }
        });
    }

    public void addTextField(Table uiTable, int colspan, int numberOfPlayer) {
        uiTable.add(textField).center().padTop(200).colspan(colspan);
        uiTable.getCell(textField).fill(0.18f*numberOfPlayer,1f);
        //uiTable.getCell(textField).width(200);
        textField.setMaxLength(13);
    }

    private void addAvatar(Table uiTable, int colspan) {
        uiTable.add(player.avatar).center().pad(20,0,20,0).colspan(colspan).fill();
    }

    public void updateName() {
        textField.setText(player.pseudo);
    }

    public void updateAvatar() {
        player.avatar.setTexture(new Texture(Gdx.files.internal("Avatars/" + player.avatarName + ".png")));
    }

    private InputListener arrowsInputListener(final int i) {
        return new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                avatarNum = modulo(avatarNum + i, 10);
                player.avatarName = BaseScreen.animalNames[avatarNum];
                Multiplayer.courrier.sendMessage(new TextMessage("changeAvatar",player.avatarName));
                player.avatar.setTexture(new Texture(Gdx.files.internal("Avatars/" + player.avatarName + ".png")));
                //avatar.setSize(150, 150);
            }
        };
    }


    public void load(Skin skin, final Table uiTable, int numberOfPlayers, int numberOfColumns) {
        if (isModifiable) {
            textField.setDisabled(false);
        }
        //System.out.println((numberOfColumns-4*numberOfPlayers)/(2*numberOfPlayers));
        addLeftArrow(skin, uiTable,(numberOfColumns-4*numberOfPlayers)/(2*numberOfPlayers));
        addAvatar(uiTable,4);
        addRightArrow(skin, uiTable,(numberOfColumns-4*numberOfPlayers)/(2*numberOfPlayers));
    }
    private void addLeftArrow(Skin skin, final Table uiTable,int colspan) {
        Texture leftArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_left.png"));
        skin.add("leftArrow", leftArrowTexture );
        Button.ButtonStyle leftArrowStyle = new Button.ButtonStyle();
        leftArrowStyle.up = skin.getDrawable("leftArrow");
        Button leftArrow = new Button(leftArrowStyle);
        if (isModifiable) {
            leftArrow.addListener(arrowsInputListener(-1));
        }
        uiTable.add(leftArrow).right().pad(20, 0, 20, 0).colspan(colspan);
        if (colspan == 1) uiTable.getCell(leftArrow).center();
        leftArrow.setVisible(isModifiable);

    }
    private void addRightArrow(Skin skin, final Table uiTable,int colspan) {
        Texture rightArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_right.png"));
        skin.add("rightArrow", rightArrowTexture);
        Button.ButtonStyle rightArrowStyle = new Button.ButtonStyle();
        rightArrowStyle.up = skin.getDrawable("rightArrow");
        Button rightArrow = new Button(rightArrowStyle);
        if (isModifiable) {
            rightArrow.addListener(arrowsInputListener(+1));
        }
        uiTable.add(rightArrow).left().pad(20, 0, 20, 0).colspan(colspan);
        if (colspan == 1) uiTable.getCell(rightArrow).center();
        rightArrow.setVisible(isModifiable);
    }

}
