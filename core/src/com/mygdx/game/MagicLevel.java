package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Rectangle;;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.audio.Music;

import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

public class MagicLevel extends BaseScreen
{
    private AnimatedActor mousey;
    private BaseActor cheese;
    private BaseActor floor;
    private BaseActor winText;
    private boolean win;
    private float timeElapsed;
    private Label timeLabel;

    // game world dimensions
    final int mapWidth = 1920;
    final int mapHeight = 1080;

    private float audioVolume;
    private Music instrumental;

    public Label pseudoLabel;

    private Table pauseOverlay;

    private float remainingTime;

    public MagicLevel(BaseGame g, int currentAvatarNumber, String playerName)
    {
        super(g);

        Cell<Image> cell = uiTable.getCell(currentAvatar);
        cell.clearActor();
        currentAvatar = avatarImages[currentAvatarNumber];
        cell.setActor(currentAvatar);

        Label currentPseudoLabel = new Label(playerName, game.skin, "uiLabelStyle");
        //C'est nawak???? wtf pour je suis obligé de faire ça???
        Cell<Label> cell1 = uiTable.getCell(pseudoLabel);
        cell1.clearActor();
        cell1.setActor(currentPseudoLabel);

    }

    public void create() {

        //C'est nawak???? wtf pourquoi je suis obligé de faire ça???
        pseudoLabel = new Label("", game.skin, "uiLabelStyle");

        timeElapsed = 0;
        remainingTime = 20;

        floor = new BaseActor();
        floor.setTexture(new Texture(Gdx.files.internal("GameAssets/tiles.jpg")));
        floor.setPosition(0, 0);
        mainStage.addActor(floor);

        cheese = new BaseActor();
        cheese.setTexture(new Texture(Gdx.files.internal("GameAssets/star.png")));
        cheese.setPosition(400, 300);
        cheese.setOrigin(cheese.getWidth() / 2, cheese.getHeight() / 2);
        mainStage.addActor(cheese);
        mousey = new AnimatedActor();
        TextureRegion[] frames = new TextureRegion[4];
        for (int n = 0; n < 4; n++) {
            String fileName = "GameAssets/mouse" + n + ".png";
            Texture tex = new Texture(Gdx.files.internal(fileName));
            tex.setFilter(TextureFilter.Linear, TextureFilter.Linear);
            frames[n] = new TextureRegion(tex);
        }
        Array<TextureRegion> framesArray = new Array<TextureRegion>(frames);
        Animation anim = new Animation(0.1f, framesArray, Animation.PlayMode.LOOP_PINGPONG);
        mousey.setAnimation(anim);
        mousey.setOrigin(mousey.getWidth() / 2, mousey.getHeight() / 2);
        mousey.setPosition(20, 20);
        mainStage.addActor(mousey);

        BitmapFont font = new BitmapFont();
        String text = "Time: ";
        LabelStyle style = new LabelStyle(font, Color.NAVY);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(2);
        timeLabel.setPosition(960, 1000); // sets bottom left (baseline) corner?
        uiStage.addActor(timeLabel);

        win = false;

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Gaur_Plain.ogg"));
        audioVolume = 0.80f;
        instrumental.setLooping(true);
        instrumental.setVolume(audioVolume);
        instrumental.play();

        final Slider audioSlider = new Slider(0, 1, 0.005f, false, game.skin, "uiSliderStyle" );
        audioSlider.setValue( audioVolume );
        audioSlider.addListener(
                new ChangeListener()
                {
                    public void changed(ChangeEvent event, Actor actor)
                    {
                        audioVolume = audioSlider.getValue();
                        instrumental.setVolume(audioVolume);
                    }
                });

        Texture pauseTexture = new Texture(Gdx.files.internal("GameUIAssets/barsHorizontal.png"));
        game.skin.add("pauseImage", pauseTexture );
        ButtonStyle pauseStyle = new ButtonStyle();
        pauseStyle.up = game.skin.getDrawable("pauseImage");
        Button pauseButton = new Button( pauseStyle );
        pauseButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
                    {
                        togglePaused();
                        pauseOverlay.setVisible( isPaused() );
                        return true;
                    }
                });

        pauseOverlay = new Table();
        pauseOverlay.setFillParent(true);

        Stack stacker = new Stack();
        stacker.setFillParent(true);
        uiStage.addActor(stacker);
        stacker.add(uiTable);
        stacker.add(pauseOverlay);

        game.skin.add("white", new Texture( Gdx.files.internal("GameUIAssets/white4px.png")) );
        Drawable pauseBackground = game.skin.newDrawable("white", new Color(0,0,0,0.8f) );

        Label pauseLabel = new Label("Paused", game.skin, "uiLabelStyle");
        TextButton resumeButton = new TextButton("Resume", game.skin, "uiTextButtonStyle");
        resumeButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        togglePaused();
                        pauseOverlay.setVisible( isPaused() );
                    }
                });
        TextButton quitButton = new TextButton("Quit", game.skin, "uiTextButtonStyle");
        quitButton.addListener(
                new InputListener()
                {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer,
                                              int button)
                    { return true; }
                    public void touchUp (InputEvent event, float x, float y, int pointer, int button)
                    {
                        dispose();
                        Gdx.app.exit();
                    }
                });
        Label volumeLabel = new Label("Volume", game.skin, "uiLabelStyle");

        float w = resumeButton.getWidth();
        pauseOverlay.setBackground(pauseBackground);
        pauseOverlay.add(pauseLabel).pad(20);
        pauseOverlay.row();
        pauseOverlay.add(resumeButton);
        pauseOverlay.row();
        pauseOverlay.add(quitButton).width(w);
        pauseOverlay.row();
        pauseOverlay.add(volumeLabel).padTop(100);
        pauseOverlay.row();
        pauseOverlay.add(audioSlider).width(400);

        pauseOverlay.setVisible(false);

        uiTable.pad(10);
        uiTable.add(pseudoLabel);
        uiTable.add(currentAvatar).padLeft(50);
        uiTable.add().expandX();
        uiTable.add(pauseButton);
        uiTable.row();
        uiTable.add().colspan(4).expandY();


    }

    public void update(float dt)
    {

        //Gdx.gl.glClearColor(1, 0, 0, 0);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //hourglassBatch.begin();
        //hourglassBatch.draw(hourglassAnimation.getKeyFrame(dt), 20.0f, 20.0f);
        //hourglassBatch.end();

            // process input
        mousey.velocityX = 0;
        mousey.velocityY = 0;
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            mousey.velocityX -= 100;
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            mousey.velocityX += 100;
        if (Gdx.input.isKeyPressed(Keys.UP))
            mousey.velocityY += 100;
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            mousey.velocityY -= 100;

        // bound mousey to the rectangle defined by mapWidth, mapHeight
        mousey.setX( MathUtils.clamp( mousey.getX(), 0, mapWidth - mousey.getWidth() ));
        mousey.setY( MathUtils.clamp( mousey.getY(), 0, mapHeight - mousey.getHeight() ));

        // check win condition: mousey must be overlapping cheese
        Rectangle cheeseRectangle = cheese.getBoundingRectangle();
        Rectangle mouseyRectangle = mousey.getBoundingRectangle();
        if ( !win && cheeseRectangle.contains( mouseyRectangle ) )
        {
            win = true;
            dispose();
            game.setScreen( new VictoryScreen(game) );

        }
        if (!win)
        {
            timeElapsed += dt;
            timeLabel.setText( "Time: " + (int)remainingTime );
            remainingTime -= dt;

            if (remainingTime < 0) {
                dispose();
                game.setScreen(new DefeatScreen(game));
            }
        }
        // camera adjustment
        Camera cam = mainStage.getCamera();
        if (Gdx.input.isKeyPressed(Keys.NUMPAD_SUBTRACT)) {
            ((OrthographicCamera)cam).zoom += 0.02;
        }
        if (Gdx.input.isKeyPressed(Keys.NUMPAD_ADD)) {
            ((OrthographicCamera)cam).zoom -= 0.02;
        }

        // center camera on player
        cam.position.set( mousey.getX() + mousey.getOriginX(),
                mousey.getY() + mousey.getOriginY(), 0 );
        // bound camera to layout
        cam.position.x = MathUtils.clamp(cam.position.x, viewWidth/2, mapWidth-viewWidth/2);
        cam.position.y = MathUtils.clamp(cam.position.y, viewHeight/2, mapHeight-viewHeight/2);
        cam.update();
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    public void dispose()
    {
        instrumental.dispose();
    }
}
