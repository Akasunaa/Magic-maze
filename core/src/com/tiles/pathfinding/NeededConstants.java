package com.tiles.pathfinding;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import java.util.ArrayList;

public class NeededConstants {
    /*
    C'est sans doute de l'hérétisme mais au moins ici je peux gérer toutes les variables facilement
    Boum, pour une fois que de l'hérétisme paye
    En vrai je sais pas si c'est de l'hérétisme, mais on nous avait dit que les classes statiques c'était caca donc bon...
     */
    static float tileSize = 300f;
    static float offset = 40 * tileSize / 600;
    static float caseSize = (tileSize - 2 * offset) / 4;
    static Vector2 origin = new Vector2();
    static OrthographicCamera camera;
    static Vector2 mouseInput() {
        Vector3 temp = camera.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
        return new Vector2(temp.x,temp.y);
    }
    static ArrayList<Tile> tileList;
    static SpriteBatch batch;
    static Case lastExploredCase;

//    private Vector2 firstVect = new Vector2(tileSize,-caseSize);
//    private Vector2 secondVect = new Vector2(caseSize, tileSize);
    static Matrix3 newBase = new Matrix3(new float[]{tileSize, -caseSize, 0f, caseSize, tileSize, 0f, 0f, 0f, 1f});
    static Matrix3 newBaseInvert = new Matrix3(newBase).inv();

    static void snap(Vector2 mousePosition) {
        // Beaucoup de debug ici
        // C'est essentiellement un changement de base, un arrondissement à l'entier, puis on remet la bonne base
        //System.out.println(mousePosition);
        mousePosition.sub(origin);
        //System.out.println(mousePosition);
        mousePosition.mul(newBaseInvert);
        //System.out.println(mousePosition);
        mousePosition.x = Math.round(mousePosition.x);
        mousePosition.y = Math.round(mousePosition.y);
        //System.out.println(mousePosition);
        mousePosition.mul(newBase);
        //System.out.println(mousePosition);
        mousePosition.add(origin);
        //System.out.println(mousePosition);
    }
    static Tile getTile() {
        for (Tile tile : tileList) {
            if ((tile.x < mouseInput().x) && (mouseInput().x < tile.x + tileSize) && (tile.y < mouseInput().y) && (mouseInput().y < tile.y + tileSize)) {
                return tile;
            }
        }
        return null;
    }
    static Tile getTile(Vector2 mousePosition) {
        for (Tile tile : tileList) {
            if ((tile.x < mousePosition.x) && (mousePosition.x < tile.x + tileSize) && (tile.y < mousePosition.y) && (mousePosition.y < tile.y + tileSize)) {
                return tile;
            }
        }
        return null;
    }
}
