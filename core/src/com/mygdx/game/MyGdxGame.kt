package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.TextureArray
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Rectangle;
import kotlin.properties.Delegates
import com.badlogic.gdx.math.Vector3


class MyGdxGame : ApplicationAdapter() {

    //fond
    private var texture: Texture? = null
    private var batch: SpriteBatch? = null

    //pion
    private var Pion: Sprite? = null

    //camera
    lateinit var camera: OrthographicCamera



    //représentation tuile
    val colonne0: IntArray = intArrayOf(1,0,1,0,1)
    val colonne1: IntArray = intArrayOf(0,0,0,0,0)
    val colonne2: IntArray = intArrayOf(1,1,1,1,1)
    val colonne3: IntArray = intArrayOf(0,0,0,0,0)
    val colonne4: IntArray = intArrayOf(0,0,0,0,0)
    val tab: Array<IntArray> = arrayOf(colonne0,colonne1,colonne2,colonne3,colonne4)



    override fun create() {
        batch = SpriteBatch()
        //gestion camera
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        //fond
        val worldFile = Gdx.files.internal("Sans titre 1.png")
        texture = Texture(worldFile)
        //pion
        val pion = Gdx.files.internal("pion.png")
        Pion= Sprite(Texture(pion))

    }

    override fun dispose() {
        batch!!.dispose()
        texture!!.dispose()
    }




    override fun render() {
        //gestion camera
        batch!!.setProjectionMatrix(camera.combined)
        //affichage sprite
        batch!!.begin()
        batch!!.draw(texture, 0.toFloat(), 0.toFloat())
        batch!!.draw(Pion,450.toFloat(),450.toFloat())
        batch!!.end()
        //Coordonnée souris


        val estTouche = Gdx.input.isTouched
        if(estTouche) {
            //val X = Gdx.input.x.toFloat()
            //val Y = Gdx.graphics.getHeight() - Gdx.input.y.toFloat() pour inverser l'origine du click

            val X=getMouseX()
            val Y=getMouseY()
            val x = (X / 100).toInt()
            val y = (Y / 100).toInt()
            println(x)
            println(y)
            if (tab[x][y] == 0) {
                var pionx = (x*100+25).toFloat()
                var piony = (y*100+25).toFloat()
                batch!!.begin()
                batch!!.draw(Pion,pionx,piony)
                batch!!.end()
            }
        }
    }
    //coordonnée souris
    fun getMouseX(): Float {return camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x}
    fun getMouseY(): Float {return camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y}

}