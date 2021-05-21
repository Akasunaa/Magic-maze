package com.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.screens.menu.MainMenu;

import static com.utils.Functions.quit;

public class MagicGame extends Game {
    public Skin skin;
    public float audioVolume = 0.70f;
    public void create() {
        // initialize resources common to multiple screens and store to skin database
        skin = new Skin(Gdx.files.internal("UserInterface/uiskin.json"));

        // Police d'écriture
        BitmapFont uiFont = skin.getFont("default-font");
        LabelStyle uiLabelStyle = new LabelStyle(uiFont, Color.BLUE);
        skin.add("uiLabelStyle", uiLabelStyle);

        //Texture bouton de base
        TextButtonStyle uiTextButtonStyle = new TextButtonStyle();
        uiTextButtonStyle.font = uiFont;
        uiTextButtonStyle.fontColor = Color.NAVY;
        Texture upTex = new Texture(Gdx.files.internal("UserInterface/Button/Idle.png"));
        skin.add("buttonUp", new NinePatch(upTex, 26,26,16,20));
        uiTextButtonStyle.up = skin.getDrawable("buttonUp");

        //Texture bouton quand survolé
        Texture overTex = new Texture(Gdx.files.internal("UserInterface/Button/Clicked.png"));
        skin.add("buttonOver", new NinePatch(overTex, 26,26,16,20) );
        uiTextButtonStyle.over = skin.getDrawable("buttonOver");
        uiTextButtonStyle.overFontColor = Color.BLUE;

        //Texture bouton quand appuyé
        Texture downTex = new Texture(Gdx.files.internal("UserInterface/Button/Released.png"));
        skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20) );
        uiTextButtonStyle.down = skin.getDrawable("buttonDown");
        uiTextButtonStyle.downFontColor = Color.BLUE;
        skin.add("uiTextButtonStyle", uiTextButtonStyle);

        // Texture du slider audio
        SliderStyle uiSliderStyle = new SliderStyle();
        skin.add("sliderBack", new Texture(Gdx.files.internal("UserInterface/SoundSlider/background.png")) );
        skin.add("sliderKnob", new Texture(Gdx.files.internal("UserInterface/SoundSlider/cursor.png")) );
        skin.add("sliderBefore", new Texture(Gdx.files.internal("UserInterface/SoundSlider/completionBar.png")) );
        skin.add("sliderAfter", new Texture(Gdx.files.internal("UserInterface/SoundSlider/background.png")) );
        uiSliderStyle.background = skin.getDrawable("sliderBack");
        uiSliderStyle.knob = skin.getDrawable("sliderKnob");
        uiSliderStyle.knobAfter = skin.getDrawable("sliderAfter");
        uiSliderStyle.knobBefore = skin.getDrawable("sliderBefore");
        skin.add("uiSliderStyle", uiSliderStyle);

        TextField.TextFieldStyle logStyle = new TextField.TextFieldStyle();
        Texture logBackground = new Texture(Gdx.files.internal("UserInterface/logBackground.png"));
        skin.add("logBackground", new NinePatch(logBackground, 10,10,10,10));
        logStyle.background = skin.getDrawable("logBackground");
        logStyle.font = skin.getFont("default-font-small");
        logStyle.fontColor = skin.getColor("white");
        skin.add("logStyle", logStyle);

        BitmapFont clockFont = skin.getFont("default-font-huge");
        LabelStyle clockStyle = new LabelStyle(clockFont, Color.NAVY);
        skin.add("clockStyle", clockStyle);

        BitmapFont tilesLeftFont = skin.getFont("default-font-big");
        LabelStyle tilesLeftStyle = new LabelStyle(tilesLeftFont, Color.NAVY);
        skin.add("tilesLeftStyle", tilesLeftStyle);

        //Toujours à la fin
        MainMenu cm = new MainMenu(this);
        GameScreens.game = this;
        setScreen( cm );
    }
    public void dispose() {
        skin.dispose();
        screen.dispose();
        quit();
    }
    @Override
    public void setScreen (Screen screen) {
        if (this.screen != null) {
            this.screen.dispose();
            this.screen.hide();
        }
        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }
}