package com.menu;

//ça c'est réellement l'interface que j'ai fait

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.multiplayer.ServerNotReachedException;
import com.tiles.Pawn;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.utils.GameScreens;
import com.utils.MainConstants;
import com.utils.Multiplayer;

import static com.utils.Colors.currentColor;
import static com.utils.Colors.getColor;
import static com.utils.MainConstants.getFontSize;
import static com.utils.Multiplayer.courrier;
import static com.utils.TileAndCases.*;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;

public class GameInterface extends BaseScreen {
    private BaseActor[] avatars;

    //compteur de plaques restantes à placer
    private Label textTilesLeft;

    boolean isPhaseA;
    boolean voiceOn;

    TextButton loadPawnButton;


    // Pour le "menu" de "pause" (en vrai à voir si c'est nécessaire et comment c'est géré evec le multi
    private Table pauseOverlay;

    private boolean win;

    private Label[] pseudoLabels;

    float remainingTime;
    Label timeLabel;

    public static TextArea TF;

    //constructeur
    public GameInterface(MagicGame g, float audioVolume){
        super(g);

        instrumental.setVolume(audioVolume);
        audioSlider.setValue(audioVolume);
    }

    public void create() {
        uiStage = GameScreens.mainScreen.getUiStage();
        mainStage = GameScreens.mainScreen.getMainStage();

        //il faut créer tous les éléments de l'interface

        //d'abord les 2 indicateurs de phase en haut à droite qu'on a scanné (un seul est visible à la fois)
        // pour indiquer la phase
        BaseActor phaseA = new BaseActor();
        phaseA.setTexture(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setSize(3*60+45,150);
        phaseA.setPosition(viewWidth - phaseA.getWidth(), viewHeight - phaseA.getHeight());
        isPhaseA = true;
        uiStage.addActor(phaseA);

        BaseActor phaseB = new BaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("interface/phaseB.jpg")));
        phaseB.setSize(phaseA.getWidth(), phaseA.getHeight());
        phaseB.setPosition(viewWidth - phaseB.getWidth(), viewHeight - phaseB.getHeight());
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        //le bouton restart que j'ai pris sur internet, va falloir en refaire un
        BaseActor restart = new BaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setSize(150,50);
        restart.setPosition(0, 0);
        uiStage.addActor(restart);
        restart.addListener(
                new InputListener() {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        BaseActor wantToRestart = new BaseActor();
                        wantToRestart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
                        wantToRestart.setPosition(avatars[0].getX() - 15, avatars[0].getY() + avatars[0].getHeight()*1.5f / 2);
                        uiStage.addActor(wantToRestart);
                        return true;
                    }
                }
        );

        //le log c'est juste un rectangle transparent ^^'
        BaseActor log = new BaseActor();
        log.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        log.setSize(300,500);
        log.setPosition(0, restart.getHeight());
        uiStage.addActor(log);

        Skin uiSkin = new Skin(Gdx.files.internal("GameUIAssets/uiskin.json"));
        TF = new TextArea("", uiSkin);

        TF.setWidth(300);
        TF.setHeight(500);
        TF.setPosition(0,restart.getHeight());
        TF.setText("Debut de la partie!");
        uiStage.addActor(TF);

        //la c'est juste des faux bouton
        BaseActor hourglass = new BaseActor();
        hourglass.setTexture(new Texture(Gdx.files.internal("interface/sablier.jpg")));
        hourglass.setSize(60,45);
        hourglass.setPosition(viewWidth - hourglass.getWidth(), viewHeight - phaseA.getHeight() - hourglass.getHeight());
        uiStage.addActor(hourglass);

        BaseActor zoomMoins = new BaseActor();
        zoomMoins.setTexture(new Texture(Gdx.files.internal("interface/zoommoins.jpg")));
        zoomMoins.setSize(60,45);
        zoomMoins.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth(), viewHeight - phaseA.getHeight() - zoomMoins.getHeight());
        uiStage.addActor(zoomMoins);

        BaseActor zoomPlus = new BaseActor();
        zoomPlus.setTexture(new Texture(Gdx.files.internal("interface/zoomplus.jpg")));
        zoomPlus.setSize(60,45);
        zoomPlus.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth(), viewHeight - phaseA.getHeight() - zoomPlus.getHeight());
        uiStage.addActor(zoomPlus);

        BaseActor volume = new BaseActor();
        volume.setTexture(new Texture(Gdx.files.internal("interface/haut-parleur.png")));
        volume.setSize(45,45);
        volume.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        voiceOn = true;
        uiStage.addActor(volume);

        BaseActor cross = new BaseActor();
        cross.setTexture(new Texture(Gdx.files.internal("interface/croix.png")));
        cross.setSize(45,45);
        cross.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        cross.setVisible(false);
        uiStage.addActor(cross);

        //la c'est les avatars des joueurs, faudra changer les sprites, c'est par joueur avec en dessous l'indication de son pouvoir
        avatars = new BaseActor[4];

        // On créé cette liste ici pour mettre des placeholders qu'on remplace ensuite par des vrais noms.
        String[] pseudoList = new String[]{"Joueur 1", "Joueur 2", "Joueur 3", "Joueur 4"};

        for (int i = 0; i < Multiplayer.playerList.size(); i++) {
            avatars[i] = Multiplayer.playerList.get(i).avatar;
            avatars[i].setSize(90, 90);
            avatars[i].setPosition(viewWidth - avatars[i].getWidth() - 45, viewHeight - avatars[i].getHeight() - 225 - 135 * i);
            uiStage.addActor(avatars[i]);
            final int temp = i;
            avatars[i].addListener(new InputListener() {
                public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                    avatars[temp].addAction(Actions.sequence(
                            Actions.color(new Color(1,0,0,1),(float)0.20),
                            Actions.color(new Color(1,1,1,1),(float)0.20)));
                    courrier.sendMessage("ping " + Multiplayer.playerList.get(temp).pseudo);
                    // What
                    // the
                    // fuck
                    // LibGDX c'est cool jusqu'à ce que tu soit obligé de faire des trucs comme ça
                    // Pour l'expliquer simplement: plutôt que de créer l'action en final en dehors de toute ça,
                    // Il faut la créer nous même à chaque fois que l'inputListener est appellé
                    // Désolé d'avoir craché sur les InputListener de LibGDX, ils sont très bien.
                    return true;
                }
            });
            pseudoList[i] = Multiplayer.playerList.get(i).pseudo;
        }
        for (int i = Multiplayer.playerList.size(); i < 4; i++) {
            avatars[i] = new BaseActor(new Texture(Gdx.files.internal("interface/kuro" + i + ".png")));
            avatars[i].setSize(90, 90);
            avatars[i].setPosition(viewWidth - avatars[i].getWidth() - 45, viewHeight - avatars[i].getHeight() - 225 - 135 * i);
            uiStage.addActor(avatars[i]);
            final int temp = i;
            avatars[i].addListener(new InputListener() {
                public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                    avatars[temp].addAction(Actions.sequence(
                            Actions.color(new Color(1,0,0,1),(float)0.20),
                            Actions.color(new Color(1,1,1,1),(float)0.20)));
                    // What
                    // the
                    // fuck
                    // LibGDX c'est cool jusqu'à ce que tu soit obligé de faire des trucs comme ça
                    // Pour l'expliquer simplement: plutôt que de créer l'action en final en dehors de toute ça,
                    // Il faut la créer nous même à chaque fois que l'inputListener est appellé
                    // Désolé d'avoir craché sur les InputListener de LibGDX, ils sont très bien.
                    return true;
                }
            });
        }

        //affichage des pseudos des joueurs
        BitmapFont pseudoFont = getFontSize(18*2);
        LabelStyle pseudoStyle = new LabelStyle(pseudoFont, Color.WHITE);
        //Les pseudos en tant que label
        Label[] pseudoLabelList = new Label[4];
        for (int i = 0; i <=3; i ++) {
            pseudoLabelList[i] = new Label(pseudoList[i], pseudoStyle);
            pseudoLabelList[i].setFontScale(0.5f);
            pseudoLabelList[i].setPosition(avatars[i].getX(), avatars[i].getY()-30);
            uiStage.addActor(pseudoLabelList[i]);
        }

        // Les pouvoirs (il doit y avoir un moyen de faire ça plus propre)
        BaseActor up = new BaseActor();
        up.setTexture(new Texture(Gdx.files.internal("interface/flechehaut.png")));
        up.setPosition(viewWidth - avatars[0].getWidth() - 50, viewHeight - avatars[0].getHeight() - 219);
        uiStage.addActor(up);

        BaseActor left = new BaseActor();
        left.setTexture(new Texture(Gdx.files.internal("interface/gauche.png")));
        left.setPosition(avatars[1].getX() - 9, avatars[1].getY() + 30);
        uiStage.addActor(left);

        BaseActor portal = new BaseActor();
        portal.setTexture(new Texture(Gdx.files.internal("interface/teleport.png")));
        portal.setPosition(avatars[1].getX() - 12, avatars[1].getY());
        uiStage.addActor(portal);

        BaseActor down = new BaseActor();
        down.setTexture(new Texture(Gdx.files.internal("interface/bas.png")));
        down.setPosition(avatars[2].getX() - 9, avatars[2].getY() + 30);
        uiStage.addActor(down);

        BaseActor magnifier = new BaseActor();
        magnifier.setTexture(new Texture(Gdx.files.internal("interface/loupa.png")));
        magnifier.setPosition(avatars[2].getX() - 12, avatars[2].getY());
        uiStage.addActor(magnifier);

        BaseActor right = new BaseActor();
        right.setTexture(new Texture(Gdx.files.internal("interface/droite.png")));
        right.setPosition(avatars[3].getX() - 9, avatars[3].getY() + 30);
        uiStage.addActor(right);

        BaseActor elevator = new BaseActor();
        elevator.setTexture(new Texture(Gdx.files.internal("interface/escalator.png")));
        elevator.setPosition(avatars[3].getX() - 12, avatars[3].getY());
        uiStage.addActor(elevator);

        //la c'est le compteur de plaques restantes en bas a droite

        BitmapFont font = getFontSize(48*2);
        LabelStyle style = new LabelStyle(font, Color.BLACK);

        textTilesLeft = new Label(queue.textTileLeft, style);
        textTilesLeft.setFontScale(0.5f);
        textTilesLeft.setPosition(viewWidth - tileSize/2 - textTilesLeft.getWidth()/2 - 40, 0);
        uiStage.addActor(textTilesLeft);

