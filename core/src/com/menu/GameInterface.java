package com.menu;

//ça c'est réellement l'interface que j'ai fait

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.menu.BaseActor;
import com.menu.BaseScreen;
import com.menu.MagicGame;
import com.utils.GameScreens;

import static com.utils.MainConstants.camera;

public class GameInterface extends BaseScreen {
    // pour indiquer la phase
    private char phase;

    private BaseActor phaseA;
    private BaseActor phaseB;
    private BaseActor log;
    private BaseActor restart;
    private BaseActor hautParleur;
    private BaseActor croix;
    private BaseActor zoomPlus;
    private BaseActor zoomMoins;
    private BaseActor avatar1;
    private BaseActor avatar2;
    private BaseActor avatar3;
    private BaseActor avatar4;
    private BaseActor prochainePlaque;
    private BaseActor sablier;

    private BaseActor haut;
    private BaseActor gauche;
    private BaseActor teleportation;
    private BaseActor bas;
    private BaseActor loupe;
    private BaseActor droite;
    private BaseActor escalator;

    //compteur de plaques restantes à placer
    private Label plaquesRestantesLabel;
    private int plaquesrestantes;

    boolean bphaseA;
    boolean voiceOn;

    //Les pseudos, du coup faura faire en sorte que ce soit les bons
    private Label pseudo1Label;
    private Label pseudo2Label;
    private Label pseudo3Label;
    private Label pseudo4Label;
    private String pseudo1;
    private String pseudo2;
    private String pseudo3;
    private String pseudo4;

    //Listeners
    private InputListener pingavatar1;
    private InputListener pingavatar2;
    private InputListener pingavatar3;
    private InputListener pingavatar4;



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
        phaseA.setPosition(viewWidth - phaseA.getWidth()*1.5f, viewHeight - phaseA.getHeight()*1.5f);
        bphaseA = true;
        uiStage.addActor(phaseA);

