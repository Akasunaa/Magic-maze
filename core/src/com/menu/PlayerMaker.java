package com.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;

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
        avatar.setSize(150,150);

    }

    public void addTextField(Table uiTable) {
        uiTable.add(textField).center().padTop(200).colspan(3);
    }

    private void addAvatar(Table uiTable, int left, int right) {
        uiTable.add(avatar).pad(20,left,20,right);
    }
    public void load(Skin skin, final Table uiTable) {
        if (isModifiable) {
            textField.setDisabled(false);
        }
        addLeftArrow(skin, uiTable);
        addAvatar(uiTable, 0, 0);
        addRightArrow(skin, uiTable);
    }
    private void addLeftArrow(Skin skin, final Table uiTable) {
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
                            avatar.setSize(150, 150);

                        }
                    });
        }
        uiTable.add(leftArrow).pad(20, 20, 20, 20);
        leftArrow.setVisible(isModifiable);

    }
    private void addRightArrow(Skin skin, final Table uiTable) {
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
                            avatar.setSize(150, 150);

                        }
                    });
        }
        uiTable.add(rightArrow).pad(20, 20, 20, 20);
        rightArrow.setVisible(isModifiable);
    }

}
