package com.mygdx.game;

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

public class MMinterface extends IBaseScreen {
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

    //constructeur
    public MMinterface(Game g) {
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
        avatar1.setPosition(viewWidth - avatar1.getWidth() - 10, viewHeight - avatar1.getHeight() - 164);
        uiStage.addActor(avatar1);

        haut = new IBaseActor();
        haut.setTexture(new Texture(Gdx.files.internal("interface/flechehaut.png")));
        haut.setPosition(viewWidth - avatar1.getWidth() - 13, viewHeight - avatar1.getHeight() - 160);
        uiStage.addActor(haut);

        avatar2 = new IBaseActor();
        avatar2.setTexture(new Texture(Gdx.files.internal("interface/kuro2.png")));
        avatar2.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 20, viewHeight - avatar2.getHeight() - 164);
        uiStage.addActor(avatar2);

        gauche = new IBaseActor();
        gauche.setTexture(new Texture(Gdx.files.internal("interface/gauche.png")));
        gauche.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25, viewHeight - avatar2.getHeight() - 150);
        uiStage.addActor(gauche);

        teleportation = new IBaseActor();
        teleportation.setTexture(new Texture(Gdx.files.internal("interface/teleport.png")));
        teleportation.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25, viewHeight - avatar2.getHeight() - 170);
        uiStage.addActor(teleportation);

        avatar3 = new IBaseActor();
        avatar3.setTexture(new Texture(Gdx.files.internal("interface/kuro3.png")));
        avatar3.setPosition(viewWidth - avatar1.getWidth() - 10, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 194);
        uiStage.addActor(avatar3);

        bas = new IBaseActor();
        bas.setTexture(new Texture(Gdx.files.internal("interface/bas.png")));
        bas.setPosition(viewWidth - avatar1.getWidth() - 15, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 180);
        uiStage.addActor(bas);

        loupe = new IBaseActor();
        loupe.setTexture(new Texture(Gdx.files.internal("interface/loupa.png")));
        loupe.setPosition(viewWidth - avatar1.getWidth() - 15, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 200);
        uiStage.addActor(loupe);

        avatar4 = new IBaseActor();
        avatar4.setTexture(new Texture(Gdx.files.internal("interface/kuro4.png")));
        avatar4.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 20, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 194);
        uiStage.addActor(avatar4);

        droite = new IBaseActor();
        droite.setTexture(new Texture(Gdx.files.internal("interface/droite.png")));
        droite.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 180);
        uiStage.addActor(droite);

        escalator = new IBaseActor();
        escalator.setTexture(new Texture(Gdx.files.internal("interface/escalator.png")));
        escalator.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25, viewHeight - avatar1.getHeight() - avatar3.getHeight() - 195);
        uiStage.addActor(escalator);

        //la c'est le compteur de plaques restantes en bas a droite
        BitmapFont font = new BitmapFont();
        LabelStyle style = new LabelStyle(font, Color.VIOLET);

        plaquesrestantes = 10;
        plaquesRestantesLabel = new Label("Plaques restantes: 10", style);
        plaquesRestantesLabel.setFontScale(1);
        plaquesRestantesLabel.setPosition(viewWidth - plaquesRestantesLabel.getWidth() - 5, 0);
        uiStage.addActor(plaquesRestantesLabel);

        prochainePlaque = new IBaseActor();
        prochainePlaque.setTexture(new Texture(Gdx.files.internal("interface/plaque3.jpg")));
        prochainePlaque.setPosition(viewWidth - prochainePlaque.getWidth() - 25, plaquesRestantesLabel.getHeight() + 10);
        uiStage.addActor(prochainePlaque);
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
        avatar1.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatar1.clear();
                        avatar1.addAction(ping);
                        return true;
                    }
                }
        );

        avatar2.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatar2.clear();
                        avatar2.addAction(ping);
                        return true;
                    }
                }
        );

        avatar3.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatar3.clear();
                        avatar3.addAction(ping);
                        return true;
                    }
                }
        );

        avatar4.addListener(
                new InputListener()
                {
                    public boolean touchDown(InputEvent ev, float x, float y, int pointer, int button) {
                        avatar4.clear();
                        avatar4.addAction(ping);
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

