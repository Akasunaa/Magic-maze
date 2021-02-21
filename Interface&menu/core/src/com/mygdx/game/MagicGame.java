package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;

import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;

public class MagicGame extends BaseGame
{
    public void create()
    {
        // initialize resources common to multiple screens and store to skin database
        BitmapFont uiFont = new BitmapFont(Gdx.files.internal("core/assets/GameUIAssets/fontTest.fnt"));
        uiFont.getRegion().getTexture().setFilter(TextureFilter.Linear,
                TextureFilter.Linear);
        skin.add("uiFont", uiFont);
        LabelStyle uiLabelStyle = new LabelStyle(uiFont, Color.BLUE);
        skin.add("uiLabelStyle", uiLabelStyle);

        TextButtonStyle uiTextButtonStyle = new TextButtonStyle();
        uiTextButtonStyle.font = uiFont;
        uiTextButtonStyle.fontColor = Color.NAVY;
        Texture upTex = new Texture(Gdx.files.internal("core/assets/GameUIAssets/ninepatch-1.png"));
        skin.add("buttonUp", new NinePatch(upTex, 26,26,16,20));
        uiTextButtonStyle.up = skin.getDrawable("buttonUp");

        Texture overTex = new Texture(Gdx.files.internal("core/assets/GameUIAssets/ninepatch-2.png"));
        skin.add("buttonOver", new NinePatch(overTex, 26,26,16,20) );
        uiTextButtonStyle.over = skin.getDrawable("buttonOver");
        uiTextButtonStyle.overFontColor = Color.BLUE;

        Texture downTex = new Texture(Gdx.files.internal("core/assets/GameUIAssets/ninepatch-3.png"));
        skin.add("buttonDown", new NinePatch(downTex, 26,26,16,20) );
        uiTextButtonStyle.down = skin.getDrawable("buttonDown");
        uiTextButtonStyle.downFontColor = Color.BLUE;
        skin.add("uiTextButtonStyle", uiTextButtonStyle);

        SliderStyle uiSliderStyle = new SliderStyle();
        skin.add("sliderBack", new Texture(Gdx.files.internal("core/assets/GameUIAssets/grey_sliderHorizontal.png")) );
        skin.add("sliderKnob", new Texture(Gdx.files.internal("core/assets/GameUIAssets/red_sliderDown.png")) );
        skin.add("sliderBefore", new Texture(Gdx.files.internal("core/assets/GameUIAssets/red_sliderHorizontal.png")) );
        skin.add("sliderAfter", new Texture(Gdx.files.internal("core/assets/GameUIAssets/grey_sliderHorizontal.png")) );
        uiSliderStyle.background = skin.getDrawable("sliderBack");
        uiSliderStyle.knob = skin.getDrawable("sliderKnob");
        uiSliderStyle.knobAfter = skin.getDrawable("sliderAfter");
        uiSliderStyle.knobBefore = skin.getDrawable("sliderBefore");
        skin.add("uiSliderStyle", uiSliderStyle);

        //Toujours à la fin
        MainMenu cm = new MainMenu(this);
        setScreen( cm );
    }
}