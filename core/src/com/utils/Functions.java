package com.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.multiplayer.messages.TextMessage;
import com.screens.game.board.Case;
import com.screens.game.board.Pawn;
import com.screens.game.board.Tile;

import static com.utils.Colors.getColor;
import static com.utils.MainConstants.camera;
import static com.utils.TileAndCases.pawnList;
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

    public static void quit() {
        Multiplayer.courrier.sendMessage(new TextMessage("quitting"));
        Multiplayer.courrier.killThread();
        if (Multiplayer.isServer) Multiplayer.serverMaker.killThread();
        Gdx.app.exit();
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
    static float step = 20;
    static Vector3 target;
    public static void updateCamera() {
        target = new Vector3();
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) target.add(0f, 1, 0f);
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) target.add(-1, 0f, 0f);
        if (Gdx.input.isKeyPressed(Input.Keys.S)) target.add(0f, -1, 0f);
        if (Gdx.input.isKeyPressed(Input.Keys.D)) target.add(1, 0f, 0f);
        if (!target.isZero()) {
            target.scl(step*camera.zoom);
            target.add(camera.position);
            for (int loop = 1; loop <= 50; loop++) {
                camera.position.interpolate(target, 0.1f, Interpolation.smooth);
                camera.update();
            }
        }
    }
    public static Case findCase() {
        for (Tile tile : tileList) {
            if (tile.x <= Functions.mouseInput().x && Functions.mouseInput().x <= tile.x + tile.getSize() &&
                    tile.y <= Functions.mouseInput().y && Functions.mouseInput().y <= tile.y + tile.getSize()) {
                try {
                    return tile.getCase(Functions.mouseInput());
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Clicked border of Tile in Pawn.findCase");
                }
            }
        }
        return null;
    }

    public static Case findCase(Vector2 coordinates) {
        for (Tile tile : tileList) {
            if (tile.x <= coordinates.x && coordinates.x <= tile.x + tile.getSize() &&
                    tile.y <= coordinates.y && coordinates.y <= tile.y + tile.getSize()) {
                try {
                    return tile.getCase(coordinates);
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Clicked border of Tile in Pawn.findCase");
                }
            }
        }
        return null;
    }

    public static Pawn getPawn(String color) {
        for (Pawn pawn : pawnList) {
            if (getColor(pawn.getColor()).equals(color)) {
                return pawn;
            }
        }
        return null;
    }
}
