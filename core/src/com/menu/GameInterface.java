package com.menu;

//ça c'est réellement l'interface que j'ai fait

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.utils.GameScreens;

import static com.utils.MainConstants.camera;
import static com.utils.MainConstants.getFontSize;
import static com.utils.TileAndCases.queue;
import static com.utils.TileAndCases.tileSize;

public class GameInterface extends BaseScreen {
    // pour indiquer la phase
    private char phase;

    private BaseActor phaseA;
    private BaseActor phaseB;
    private BaseActor log;
    private BaseActor restart;
    private BaseActor volume;
    private BaseActor cross;
    private BaseActor zoomPlus;
    private BaseActor zoomMoins;
    private BaseActor[] avatars;
    private BaseActor hourglass;

    private BaseActor up;
    private BaseActor left;
    private BaseActor portal;
    private BaseActor down;
    private BaseActor magnifier;
    private BaseActor right;
    private BaseActor elevator;

    //compteur de plaques restantes à placer
    private Label textTilesLeft;
    private int plaquesrestantes;

    boolean isPhaseA;
    boolean voiceOn;

    //Les pseudos, du coup faura faire en sorte que ce soit les bons
    private Label[] pseudoLabelList;
    private String[] pseudoList;

    //Listeners
    private InputListener pingavatar1;
    private InputListener pingavatar2;
    private InputListener pingavatar3;
    private InputListener pingavatar0;


    //constructeur
    public GameInterface(MagicGame g) {
        super(g);
        phase = 'a';
    }

    @Override
    public void create() {
        uiStage = GameScreens.mainScreen.getUiStage();

        //il faut créer tous les éléments de l'interface

        //d'abord les 2 indicateurs de phase en haut à droite qu'on a scanné (un seul est visible à la fois)
        phaseA = new BaseActor();
        phaseA.setTexture(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setSize(3*60+45,150);
        phaseA.setPosition(viewWidth - phaseA.getWidth(), viewHeight - phaseA.getHeight());
        isPhaseA = true;
        uiStage.addActor(phaseA);

        phaseB = new BaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("interface/phaseB.jpg")));
        phaseB.setSize(phaseA.getWidth(),phaseA.getHeight());
        phaseB.setPosition(viewWidth - phaseB.getWidth(), viewHeight - phaseB.getHeight());
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        //le bouton restart que j'ai pris sur internet, va falloir en refaire un
        restart = new BaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setSize(150,50);
        restart.setPosition(0, 0);
        uiStage.addActor(restart);

        //le log c'est juste un rectangle transparent ^^'
        log = new BaseActor();
        log.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        log.setSize(150,300);
        log.setPosition(0, restart.getHeight());
        uiStage.addActor(log);

        //la c'est juste des faux bouton
        hourglass = new BaseActor();
        hourglass.setTexture(new Texture(Gdx.files.internal("interface/sablier.jpg")));
        hourglass.setSize(60,45);
        hourglass.setPosition(viewWidth - hourglass.getWidth(), viewHeight - phaseA.getHeight() - hourglass.getHeight());
        uiStage.addActor(hourglass);

