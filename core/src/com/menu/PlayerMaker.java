package com.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.tiles.Player;

import static com.utils.Functions.modulo;

public class PlayerMaker {
    BaseActor avatar;
    String pseudo;
    Button rightArrow;
    Button leftArrow;
    int avatarNum = 0; // à changer
    // Pas besoin de flèches pour modifier les avatars des autres, ça n'a aucun sens ?
    TextField textField;
    boolean isModifiable;

    PlayerMaker(String pseudo, String avatarName, Skin uiSkin, boolean isModifiable) {
        this.isModifiable = isModifiable;
        this.pseudo = pseudo;
        textField = new TextField(pseudo,uiSkin);
        textField.setDisabled(true);
        this.avatar = new BaseActor();
        avatar.setTexture(new Texture(Gdx.files.internal("Avatars/" + avatarName + ".png")));
        for (int i = 0; i < BaseScreen.animalNames.length; i++) {
            if (BaseScreen.animalNames[i].equals(avatarName)) {
                avatarNum = i;
                break;
            }
        }
        // On doit faire ça pour récupérer le numéro de l'avatar
        //avatar.setSize(300,300);
    }

    PlayerMaker(Player player, Skin uiSkin, boolean isModifiable) {
        // Pour faire un de ces trucs à partir d'un Player
        this.isModifiable = isModifiable;
        pseudo = player.pseudo;
        avatar = player.avatar;
        textField = new TextField(pseudo,uiSkin);
        textField.setDisabled(true);
        for (int i = 0; i < BaseScreen.animalNames.length; i++) {
            if (BaseScreen.animalNames[i].equals(player.avatarName)) {
                avatarNum = i;
                break;
            }
        }
    }

    public void addTextField(Table uiTable, int numberOfPlayers) {
        uiTable.add(textField).center().padTop(200).colspan(24/numberOfPlayers);
        uiTable.getCell(textField).width(300);
        textField.setMaxLength(13);
    }

    private void addAvatar(Table uiTable, int numberOfPlayers) {
        uiTable.add(avatar).center().pad(20,0,20,0).colspan(4);
    }
    public void load(Skin skin, final Table uiTable, int numberOfPlayers) {
        if (isModifiable) {
            textField.setDisabled(false);
        }
        addLeftArrow(skin, uiTable,numberOfPlayers);
        addAvatar(uiTable,numberOfPlayers);
        addRightArrow(skin, uiTable,numberOfPlayers);
    }
    private void addLeftArrow(Skin skin, final Table uiTable,int numberOfPlayers) {
        Texture leftArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_left.png"));
        skin.add("leftArrow", leftArrowTexture );
        Button.ButtonStyle leftArrowStyle = new Button.ButtonStyle();
        leftArrowStyle.up = skin.getDrawable("leftArrow");
        leftArrow = new Button( leftArrowStyle );
        if (isModifiable) {
            leftArrow.addListener(
                    new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }

                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            avatarNum = modulo(avatarNum - 1, 10);
                            avatar.setTexture(new Texture(Gdx.files.internal("Avatars/" + BaseScreen.animalNames[avatarNum] + ".png")));
                            //avatar.setSize(150, 150);

                        }
                    });
        }
        uiTable.add(leftArrow).center().pad(20, 0, 20, 0).colspan((24-4*numberOfPlayers)/(2*numberOfPlayers));
        leftArrow.setVisible(isModifiable);

    }
    private void addRightArrow(Skin skin, final Table uiTable,int numberOfPlayers) {
        Texture rightArrowTexture = new Texture(Gdx.files.internal("MenuAssets/arrowSilver_right.png"));
        skin.add("rightArrow", rightArrowTexture);
        Button.ButtonStyle rightArrowStyle = new Button.ButtonStyle();
        rightArrowStyle.up = skin.getDrawable("rightArrow");
        rightArrow = new Button( rightArrowStyle );
        if (isModifiable) {
            rightArrow.addListener(
                    new InputListener() {
                        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                            return true;
                        }

                        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                            avatarNum = modulo(avatarNum + 1, 10);
                            avatar.setTexture(new Texture(Gdx.files.internal("Avatars/" + BaseScreen.animalNames[avatarNum] + ".png")));
                            //avatar.setSize(150, 150);

                        }
                    });
        }
        uiTable.add(rightArrow).center().pad(20, 0, 20, 0).colspan((24-4*numberOfPlayers)/(2*numberOfPlayers));
        rightArrow.setVisible(isModifiable);
    }

}
