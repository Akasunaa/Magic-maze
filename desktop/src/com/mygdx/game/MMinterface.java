package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
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
    private IBaseActor hautparleur;
    private IBaseActor zoomplus;
    private IBaseActor zoommoins;
    private IBaseActor avatar1;
    private IBaseActor avatar2;
    private IBaseActor avatar3;
    private IBaseActor avatar4;
    private IBaseActor prochaineplaque;
    private IBaseActor sablier;

    private IBaseActor haut;
    private IBaseActor gauche;
    private IBaseActor teleportation;
    private IBaseActor bas;
    private IBaseActor loupe;
    private IBaseActor droite;
    private IBaseActor escalator;

    //compteur de plaques restantes à placer
    private Label plaquesrestantesLabel;
    private int plaquesrestantes;


    public MMinterface(Game g){
        super(g);
        phase = 'a';
    }

    @Override
    public void create() {
        phaseA = new IBaseActor();
        phaseA.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\phaseA.jpg")));
        //phaseA.setOrigin(phaseA.getWidth(), phaseA.getHeight());
        phaseA.setPosition(viewWidth - phaseA.getWidth(),viewHeight - phaseA.getHeight());
        uiStage.addActor(phaseA);

        phaseB = new IBaseActor();
        phaseB.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\phaseB.jpg")));
        phaseB.setOrigin(phaseB.getWidth(), phaseB.getHeight());
        phaseB.setPosition(viewWidth,viewHeight);
        phaseB.setVisible(false);
        uiStage.addActor(phaseB);
//osef
        restart = new IBaseActor();
        restart.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\restart-button.png")));
        restart.setPosition(0,0);
        //restart.setVisible(false);
        uiStage.addActor(restart);

        log = new IBaseActor();
        log.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\log.png")));
        log.setPosition(0,restart.getHeight());
        //log.setVisible(false);
        uiStage.addActor(log);

        sablier = new IBaseActor();
        sablier.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\sablier.jpg")));
        //sablier.setOrigin(sablier.getWidth(), sablier.getHeight());
        sablier.setPosition(viewWidth - sablier.getWidth(),viewHeight - phaseA.getHeight() - sablier.getHeight());
        //sablier.setVisible(false);
        uiStage.addActor(sablier);

        zoommoins = new IBaseActor();
        zoommoins.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\zoommoins.jpg")));
        //zoommoins.setOrigin(zoommoins.getWidth(), zoommoins.getHeight());
        zoommoins.setPosition(viewWidth - sablier.getWidth() - zoommoins.getWidth(),viewHeight - phaseA.getHeight() - zoommoins.getHeight());
        //zoommoins.setVisible(false);
        uiStage.addActor(zoommoins);

        zoomplus = new IBaseActor();
        zoomplus.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\zoomplus.jpg")));
        zoomplus.setOrigin(zoomplus.getWidth(), zoomplus.getHeight());
        zoomplus.setPosition(viewWidth - sablier.getWidth() - zoommoins.getWidth() - zoomplus.getWidth(),viewHeight - phaseA.getHeight() - zoomplus.getHeight());
        //zoomplus.setVisible(false);
        uiStage.addActor(zoomplus);


        hautparleur = new IBaseActor();
        hautparleur.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\haut-parleur.png")));
        hautparleur.setOrigin(hautparleur.getWidth(), hautparleur.getHeight());
        hautparleur.setPosition(viewWidth - sablier.getWidth() - zoommoins.getWidth() - zoomplus.getWidth() - hautparleur.getWidth(),viewHeight - phaseA.getHeight() - hautparleur.getHeight());
        //hautparleur.setVisible(false);
        uiStage.addActor(hautparleur);


        avatar1 = new IBaseActor();
        //avatar1.setVisible(false);
        avatar1.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\kuro1.png")));
        //avatar1.setOrigin(avatar1.getWidth()/2, avatar1.getHeight()/2);
        avatar1.setPosition(viewWidth - avatar1.getWidth() - 10,viewHeight - avatar1.getHeight() - 164);
        uiStage.addActor(avatar1);

        haut = new IBaseActor();
        haut.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\flechehaut.png")));
        haut.setPosition(viewWidth - avatar1.getWidth() - 13,viewHeight - avatar1.getHeight() - 160);
        uiStage.addActor(haut);

        avatar2 = new IBaseActor();
        //avatar2.setVisible(false);
        avatar2.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\kuro2.png")));
        //avatar2.setOrigin(avatar2.getWidth()/2, avatar2.getHeight()/2);
        avatar2.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 20,viewHeight - avatar2.getHeight() - 164);
        uiStage.addActor(avatar2);

        gauche = new IBaseActor();
        gauche.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\gauche.png")));
        gauche.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25,viewHeight - avatar2.getHeight() - 150 );
        uiStage.addActor(gauche);

        teleportation = new IBaseActor();
        teleportation.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\teleport.png")));
        teleportation.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25,viewHeight - avatar2.getHeight() - 170 );
        uiStage.addActor(teleportation);

        avatar3 = new IBaseActor();
        //avatar3.setVisible(false);
        avatar3.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\kuro3.png")));
        //avatar3.setOrigin(avatar3.getWidth()/2, avatar3.getHeight()/2);
        avatar3.setPosition(viewWidth - avatar1.getWidth() - 10,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 194);
        uiStage.addActor(avatar3);

        bas = new IBaseActor();
        bas.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\bas.png")));
        bas.setPosition(viewWidth - avatar1.getWidth() - 15,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 180);
        uiStage.addActor(bas);

        loupe = new IBaseActor();
        loupe.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\loupa.png")));
        loupe.setPosition(viewWidth - avatar1.getWidth() - 15,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 200);
        uiStage.addActor(loupe);

        avatar4 = new IBaseActor();
        //avatar4.setVisible(false);
        avatar4.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\kuro4.png")));
        //avatar4.setOrigin(avatar4.getWidth()/2, avatar4.getHeight()/2);
        avatar4.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 20,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 194);
        uiStage.addActor(avatar4);

        droite = new IBaseActor();
        droite.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\droite.png")));
        droite.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 180);
        uiStage.addActor(droite);

        escalator = new IBaseActor();
        escalator.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\escalator.png")));
        escalator.setPosition(viewWidth - avatar1.getWidth() - avatar2.getWidth() - 25,viewHeight - avatar1.getHeight() - avatar3.getHeight() - 195);
        uiStage.addActor(escalator);

        BitmapFont font = new BitmapFont();
        LabelStyle style = new LabelStyle(font, Color.VIOLET);

        plaquesrestantes = 10;
        plaquesrestantesLabel = new Label("Plaques restantes: 10", style);
        plaquesrestantesLabel.setFontScale(1);
        plaquesrestantesLabel.setOrigin(plaquesrestantesLabel.getWidth(),0);
        plaquesrestantesLabel.setPosition(viewWidth - plaquesrestantesLabel.getWidth() - 5,0);
        //plaquesrestantesLabel.setVisible(false);
        uiStage.addActor(plaquesrestantesLabel);

        prochaineplaque = new IBaseActor();
        prochaineplaque.setTexture(new Texture(Gdx.files.internal("C:\\Users\\user\\Documents\\Projet info\\images\\interface\\plaque3.jpg")));
        //prochaineplaque.setOrigin(prochaineplaque.getWidth(), 0 );
        prochaineplaque.setPosition(viewWidth - prochaineplaque.getWidth() - 25, plaquesrestantesLabel.getHeight() + 10);
        //prochaineplaque.setVisible(false);
        uiStage.addActor(prochaineplaque);
    }

    public void update(float dt){
    }


     @Override
     public boolean scrolled(float amountX, float amountY) {
         return false;
     }
 }

