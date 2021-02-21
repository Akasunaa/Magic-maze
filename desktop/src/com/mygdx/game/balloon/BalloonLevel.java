package com.mygdx.game.balloon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.mygdx.game.deux.BaseScreen;

public class BalloonLevel extends BBaseScreen {
    private BBaseActor background;

    private float spawnTimer;
    private float spawnInterval;

    private int popped;
    private int escaped;
    private int clickcount;

    private Label poppedLabel;
    private Label escapedLabel;
    private Label hitRatioLabel;

    final int mapWidth = 640;
    final int mapHeight = 480;

    public BalloonLevel(Game g){
        super(g);
    }

    public void create(){
        background = new BBaseActor();
        background.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\vollay.jpg")));
        background.setPosition(0,0);
        mainStage.addActor(background);

        spawnTimer = 0;
        spawnInterval = 0.5f;

        BitmapFont font = new BitmapFont();
        LabelStyle style = new LabelStyle(font, Color.BROWN);

        popped = 0;
        poppedLabel = new Label("Popped: 0", style);
        poppedLabel.setFontScale(2);
        poppedLabel.setPosition(20,440);
        uiStage.addActor(poppedLabel);

        escaped = 0;
        escapedLabel = new Label("Escaped: 0", style);
        escapedLabel.setFontScale(2);
        escapedLabel.setPosition(220,440);
        uiStage.addActor(escapedLabel);

        clickcount = 0;
        hitRatioLabel = new Label("Hit Ratio: ---", style);
        hitRatioLabel.setFontScale(2);
        hitRatioLabel.setPosition(420,440);
        uiStage.addActor(hitRatioLabel);

    }

    public void update(float dt){
        spawnTimer += dt;
        if (spawnTimer > spawnInterval){
            spawnTimer -= spawnInterval;
            final Balloon b = new Balloon();
            b.addListener(
                    new InputListener(){
                        public boolean touchDown (InputEvent ev, float x, float y, int pointer, int button){
                            popped++;
                            b.remove();
                            return true;
                        }
                    }
            );
            mainStage.addActor(b);
        }
        for (Actor a : mainStage.getActors()){
            if (a.getX() > mapWidth || a.getY() > mapHeight){
                escaped++;
                a.remove();
            }
        }

        poppedLabel.setText("Popped:" + popped);
        escapedLabel.setText("Escaped:" + escaped);
        if (clickcount > 0){
            int percent = (int)(100.0 * popped / clickcount);
            hitRatioLabel.setText("Hit Ratio:" + percent +"%");
        }

    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button){
        clickcount++;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