//        for (Actor actor: uiStage.getActors()) {
//            actor.scaleBy(.5f);
//            // Parce qu'on est plus en 1280 mais en 1920 oupsy doopsy déso chloé
//        }

        loadPawnButton = new TextButton("Afficher le pion "+ getColor(currentColor), game.skin, "uiTextButtonStyle");
        loadPawnButton.addListener(new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
                if (!tileList.isEmpty()) {
                    pawnList.add(new Pawn(currentColor));
                    pawnList.get(currentColor).setFirstCase();
                    pawnList.get(currentColor).load();
                    currentColor ++;
                    loadPawnButton.setText("Afficher le pion "+ getColor(currentColor));
                    if (currentColor >= 4) {
                        loadPawnButton.remove();
                    }
                }

            }
        });
        uiStage.addActor(loadPawnButton);
        loadPawnButton.setPosition(10,1000);

        //Ici c'est le bordel rajouté par Nathan
        win = false;

        //Temps écoulé et temps restant
        remainingTime = 10000f;

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
        font = MainConstants.getFontSize(40);
        String text = "Time: ";
        style = new LabelStyle(font, Color.NAVY);
        timeLabel = new Label(text, style);
        timeLabel.setFontScale(1.5f);
        timeLabel.setPosition(960, 1000);
        uiStage.addActor(timeLabel);

        instrumental = Gdx.audio.newMusic(Gdx.files.internal("Music&Sound/damedane.mp3"));
        instrumental.setLooping(true);
        instrumental.play();

        Texture pauseTexture = new Texture(Gdx.files.internal("GameUIAssets/barsHorizontal.png"));
        game.skin.add("pauseImage", pauseTexture );
        Button.ButtonStyle pauseStyle = new Button.ButtonStyle();
        pauseStyle.up = game.skin.getDrawable("pauseImage");
        Button pauseButton = new Button( pauseStyle );
        pauseButton.addListener(
                new InputListener() {
                    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                        togglePaused();
                        pauseOverlay.setVisible( isPaused() );
                        return true;
                    }
                });
        pauseButton.setSize(pauseButton.getWidth()*2, pauseButton.getHeight()*2);
        pauseButton.setPosition(1920*4.6f/6, 1080-pauseButton.getHeight() - 10);
        uiStage.addActor(pauseButton);

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
                        pauseOverlay.toFront();
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
                        Multiplayer.courrier.killThread();
                        if (Multiplayer.isServer) Multiplayer.serverMaker.killThread();
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
//        uiTable.pad(10);
//        uiTable.add(pseudoLabel);
//        uiTable.add(currentAvatar).padLeft(50);
//        uiTable.add(pauseButton).expandX();
//        uiTable.row();
//        uiTable.add().colspan(3).expandY();


    }

    public void update(float dt) {
        textTilesLeft.setText(queue.textTileLeft);

        if (!win) {
            timeLabel.setText( "Time: " + (int)remainingTime );
            remainingTime -= dt/3;

            // Check if timer reached 0
            if (remainingTime < 0) {
                remainingTime = 10f;
                // Parce que comme ça au moins je suis sûr qu'il va pas me faire une saloperie
                game.setScreen(new DefeatScreen(game));
            }
        }
    }
}


