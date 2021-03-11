package com.gameInterface;

//ça c'est réellement l'interface que j'ai fait

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;

public class GameInterface extends IBaseScreen {
    // pour indiquer la phase
    private char phase;

    private IBaseActor phaseA;
    private IBaseActor phaseB;
    private IBaseActor log;
    private IBaseActor restart;
    private IBaseActor hautParleur;
    private IBaseActor croix;
    private IBaseActor zoomPlus;
    private IBaseActor zoomMoins;
    private IBaseActor avatar1;
    private IBaseActor avatar2;
    private IBaseActor avatar3;
    private IBaseActor avatar4;
    private IBaseActor prochainePlaque;
    private IBaseActor sablier;

    private IBaseActor haut;
    private IBaseActor gauche;
    private IBaseActor teleportation;
    private IBaseActor bas;
    private IBaseActor loupe;
    private IBaseActor droite;
    private IBaseActor escalator;

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
    public GameInterface(Game g) {
        super(g);
        phase = 'a';
    }

    @Override
    public void create() {

        //il faut créer tous les éléments de l'interface

        //d'abord les 2 indicateurs de phase en haut à droite qu'on a scanné (un seul est visible à la fois)
        phaseA = new IBaseActor();
        phaseA.setTexture(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setPosition(viewWidth - phaseA.getWidth(), viewHeight - phaseA.getHeight());
        bphaseA = true;
        uiStage.addActor(phaseA);

        phaseB = new IBaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("interface/phaseBre.jpg")));
        phaseB.setPosition(viewWidth - phaseB.getWidth(), viewHeight - phaseB.getHeight());
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        //le bouton restart que j'ai pris sur internet, va falloir en refaire un
        restart = new IBaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setPosition(0, 0);
        uiStage.addActor(restart);

        //le log c'est juste un rectangle transparent ^^'
        log = new IBaseActor();
        log.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        log.setPosition(0, restart.getHeight());
        uiStage.addActor(log);

        //la c'est juste des faux bouton
        sablier = new IBaseActor();
        sablier.setTexture(new Texture(Gdx.files.internal("interface/sablier.jpg")));
        sablier.setPosition(viewWidth - sablier.getWidth(), viewHeight - phaseA.getHeight() - sablier.getHeight());
        uiStage.addActor(sablier);

        zoomMoins = new IBaseActor();
        zoomMoins.setTexture(new Texture(Gdx.files.internal("interface/zoommoins.jpg")));
        zoomMoins.setPosition(viewWidth - sablier.getWidth() - zoomMoins.getWidth(), viewHeight - phaseA.getHeight() - zoomMoins.getHeight());
        uiStage.addActor(zoomMoins);

        zoomPlus = new IBaseActor();
        zoomPlus.setTexture(new Texture(Gdx.files.internal("interface/zoomplus.jpg")));
        zoomPlus.setPosition(viewWidth - sablier.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth(), viewHeight - phaseA.getHeight() - zoomPlus.getHeight());
        uiStage.addActor(zoomPlus);

        hautParleur = new IBaseActor();
        hautParleur.setTexture(new Texture(Gdx.files.internal("interface/haut-parleur.png")));
        hautParleur.setPosition(viewWidth - sablier.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - hautParleur.getWidth(), viewHeight - phaseA.getHeight() - hautParleur.getHeight());
        voiceOn = true;
        uiStage.addActor(hautParleur);

        croix = new IBaseActor();
        croix.setTexture(new Texture(Gdx.files.internal("interface/croix.png")));
        croix.setPosition(viewWidth - sablier.getWidth() - zoomMoins.getWidth() - zoomPlus.getWidth() - hautParleur.getWidth(), viewHeight - phaseA.getHeight() - hautParleur.getHeight());
        croix.setVisible(false);
        uiStage.addActor(croix);

        //la c'est les avatars des joueurs, faudra changer les sprites, c'est par joueur avec en dessous l'indication de son pouvoir
        avatar1 = new IBaseActor();
        avatar1.setTexture(new Texture(Gdx.files.internal("interface/kuro1.png")));
        avatar1.setPosition(viewWidth - avatar1.getWidth() - 30, viewHeight - avatar1.getHeight() - 150);
        uiStage.addActor(avatar1);

        haut = new IBaseActor();
        haut.setTexture(new Texture(Gdx.files.internal("interface/flechehaut.png")));
        haut.setPosition(viewWidth - avatar1.getWidth() - 33, viewHeight - avatar1.getHeight() - 146);
        uiStage.addActor(haut);

