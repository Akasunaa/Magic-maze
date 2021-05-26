package com.screens.endings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.screens.BaseScreen;
import com.screens.MagicGame;
import com.screens.game.BaseActor;

public class DefeatScreen extends BaseScreen {

    public DefeatScreen(MagicGame g){
        super(g);
    }

    @Override
    public void create() {

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Defeat.mp3"));
        instrumental.setLooping(true);
        instrumental.play();
        instrumental.setVolume(game.audioVolume);

        Label defeatLabel = new Label( "Defaite...", game.skin, "uiLabelStyle" );

        BaseActor background = new BaseActor(new Texture(Gdx.files.internal("Game/DefeatImage.jpg")));
        uiStage.addActor( background );

        background.toBack();

        uiTable.add(defeatLabel);

        Action fadeInColorCycleForever = Actions.sequence(
                Actions.alpha(0), // set transparency value
                Actions.show(), // set visible to true
                Actions.fadeIn(2), // duration of fade out
                Actions.forever(
                        Actions.sequence(
                                // color shade to approach, duration
                                Actions.color( new Color(1,0,0,1), 1 ),
                                Actions.color( new Color(0,0,1,1), 1 )
                        )
                )
        );
        background.addAction( fadeInColorCycleForever );

    }

    @Override
    public void update(float dt) {

    }
}
