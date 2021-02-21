package com.mygdx.game.deux;

import com.badlogic.gdx.Input;
import com.mygdx.game.desktop.*;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.desktop.Menu;

public class HqLevel extends BaseScreen{

    private BaseActor oikawa;
    private BaseActor goal;
    private BaseActor fond;
    private BaseActor winText;
    private boolean win;
    private float timeElapsed;
    private Label timeLabel;

    final int mapWidth = 800;
    final int mapHeight = 800;


    public HqLevel(Game g)
    { super(g); }

    public void create() {
        timeElapsed = 0;

        fond = new BaseActor();
        fond.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\fond1.jpg")));
        fond.setPosition(0, 0);
        mainStage.addActor(fond);

        goal = new BaseActor();
        goal.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\goal1.png")));
        goal.setPosition(400, 300);
        mainStage.addActor(goal);

        oikawa = new BaseActor();
        oikawa.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\joueurtete1.png")));
        oikawa.setPosition(20, 20);
        mainStage.addActor(oikawa);

        goal.setOrigin(goal.getWidth() / 2, goal.getHeight() / 2);

        winText = new BaseActor();
        winText.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\win.jpg")));
        winText.setPosition(170, 60);
        winText.setVisible(false);
        uiStage.addActor(winText);

        timeElapsed = 0;
        BitmapFont font = new BitmapFont();
        String text = "Time: 0";
        Label.LabelStyle style = new Label.LabelStyle(font, Color.NAVY);
        timeLabel = new Label (text, style);
        timeLabel.setFontScale(2);
        timeLabel.setPosition(500,440);
        uiStage.addActor(timeLabel);

        win = false;
    }

    public void update(float dt){
        oikawa.velocityX = 0;
        oikawa.velocityY = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
            oikawa.velocityX += 150;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)){
            oikawa.velocityX -= 150;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            oikawa.velocityY -= 150;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)){
            oikawa.velocityY += 150;
        }

        if (!win){
            timeElapsed += dt;
            timeLabel.setText("Time:" + (int)timeElapsed);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.M)){
            game.setScreen(new Menu(game));
        }

        mainStage.act(dt);
        uiStage.act(dt);

        oikawa.setX(MathUtils.clamp(oikawa.getX(), 0, mapWidth - oikawa.getWidth()));
        oikawa.setY(MathUtils.clamp(oikawa.getY(), 0, mapHeight - oikawa.getHeight()));


        Rectangle goalRectangle = goal.getBoundingRectangle();
        Rectangle oikawaRectangle = oikawa.getBoundingRectangle();

        if (!win && goalRectangle.contains(oikawaRectangle)){
            win = true;

            Action spinShrinkFadeOut = Actions.parallel(
                    Actions.alpha(1),
                    Actions.rotateBy(360,1),
                    Actions.scaleTo(0,0,1),
                    Actions.fadeOut(1)
            );
            goal.addAction(spinShrinkFadeOut);

            Action fadeInColorCycleForever = Actions.sequence(
                    Actions.alpha(0),
                    Actions.show(),
                    Actions.fadeIn(2),
                    Actions.forever(Actions.sequence(
                            Actions.color(new Color(1,0,0,1),1),
                            Actions.color ( new Color (0,0,1,1),1)
                    ))
            );
            winText.addAction(fadeInColorCycleForever);
        }

        if (!win) {
            timeElapsed+= dt;
            timeLabel.setText("Time:" + (int)timeElapsed);
        }

        Camera cam = mainStage.getCamera();
        cam.position.set(oikawa.getX() + oikawa.getOriginX(), oikawa.getY() + oikawa.getOriginY(), 0);;
        cam.position.x = MathUtils.clamp(cam.position.x, viewWidth/2, mapWidth - viewWidth/2);
        cam.position.y = MathUtils.clamp(cam.position.y, viewHeight/2, mapHeight - viewHeight/2);
        cam.update();
    }
    public boolean keyDown(int keycode){
        if (keycode == Keys.M) {
            game.setScreen(new Menu2(game));
        }
        if (keycode == Keys.P) {
            togglePaused();
        }
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
