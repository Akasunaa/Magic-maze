package com.multiplayer.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class NeededConstants {
    /*
    Je sais pas si c'est hérétique de créer une classe comme ça
    Mais désolé j'en ai marre de me trainer des courrier dans trente six fonctions
    juste pour pouvoir envoyer un message au bout d'un clique
    Et puis c'est plus pratique pour récupérer les coordonées comme ça, boum, checkmate atheists.
     */

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