        phaseB = new BaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("interface/phaseBre.jpg")));
        phaseB.setPosition(viewWidth - phaseB.getWidth()*1.5f, viewHeight - phaseB.getHeight()*1.5f);
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        //le bouton restart que j'ai pris sur internet, va falloir en refaire un
        restart = new BaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setPosition(0, 0);
        uiStage.addActor(restart);

        //le log c'est juste un rectangle transparent ^^'
        log = new BaseActor();
        log.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        log.setPosition(0, restart.getHeight());
        uiStage.addActor(log);

        //la c'est juste des faux bouton
        sablier = new BaseActor();
        sablier.setTexture(new Texture(Gdx.files.internal("interface/sablier.jpg")));
        sablier.setPosition(viewWidth - sablier.getWidth()*1.5f, viewHeight - phaseA.getHeight()*1.5f - sablier.getHeight()*1.5f);
        uiStage.addActor(sablier);

        zoomMoins = new BaseActor();
        zoomMoins.setTexture(new Texture(Gdx.files.internal("interface/zoommoins.jpg")));
        zoomMoins.setPosition(viewWidth - sablier.getWidth()*1.5f - zoomMoins.getWidth()*1.5f, viewHeight - phaseA.getHeight()*1.5f - zoomMoins.getHeight()*1.5f);
        uiStage.addActor(zoomMoins);

        zoomPlus = new BaseActor();
        zoomPlus.setTexture(new Texture(Gdx.files.internal("interface/zoomplus.jpg")));
        zoomPlus.setPosition(viewWidth - sablier.getWidth()*1.5f - zoomMoins.getWidth()*1.5f - zoomPlus.getWidth()*1.5f, viewHeight - phaseA.getHeight()*1.5f - zoomPlus.getHeight()*1.5f);
        uiStage.addActor(zoomPlus);

        hautParleur = new BaseActor();
        hautParleur.setTexture(new Texture(Gdx.files.internal("interface/haut-parleur.png")));
        hautParleur.setPosition(viewWidth + 1.5f*(- sablier.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - hautParleur.getWidth()), viewHeight +1.5f*(- phaseA.getHeight() - hautParleur.getHeight()));
        voiceOn = true;
        uiStage.addActor(hautParleur);

        croix = new BaseActor();
        croix.setTexture(new Texture(Gdx.files.internal("interface/croix.png")));
        croix.setPosition(viewWidth - sablier.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - hautParleur.getWidth(), viewHeight - phaseA.getHeight() - hautParleur.getHeight());
        croix.setVisible(false);
        uiStage.addActor(croix);

        //la c'est les avatars des joueurs, faudra changer les sprites, c'est par joueur avec en dessous l'indication de son pouvoir
        avatar1 = new BaseActor();
        avatar1.setTexture(new Texture(Gdx.files.internal("interface/kuro1.png")));
        avatar1.setPosition(viewWidth - avatar1.getWidth() - 45, viewHeight - avatar1.getHeight() - 225);
        uiStage.addActor(avatar1);

        haut = new BaseActor();
        haut.setTexture(new Texture(Gdx.files.internal("interface/flechehaut.png")));
        haut.setPosition(viewWidth - avatar1.getWidth() - 50, viewHeight - avatar1.getHeight() - 219);
        uiStage.addActor(haut);

        avatar2 = new BaseActor();
        avatar2.setTexture(new Texture(Gdx.files.internal("interface/kuro2.png")));
        avatar2.setPosition(avatar1.getX(), avatar1.getY() - 150);
        uiStage.addActor(avatar2);

        gauche = new BaseActor();
        gauche.setTexture(new Texture(Gdx.files.internal("interface/gauche.png")));
        gauche.setPosition(avatar2.getX() - 9, avatar2.getY() + 30);
        uiStage.addActor(gauche);

        teleportation = new BaseActor();
        teleportation.setTexture(new Texture(Gdx.files.internal("interface/teleport.png")));
        teleportation.setPosition(avatar2.getX() - 12, avatar2.getY());
        uiStage.addActor(teleportation);

        avatar3 = new BaseActor();
        avatar3.setTexture(new Texture(Gdx.files.internal("interface/kuro3.png")));
        avatar3.setPosition(avatar1.getX(), avatar2.getY() - 150);
        uiStage.addActor(avatar3);

        bas = new BaseActor();
        bas.setTexture(new Texture(Gdx.files.internal("interface/bas.png")));
        bas.setPosition(avatar3.getX() - 9, avatar3.getY() + 30);
        uiStage.addActor(bas);

        loupe = new BaseActor();
        loupe.setTexture(new Texture(Gdx.files.internal("interface/loupa.png")));
        loupe.setPosition(avatar3.getX() - 12, avatar3.getY());
        uiStage.addActor(loupe);

        avatar4 = new BaseActor();
        avatar4.setTexture(new Texture(Gdx.files.internal("interface/kuro4.png")));
        avatar4.setPosition(avatar1.getX(), avatar3.getY() - 150);
        uiStage.addActor(avatar4);

        droite = new BaseActor();
        droite.setTexture(new Texture(Gdx.files.internal("interface/droite.png")));
        droite.setPosition(avatar4.getX() - 9, avatar4.getY() + 30);
        uiStage.addActor(droite);

        escalator = new BaseActor();
        escalator.setTexture(new Texture(Gdx.files.internal("interface/escalator.png")));
        escalator.setPosition(avatar4.getX() - 12, avatar4.getY());
        uiStage.addActor(escalator);

        //affichage des pseudos des joueurs
        BitmapFont fontpseudo = new BitmapFont();
        LabelStyle stylepseudo = new LabelStyle(fontpseudo, Color.WHITE);
        pseudo1 = "Joueur 1";
        pseudo2 = "Joueur 2";
        pseudo3 = "Joueur 3";
        pseudo4 = "Joueur 4";

        pseudo1Label = new Label(pseudo1, stylepseudo);
        pseudo1Label.setFontScale(1.5f);
        pseudo1Label.setPosition(avatar1.getX(), avatar1.getY() - 27);
        uiStage.addActor(pseudo1Label);

        pseudo3Label = new Label(pseudo3, stylepseudo);
        pseudo3Label.setFontScale(1.5f);
        pseudo3Label.setPosition(avatar3.getX(), avatar3.getY() - 27);
        uiStage.addActor(pseudo3Label);

        pseudo4Label = new Label(pseudo4, stylepseudo);
        pseudo4Label.setFontScale(1.5f);
        pseudo4Label.setPosition(avatar4.getX(), avatar4.getY() - 27);
        uiStage.addActor(pseudo4Label);

        pseudo2Label = new Label(pseudo2, stylepseudo);
        pseudo2Label.setFontScale(1.5f);
        pseudo2Label.setPosition(avatar2.getX(), avatar2.getY() - 27);
        uiStage.addActor(pseudo2Label);

        //la c'est le compteur de plaques restantes en bas a droite
        BitmapFont font = new BitmapFont();
        LabelStyle style = new LabelStyle(font, Color.VIOLET);

        plaquesrestantes = 10;
        plaquesRestantesLabel = new Label("Plaques restantes: " + plaquesrestantes, style);
        plaquesRestantesLabel.setFontScale(1);
        plaquesRestantesLabel.setPosition(viewWidth - plaquesRestantesLabel.getWidth() - 8, 30);
        uiStage.addActor(plaquesRestantesLabel);

        prochainePlaque = new BaseActor();
        prochainePlaque.setTexture(new Texture(Gdx.files.internal("interface/plaque3.jpg")));
        prochainePlaque.setPosition(viewWidth - prochainePlaque.getWidth() - 38, plaquesRestantesLabel.getHeight() + 45);
        uiStage.addActor(prochainePlaque);

        //je vais tenter de mettre les listeners ici héééééé ça maaaaaarche !!! (et j'ai trouvé toute seule ^^) par contre ça bloque en rouge si on spam trop hmmmmm
        final Action ping = Actions.sequence(
                Actions.color(new Color(1,0,0,1),(float)0.20),
                Actions.color(new Color(1,1,1,1),(float)0.20)
        );

        pingavatar1 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar1.clear();
                avatar1.addAction(ping);
                return false;
            }
        };

        pingavatar2 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar2.clear();
                avatar2.addAction(ping);
                return false;
            }
        };

        pingavatar3 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar3.clear();
                avatar3.addAction(ping);
                return false;
            }
        };
        pingavatar4 = new InputListener(){
            public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                avatar4.clear();
                avatar4.addAction(ping);
                return false;
            }
        };

        for (Actor actor: uiStage.getActors()) {
            actor.scaleBy(.5f);
            // Parce qu'on est plus en 1280 mais en 1920 oupsy doopsy déso chloé
        }

    }



    public void update(float dt) {
    }

    //la c'est les changements/animations de l'interface, faudra juste changer les conditions
    @Override
    public boolean keyDown(int keycode){
        if (keycode == Input.Keys.A) {
            if (bphaseA) {
                phaseA.setVisible(false);
                phaseB.setVisible(true);
                bphaseA = false;
                return true;
            }
            if (bphaseA == false) {
                phaseB.setVisible(false);
                phaseA.setVisible(true);
                bphaseA = true;
                return true;
            }
        }
        if (keycode == Input.Keys.Z) {
            if (voiceOn) {
                croix.setVisible(true);
                voiceOn = false;
                return true;
            }
            if (voiceOn == false) {
                croix.setVisible(false);
                voiceOn = true;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        final Action ping = Actions.sequence(
                Actions.color(new Color(1,0,0,1),(float)0.20),
                Actions.color(new Color(1,1,1,1),(float)0.20)
        );

        avatar1.addListener(pingavatar1);
        avatar2.addListener(pingavatar2);
        avatar3.addListener(pingavatar3);
        avatar4.addListener(pingavatar4);

        //appuyer sur le bouton restart fait apparaitre sur les interfaces de chaque joueur que ce joueur veux recommencer
        restart.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        BaseActor wantToRestart = new BaseActor();
                        wantToRestart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
                        wantToRestart.setPosition(avatar1.getX() - 10, avatar1.getY() + avatar1.getHeight() / 2);
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
        if (camera.zoom >= 0.2 || amountY >= 0) camera.zoom += amountY * 0.1;
        camera.update();
        return false;
    }
}

