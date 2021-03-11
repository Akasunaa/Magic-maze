package com.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class MainConstants {
    public static OrthographicCamera camera;
    public static SpriteBatch batch;

    static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("GameUIAssets/arial.ttf"));
    static FreeTypeFontParameter parameter = new FreeTypeFontParameter();
    public static BitmapFont getFontSize(int size) {
        parameter.size = size;
        return generator.generateFont(parameter);
    }
    // This causes a memory leak but it's alright
    // Plus sérieusement c'est super important qu'on abandonne les BitMap de LibGDX
    // parce qu'elles sont toutes moches
    // Et pixellisées là beurk beurk beurk
}
