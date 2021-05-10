package com.utils;

import com.badlogic.gdx.math.Matrix3;
import com.badlogic.gdx.math.Vector2;
import com.tiles.Case;
import com.tiles.Pawn;
import com.tiles.Queue;
import com.tiles.Tile;

import java.util.ArrayList;

public class TileAndCases {
    /*
    C'est sans doute de l'hérétisme mais au moins ici je peux gérer toutes les variables facilement
    Boum, pour une fois que de l'hérétisme paye
    En vrai je sais pas si c'est de l'hérétisme, mais on nous avait dit que les classes statiques c'était caca donc bon...
     */
    public static float tileSize = 600;
    public static float offset = 40 * tileSize / 600;
    public static float caseSize = (tileSize - 2 * offset) / 4;
    public static Vector2 origin = new Vector2();
    public static ArrayList<Tile> tileList;
    public static Case lastExploredCase;

    public static boolean isInPhaseB = false;

    public static Matrix3 newBase = new Matrix3(new float[]{tileSize, -caseSize, 0f, caseSize, tileSize, 0f, 0f, 0f, 1f});
    public static Matrix3 newBaseInvert = new Matrix3(newBase).inv();

    public static Queue queue=null;

    public static ArrayList<Pawn> pawnList = new ArrayList();


}
