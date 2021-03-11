package com.tiles.pathfinding;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import static com.utils.Functions.mouseInput;




public class FindThePath extends Game {


    @Override
    public void create() {
        MainScreen mainScreen = new MainScreen();
        setScreen(mainScreen);
    }
}
