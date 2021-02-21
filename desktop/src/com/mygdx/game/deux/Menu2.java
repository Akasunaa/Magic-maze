package com.mygdx.game.deux;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.mygdx.game.desktop.BaseActor;
import com.mygdx.game.desktop.HaikyuuLevel;

public class Menu2 extends BaseScreen {
    public Menu2(Game g){ super (g);}

    public void create(){

        BaseActor background = new BaseActor();
        background.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\menu.png")));
        uiStage.addActor(background);

        BaseActor titre = new BaseActor();
        titre.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\Haikyuu_Logo.jpg")));
        titre.setPosition(0,100);
        uiStage.addActor(titre);

        BitmapFont font = new BitmapFont();
        String text = " Press S to start, M for main menu ";
        LabelStyle style = new LabelStyle( font, Color.YELLOW );
        Label instructions = new Label( text, style );
        instructions.setFontScale(2);
        instructions.setPosition(100, 50);
// repeating color pulse effect
        instructions.addAction(
                Actions.forever(
                        Actions.sequence(
                                Actions.color( new Color(1, 1, 0, 1), 0.5f ),
                                Actions.delay( 0.5f ),
                                Actions.color( new Color(0.5f, 0.5f, 0, 1), 0.5f )
                        )
                )
        );
        uiStage.addActor( instructions );


    }

    public void update(float dt){}
    public boolean keyDown(int keycode){
        if (keycode == Keys.S){
            game.setScreen( new HqLevel(game));
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
