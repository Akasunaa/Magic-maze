package com.multiplayer.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NeededConstants {
    public static Courrier courrier;
    public static SpriteBatch batch;
    public static OrthographicCamera camera;
    static Vector2 mouseInput() {
        Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Vector2(temp.x,temp.y);
    }
    public static ButtonList buttonList;
    public static ClientList clientList;
    public static Decryptor key;
}
