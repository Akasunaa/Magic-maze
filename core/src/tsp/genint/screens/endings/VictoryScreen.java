package tsp.genint.screens.endings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import tsp.genint.screens.BaseScreen;
import tsp.genint.screens.MagicGame;
import tsp.genint.screens.game.BaseActor;

public class VictoryScreen extends BaseScreen {

    public VictoryScreen(MagicGame g){
        super(g);
    }

    @Override
    public void create() {

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Victory.wav"));
        instrumental.setLooping(true);
        instrumental.play();

        Label victoryLabel = new Label( "Victoire !!!!", game.skin, "uiLabelStyle" );

        BaseActor background = new BaseActor(new Texture(Gdx.files.internal("Game/VictoryImage.jpg")));
        uiStage.addActor( background );

        background.toBack();

        uiTable.add(victoryLabel);

        Action fadeInColorCycleForever = Actions.sequence(
                Actions.alpha(0), // set transparency value
                Actions.show(), // set visible to true
                Actions.fadeIn(2), // duration of fade out
                Actions.forever(
                        Actions.sequence(
                                // color shade to approach, duration
                                Actions.color(new Color(1, 0, 0, 1), 1),
                                Actions.color(new Color(0, 0, 1, 1), 1)
                        )
                )
        );
        background.addAction(fadeInColorCycleForever);

    }

    @Override
    public void update(float dt) {

    }

}
