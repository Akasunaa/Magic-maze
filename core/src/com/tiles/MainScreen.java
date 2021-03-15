package com.tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.menu.*;
import com.utils.Functions;


import java.util.ArrayList;

// La caméra
import static com.utils.Functions.*;

// La liste des tuiles affichées sur l'écran

// le batch pour dessiner
import static com.utils.MainConstants.*;
import static com.utils.TileAndCases.*;

// btw, le fait que MainScreen soit dans le dossier multi c'est un peu chelou niveau orga non?

public class MainScreen extends BaseScreen {
    // Trucs de déboguages pour afficher les coordonées de la souris
    Label coordMouse;
    Label numberCase;


    // Le pion
    Pawn greenPawn;
    boolean pawnTime = false;

    // Le joueur
    Player player;

    // Pour la sérialisation
    ObjectMapper mapper = new ObjectMapper();

    GameInterface gameInterface;

    // Bordel de Nathan
    // Timer et consort
    private float remainingTime;
    private float timeElapsed;
    private Label timeLabel;
    private AnimatedActor Animatedhourglass;

    // Pseudo et avatars partagés entre les écrans
    public Label pseudoLabel;

    // Pour le "menu" de "pause" (en vrai à voir si c'est nécessaire et comment c'est géré evec le multi
    private Table pauseOverlay;

    private boolean win;


    public String stringMousePosition(OrthographicCamera camera) {
        return "x = " + (int) mouseInput(camera).x + "; y = " + (int) mouseInput(camera).y;
    }

    public MainScreen(MagicGame g, int currentAvatarNumber, String playerName, float audioVolume) {
        super(g);

        instrumental.setVolume(audioVolume);
        audioSlider.setValue( audioVolume );

        // Afficher l'avatar correspondant à celui choisi sur l'écran précédent
        Cell<Image> cell = uiTable.getCell(currentAvatar);
        cell.clearActor();
        currentAvatar = avatarImages[currentAvatarNumber];
        cell.setActor(currentAvatar);

        // Le pseudo affiché est celui rentré sur l'écran précédent
        Label currentPseudoLabel = new Label(playerName, game.skin, "uiLabelStyle");
        //C'est nawak???? wtf pour je suis obligé de faire ça???
        Cell<Label> cell1 = uiTable.getCell(pseudoLabel);
        cell1.clearActor();
        cell1.setActor(currentPseudoLabel);

    }

    Tile tempTile;
    // une tempTile on en aura besoin plus tard

    BaseActor background;

    public void create() {
        background = new BaseActor();
        background.setTexture(new Texture("GameUIAssets/floorboard.png"));
        background.setScale(1.5f);

        //camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera = (OrthographicCamera) mainStage.getCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.zoom = 2;
        // Cette caméra nous sert à avoir le bon système de coordonées
        Gdx.input.setInputProcessor(this);
        // Pour le système de zoom, il écoute les actions de la souris
        // Le niveau est un InputListener, weirdly enough
        // Je ne vais pas critiquer les actions de Nathan, il doit y avoir une raison à cela

        tileList = new ArrayList();

        player = new Player(true, true, false, false, false, false);

        queue = new Queue(9); // J'ai fait les cases uniquement jusqu'à la 9


        // Bon là c'est le batch et les trucs pour écrire, rien d'important
        batch = new SpriteBatch();
        BitmapFont font = getFontSize(40);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);
        coordMouse = new Label(" ", style);
        coordMouse.setPosition(200, 50);
        numberCase = new Label(" ", style);
        numberCase.setPosition(200,100);
        uiStage.addActor(coordMouse);
        uiStage.addActor(numberCase);

        //Ici c'est le bordel rajouté par Nathan

        win = false;

        //C'est nawak???? wtf pourquoi je suis obligé de faire ça???
        // Le pseudo initial est vide et est remplacer par le constructeur
        pseudoLabel = new Label("", game.skin, "uiLabelStyle");

        //Temps écoulé et temps restant
        timeElapsed = 0;
        remainingTime = 20;

