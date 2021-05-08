package com.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.multiplayer.ServerNotReachedException;
import com.utils.GameScreens;

public class MagicGame extends Game {
    public Skin skin;
    public void create() {
        // initialize resources common to multiple screens and store to skin database
        skin = new Skin();
        // Police d'écriture
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("GameUIAssets/fontTest.fnt"));
        uiFont.getRegion().getTexture().setFilter(TextureFilter.Linear,
                TextureFilter.Linear);
        skin.add("uiFont", uiFont);
        LabelStyle uiLabelStyle = new LabelStyle(uiFont, Color.BLUE);
        skin.add("uiLabelStyle", uiLabelStyle);

        //Texture boutton de base
        TextButtonStyle uiTextButtonStyle = new TextButtonStyle();
        uiTextButtonStyle.font = uiFont;
        uiTextButtonStyle.fontColor = Color.NAVY;
        Texture upTex = new Texture(Gdx.files.internal("GameUIAssets/ninepatch-1.png"));
        skin.add("buttonUp", new NinePatch(upTex, 26,26,16,20));
        uiTextButtonStyle.up = skin.getDrawable("buttonUp");

        //Texture boutton quand survolé
        Texture overTex = new Texture(Gdx.files.internal("GameUIAssets/ninepatch-2.png"));
        skin.add("buttonOver", new NinePatch(overTex, 26,26,16,20) );
        uiTextButtonStyle.over = skin.getDrawable("buttonOver");
        uiTextButtonStyle.overFontColor = Color.BLUE;

        //Texture boutton quand appuyé
        Texture downTex = new Texture(Gdx.files.internal("GameUIAssets/ninepatch-3.png"));
        skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20) );
        uiTextButtonStyle.down = skin.getDrawable("buttonDown");
        uiTextButtonStyle.downFontColor = Color.BLUE;
        skin.add("uiTextButtonStyle", uiTextButtonStyle);

        // Texture du slider audio
        SliderStyle uiSliderStyle = new SliderStyle();
        skin.add("sliderBack", new Texture(Gdx.files.internal("GameUIAssets/grey_sliderHorizontal.png")) );
        skin.add("sliderKnob", new Texture(Gdx.files.internal("GameUIAssets/red_sliderDown.png")) );
        skin.add("sliderBefore", new Texture(Gdx.files.internal("GameUIAssets/red_sliderHorizontal.png")) );
        skin.add("sliderAfter", new Texture(Gdx.files.internal("GameUIAssets/grey_sliderHorizontal.png")) );
        uiSliderStyle.background = skin.getDrawable("sliderBack");
        uiSliderStyle.knob = skin.getDrawable("sliderKnob");
        uiSliderStyle.knobAfter = skin.getDrawable("sliderAfter");
        uiSliderStyle.knobBefore = skin.getDrawable("sliderBefore");
        skin.add("uiSliderStyle", uiSliderStyle);

        //Toujours à la fin
        MainMenu cm = new MainMenu(this);
        GameScreens.game = this;
        setScreen( cm );
    }
    public void dispose() {
        skin.dispose();
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