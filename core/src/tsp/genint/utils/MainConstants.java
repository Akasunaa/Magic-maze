package tsp.genint.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;


public class MainConstants {
    public static OrthographicCamera camera;
    public static SpriteBatch batch;

    final static FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UserInterface/arial.ttf"));
    final static FreeTypeFontParameter parameter = new FreeTypeFontParameter();

    public static BitmapFont getFontSize(int size) {
        parameter.size = size;
        return generator.generateFont(parameter);
    }
}