        // Pour l'instant on touche pas à ça!!!!
//        Animatedhourglass = new AnimatedActor();
//        TextureRegion[] hourglassFrames = new TextureRegion[118];
//        for (int m = 1; m < 119; m++) {
//            String hourglassFileName = "GameUIAssets/hourglassAssets/frame(" + m +").gif";
//            Texture hourglassTex = new Texture(Gdx.files.internal(hourglassFileName));
//            hourglassTex.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
//            hourglassFrames[m-1] = new TextureRegion(hourglassTex);
//        }
//        Array<TextureRegion> hourglassFramesArray = new Array<TextureRegion>(hourglassFrames);
//        anim = new Animation(0.1f, hourglassFramesArray, Animation.PlayMode.LOOP);
//        Animatedhourglass.setAnimation(anim);
//        Animatedhourglass.setOrigin(hourglass.getWidth() / 2, hourglass.getHeight() / 2);
//        Animatedhourglass.setPosition(200, 600);
//        mainStage.addActor(Animatedhourglass);

        // Temps restant
        //BitmapFont font = new BitmapFont();
        String text = "Time: ";
        //LabelStyle style = new LabelStyle(font, Color.NAVY);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(1.5f);
        timeLabel.setPosition(960, 1000);
        uiStage.addActor(timeLabel);

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/Gaur_Plain.ogg"));
        instrumental.setLooping(true);
        instrumental.play();

        Texture pauseTexture = new Texture(Gdx.files.internal("GameUIAssets/barsHorizontal.png"));
        game.skin.add("pauseImage", pauseTexture );
        Button.ButtonStyle pauseStyle = new Button.ButtonStyle();
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

        // Pause overlay
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

        // Overlay
        uiTable.pad(10);
        uiTable.add(pseudoLabel);
        uiTable.add(currentAvatar).padLeft(50);
        uiTable.add(pauseButton).expandX();
        uiTable.row();
        uiTable.add().colspan(3).expandY();

    }

    public void load() {
        for (Tile tile : tileList) {
            tile.load();
        }
        gameInterface = new GameInterface(game);
        gameInterface.hasBackground = false;
        queue.load();
        queue.setCoordinates(1920-tileSize/2-20, 20);
        // A gere pour pouvoir le faire sans avoir load
    }

    @Override
    public void update(float dt) {
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // Couleur d'arrière plan, et on clear tout
        batch.setProjectionMatrix(uiStage.getCamera().combined);
        batch.begin();
        background.draw(batch, 1);
        batch.end();

        batch.setProjectionMatrix(camera.combined);
        // On change le système de coordonées
        mainStage.draw();
        gameInterface.render(delta);

        batch.begin();
        coordMouse.setText(stringMousePosition(camera) + "\n" + stringMousePosition((OrthographicCamera) uiStage.getCamera())); // On écrit les coordonées
        queue.handleInput();

        // Pourquoi je dois mettre ça dans le render pour que ça marche et pas dans update? (non pas que ça me pose problème mais c'est chelou)
        if (!win)
        {
            timeElapsed += delta;
            timeLabel.setText( "Time: " + (int)remainingTime );
            remainingTime -= delta;

            // Check if timer reached 0
            if (remainingTime < 0) {
                dispose();
                game.setScreen(new DefeatScreen(game));
            }
        }

//        if (pawnTime) {
//            greenPawn.draw();
//            greenPawn.handleInput(player);
//        } else {
//            tempTile = getTile();
//            if (tempTile != null) tempTile.handleInput(player, numberCase);
//            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
//                pawnTime = true;
//                greenPawn = new Pawn("green");
//                greenPawn.setCase = tileList.get(0).caseList[0][0];
//                greenPawn.load();
//            }
//        }
        // Gestion du déplacement de la caméra
        updateCamera();
        batch.end();

    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Tile tile : tileList) {
            tile.dispose();
        }
        //greenPawn.dispose();
        instrumental.dispose();
    }
}