        zoomMoins = new BaseActor();
        zoomMoins.setTexture(new Texture(Gdx.files.internal("interface/zoommoins.jpg")));
        zoomMoins.setSize(60,45);
        zoomMoins.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth(), viewHeight - phaseA.getHeight() - zoomMoins.getHeight());
        uiStage.addActor(zoomMoins);

        zoomPlus = new BaseActor();
        zoomPlus.setTexture(new Texture(Gdx.files.internal("interface/zoomplus.jpg")));
        zoomPlus.setSize(60,45);
        zoomPlus.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth(), viewHeight - phaseA.getHeight() - zoomPlus.getHeight());
        uiStage.addActor(zoomPlus);

        volume = new BaseActor();
        volume.setTexture(new Texture(Gdx.files.internal("interface/haut-parleur.png")));
        volume.setSize(45,45);
        volume.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        voiceOn = true;
        uiStage.addActor(volume);

        cross = new BaseActor();
        cross.setTexture(new Texture(Gdx.files.internal("interface/croix.png")));
        cross.setSize(45,45);
        cross.setPosition(viewWidth - hourglass.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - volume.getWidth(), viewHeight - phaseA.getHeight() - volume.getHeight());
        cross.setVisible(false);
        uiStage.addActor(cross);

        //la c'est les avatars des joueurs, faudra changer les sprites, c'est par joueur avec en dessous l'indication de son pouvoir
        avatars = new BaseActor[4];
        for (int i = 0; i <=3; i++) {
            avatars[i] = new BaseActor();
            avatars[i].setTexture(new Texture(Gdx.files.internal("interface/kuro"+i+".png")));
            avatars[i].setSize(90,90);
            avatars[i].setPosition(viewWidth-avatars[i].getWidth() - 45, viewHeight-avatars[i].getHeight() - 225 - 150*i);
            uiStage.addActor(avatars[i]);
        }

        // Les pouvoirs (il doit y avoir un moyen de faire ça plus propre)
        up = new BaseActor();
        up.setTexture(new Texture(Gdx.files.internal("interface/flechehaut.png")));
        up.setPosition(viewWidth - avatars[0].getWidth() - 50, viewHeight - avatars[0].getHeight() - 219);
        uiStage.addActor(up);

        left = new BaseActor();
        left.setTexture(new Texture(Gdx.files.internal("interface/gauche.png")));
        left.setPosition(avatars[1].getX() - 9, avatars[1].getY() + 30);
        uiStage.addActor(left);

        portal = new BaseActor();
        portal.setTexture(new Texture(Gdx.files.internal("interface/teleport.png")));
        portal.setPosition(avatars[1].getX() - 12, avatars[1].getY());
        uiStage.addActor(portal);

        down = new BaseActor();
        down.setTexture(new Texture(Gdx.files.internal("interface/bas.png")));
        down.setPosition(avatars[2].getX() - 9, avatars[2].getY() + 30);
        uiStage.addActor(down);

        magnifier = new BaseActor();
        magnifier.setTexture(new Texture(Gdx.files.internal("interface/loupa.png")));
        magnifier.setPosition(avatars[2].getX() - 12, avatars[2].getY());
        uiStage.addActor(magnifier);

        right = new BaseActor();
        right.setTexture(new Texture(Gdx.files.internal("interface/droite.png")));
        right.setPosition(avatars[3].getX() - 9, avatars[3].getY() + 30);
        uiStage.addActor(right);

        elevator = new BaseActor();
        elevator.setTexture(new Texture(Gdx.files.internal("interface/escalator.png")));
        elevator.setPosition(avatars[3].getX() - 12, avatars[3].getY());
        uiStage.addActor(elevator);

        //affichage des pseudos des joueurs
        BitmapFont pseudoFont = getFontSize(18*2);
        LabelStyle pseudoStyle = new LabelStyle(pseudoFont, Color.WHITE);
        pseudoLabelList = new Label[4];
        pseudoList = new String[]{"Joueur 1","Joueur 2","Joueur 3","Joueur 4"};

        for (int i = 0; i <=3; i ++) {
            pseudoLabelList[i] = new Label(pseudoList[i], pseudoStyle);
            pseudoLabelList[i].setFontScale(0.5f);
            pseudoLabelList[i].setPosition(avatars[i].getX(), avatars[i].getY()-30);
            uiStage.addActor(pseudoLabelList[i]);
        }

        //la c'est le compteur de plaques restantes en bas a droite

        BitmapFont font = getFontSize(48*2);
        LabelStyle style = new LabelStyle(font, Color.BLACK);

        textTilesLeft = new Label(queue.textTileLeft, style);
        textTilesLeft.setFontScale(0.5f);
        textTilesLeft.setPosition(viewWidth - tileSize/2 - textTilesLeft.getWidth()/2 - 40, 0);
        uiStage.addActor(textTilesLeft);


        //je vais tenter de mettre les listeners ici héééééé ça maaaaaarche !!! (et j'ai trouvé toute seule ^^) par contre ça bloque en rouge si on spam trop hmmmmm
        final Action ping = Actions.sequence(
                Actions.color(new Color(1,0,0,1),(float)0.20),
                Actions.color(new Color(1,1,1,1),(float)0.20)
        );

        pingavatar0 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatars[0].clear();
                avatars[0].addAction(ping);
                return false;
            }
        };

        pingavatar1 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatars[1].clear();
                avatars[1].addAction(ping);
                return false;
            }
        };

        pingavatar2 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatars[2].clear();
                avatars[2].addAction(ping);
                return false;
            }
        };
        pingavatar3 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatars[3].clear();
                avatars[3].addAction(ping);
                return false;
            }
        };

        for (Actor actor: uiStage.getActors()) {
            //actor.scaleBy(.5f);
            // Parce qu'on est plus en 1280 mais en 1920 oupsy doopsy déso chloé
        }

    }



    public void update(float dt) {
        textTilesLeft.setText(queue.textTileLeft);
    }

    //la c'est les changements/animations de l'interface, faudra juste changer les conditions
    @Override
    public boolean keyDown(int keycode){
        if (keycode == Input.Keys.P) {
            if (isPhaseA) {
                phaseA.setVisible(false);
                phaseB.setVisible(true);
                isPhaseA = false;
                return true;
            }
            else {
                phaseB.setVisible(false);
                phaseA.setVisible(true);
                isPhaseA = true;
                return true;
            }
        }
        if (keycode == Input.Keys.M) {
            if (voiceOn) {
                cross.setVisible(true);
                voiceOn = false;
                return true;
            }
            else {
                cross.setVisible(false);
                voiceOn = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        //appuyer sur le bouton restart fait apparaitre sur les interfaces de chaque joueur que ce joueur veux recommencer
        restart.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        BaseActor wantToRestart = new BaseActor();
                        wantToRestart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
                        wantToRestart.setPosition(avatars[0].getX() - 15, avatars[0].getY() + avatars[0].getHeight()*1.5f / 2);
                        uiStage.addActor(wantToRestart);
                        return true;
                    }
                }
        );
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        System.out.println("Scrolled by " + amountX + " and " + amountY);
        camera.zoom = (float) Math.max(0.4,Math.min(5,camera.zoom +amountY * 0.2));
        camera.update();
        return false;
    }
}

