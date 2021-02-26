package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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


    public MMinterface(Game g) {
        super(g);
        phase = 'a';
    }

    @Override
    public void create() {

        phaseA = new IBaseActor();
        phaseA.setTexture(new Texture(Gdx.files.internal("interface/phaseA.jpg")));
        phaseA.setPosition(viewWidth - phaseA.getWidth(), viewHeight - phaseA.getHeight());
        uiStage.addActor(phaseA);

        phaseB = new IBaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("interface/phaseB.jpg")));
        phaseB.setPosition(viewWidth - phaseB.getWidth(), viewHeight - phaseB.getHeight());
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);

        restart = new IBaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("interface/restart-button.png")));
        restart.setPosition(0, 0);
        uiStage.addActor(restart);

        log = new IBaseActor();
        log.setTexture(new Texture(Gdx.files.internal("interface/log.png")));
        log.setPosition(0, restart.getHeight());
        uiStage.addActor(log);

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
        uiStage.addActor(hautParleur);


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

    public void update(float dt) {}

    public boolean keyDown(int keycode){
        if (keycode == Input.Keys.A){
            phaseA.setVisible(false);
            phaseB.setVisible(true);
        }
        return false;
    }


    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

