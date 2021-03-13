package com.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.tiles.Case;
import com.tiles.Tile;

import static com.utils.MainConstants.camera;
import static com.utils.TileAndCases.tileList;

public class Functions {

    public static int modulo(int a, int b) {
        return (a % b + b) % b;
    }

    public static Vector2 mouseInput() {
        Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Vector2(temp.x, temp.y);
    }

    public static Vector2 mouseInput(OrthographicCamera camera) {
        Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Vector2(temp.x, temp.y);
    }

    public static void snap(Vector2 mousePosition) {
        // Beaucoup de debug ici
        // C'est essentiellement un changement de base, un arrondissement à l'entier, puis on remet la bonne base
        //System.out.println(mousePosition);
        mousePosition.sub(TileAndCases.origin);
        //System.out.println(mousePosition);
        mousePosition.mul(TileAndCases.newBaseInvert);
        //System.out.println(mousePosition);
        mousePosition.x = Math.round(mousePosition.x);
        mousePosition.y = Math.round(mousePosition.y);
        //System.out.println(mousePosition);
        mousePosition.mul(TileAndCases.newBase);
        //System.out.println(mousePosition);
        mousePosition.add(TileAndCases.origin);
        //System.out.println(mousePosition);
    }

    public static Tile getTile() {
        for (Tile tile : TileAndCases.tileList) {
            if ((tile.x < mouseInput().x) && (mouseInput().x < tile.x + TileAndCases.tileSize) && (tile.y < mouseInput().y) && (mouseInput().y < tile.y + TileAndCases.tileSize)) {
                return tile;
            }
        }
        return null;
    }

    public static Tile getTile(Vector2 mousePosition) {
        for (Tile tile : TileAndCases.tileList) {
            if ((tile.x < mousePosition.x) && (mousePosition.x < tile.x + TileAndCases.tileSize) && (tile.y < mousePosition.y) && (mousePosition.y < tile.y + TileAndCases.tileSize)) {
                return tile;
            }
        }
        return null;
    }

    static float step = 150f;
    static Vector3 target;
    static Vector3 middleLastClick;


    public static void updateCamera() {
        target = new Vector3(camera.position);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.MIDDLE)) {
            middleLastClick = new Vector3(1920-Gdx.input.getX(), Gdx.input.getY(), 0f);
            System.out.println(middleLastClick);
            }
        else if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
            target.add(new Vector3(1920-Gdx.input.getX(), Gdx.input.getY(), 0f).sub(middleLastClick).scl(0.5f));
        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.Z)) target.add(0f, step * camera.zoom, 0f);
            if (Gdx.input.isKeyPressed(Input.Keys.Q)) target.add(-step * camera.zoom, 0f, 0f);
            if (Gdx.input.isKeyPressed(Input.Keys.S)) target.add(0f, -step * camera.zoom, 0f);
            if (Gdx.input.isKeyPressed(Input.Keys.D)) target.add(step * camera.zoom, 0f, 0f);
        }
        camera.position.interpolate(target, 0.3f, Interpolation.bounce);
        camera.update();
    }
    public static Case findCase() {
        for (Tile tile : tileList) {
            if (tile.getX() <= Functions.mouseInput().x && Functions.mouseInput().x <= tile.getX() + tile.getSize() &&
                    tile.getY() <= Functions.mouseInput().y && Functions.mouseInput().y <= tile.getY() + tile.getSize()) {
                try {
                    return tile.getCase(Functions.mouseInput());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Clicked border of Tile n°" + tile.number + " in Pawn.findCase");
                }
            }
        }
        return null;
    }
}
