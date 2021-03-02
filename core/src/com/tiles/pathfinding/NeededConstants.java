package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;

public class NeededConstants {
    static final float tileSize = 200f;
    static Vector2 origin = new Vector2();
    static OrthographicCamera camera;
    static Vector2 mouseInput() {
        Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Vector2(temp.x,temp.y);
    }
    static ArrayList<Tile> tileList;
    static SpriteBatch batch;
}