        avatar2 = new IBaseActor();
        avatar2.setTexture(new Texture(Gdx.files.internal("interface/kuro2.png")));
        avatar2.setPosition(avatar1.getX(), avatar1.getY() - 100);
        uiStage.addActor(avatar2);

        gauche = new IBaseActor();
        gauche.setTexture(new Texture(Gdx.files.internal("interface/gauche.png")));
        gauche.setPosition(avatar2.getX() - 6, avatar2.getY() + 20);
        uiStage.addActor(gauche);

        teleportation = new IBaseActor();
        teleportation.setTexture(new Texture(Gdx.files.internal("interface/teleport.png")));
        teleportation.setPosition(avatar2.getX() - 8, avatar2.getY());
        uiStage.addActor(teleportation);

        avatar3 = new IBaseActor();
        avatar3.setTexture(new Texture(Gdx.files.internal("interface/kuro3.png")));
        avatar3.setPosition(avatar1.getX(), avatar2.getY() - 100);
        uiStage.addActor(avatar3);

        bas = new IBaseActor();
        bas.setTexture(new Texture(Gdx.files.internal("interface/bas.png")));
        bas.setPosition(avatar3.getX() - 6, avatar3.getY() + 20);
        uiStage.addActor(bas);

        loupe = new IBaseActor();
        loupe.setTexture(new Texture(Gdx.files.internal("interface/loupa.png")));
        loupe.setPosition(avatar3.getX() - 8, avatar3.getY());
        uiStage.addActor(loupe);

        avatar4 = new IBaseActor();
        avatar4.setTexture(new Texture(Gdx.files.internal("interface/kuro4.png")));
        avatar4.setPosition(avatar1.getX(), avatar3.getY() - 100);
        uiStage.addActor(avatar4);

        droite = new IBaseActor();
        droite.setTexture(new Texture(Gdx.files.internal("interface/droite.png")));
        droite.setPosition(avatar4.getX() - 6, avatar4.getY() + 20);
        uiStage.addActor(droite);

        escalator = new IBaseActor();
        escalator.setTexture(new Texture(Gdx.files.internal("interface/escalator.png")));
        escalator.setPosition(avatar4.getX() - 8, avatar4.getY());
        uiStage.addActor(escalator);

        //affichage des pseudos des joueurs
        BitmapFont fontpseudo = new BitmapFont();
        LabelStyle stylepseudo = new LabelStyle(fontpseudo, Color.WHITE);
        pseudo1 = "Joueur 1";
        pseudo2 = "Joueur 2";
        pseudo3 = "Joueur 3";
        pseudo4 = "Joueur 4";

        pseudo1Label = new Label(pseudo1, stylepseudo);
        pseudo1Label.setFontScale(1);
        pseudo1Label.setPosition(avatar1.getX(), avatar1.getY() - 18);
        uiStage.addActor(pseudo1Label);

        pseudo3Label = new Label(pseudo3, stylepseudo);
        pseudo3Label.setFontScale(1);
        pseudo3Label.setPosition(avatar3.getX(), avatar3.getY() - 18);
        uiStage.addActor(pseudo3Label);

        pseudo4Label = new Label(pseudo4, stylepseudo);
        pseudo4Label.setFontScale(1);
        pseudo4Label.setPosition(avatar4.getX(), avatar4.getY() - 18);
        uiStage.addActor(pseudo4Label);

        pseudo2Label = new Label(pseudo2, stylepseudo);
        pseudo2Label.setFontScale(1);
        pseudo2Label.setPosition(avatar2.getX(), avatar2.getY() - 18);
        uiStage.addActor(pseudo2Label);

        //la c'est le compteur de plaques restantes en bas a droite
        BitmapFont font = new BitmapFont();
        LabelStyle style = new LabelStyle(font, Color.VIOLET);

        plaquesrestantes = 10;
        plaquesRestantesLabel = new Label("Plaques restantes: " + plaquesrestantes, style);
        plaquesRestantesLabel.setFontScale(1);
        plaquesRestantesLabel.setPosition(viewWidth - plaquesRestantesLabel.getWidth() - 5, 20);
        uiStage.addActor(plaquesRestantesLabel);

        prochainePlaque = new IBaseActor();
        prochainePlaque.setTexture(new Texture(Gdx.files.internal("interface/plaque3.jpg")));
        prochainePlaque.setPosition(viewWidth - prochainePlaque.getWidth() - 25, plaquesRestantesLabel.getHeight() + 30);
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

    }



    public void update(float dt) {
    }

    //la c'est les changements/animations de l'interface, faudra juste changer les conditions
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
                        IBaseActor wantToRestart = new IBaseActor();
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
        return false;
    }
}

