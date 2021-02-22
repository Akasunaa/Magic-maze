package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.math.Rectangle;
import kotlin.properties.Delegates
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import ktx.app.KtxApplicationAdapter
import ktx.graphics.use

class MyGdxGame : ApplicationAdapter() {
    private lateinit var renderer: ShapeRenderer
    //pion
    private lateinit var Pion: Sprite
    private var pion = Pion(25f,325f)
    //fond
    private lateinit var texture: Texture
    private lateinit var batch: SpriteBatch
    //camera
    lateinit var camera: OrthographicCamera
    //représentation tuile
    val colonne0: IntArray = intArrayOf(1,0,1,1,0)
    val colonne1: IntArray = intArrayOf(1,0,1,1,0)
    val colonne2: IntArray = intArrayOf(1,0,1,1,0)
    val colonne3: IntArray = intArrayOf(1,0,0,0,0)
    val colonne4: IntArray = intArrayOf(1,1,1,1,1)
    val tab: Array<IntArray> = arrayOf(colonne0,colonne1,colonne2,colonne3,colonne4)


    override fun create() {
        batch = SpriteBatch()
        //gestion camera
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        //fond
        val grille = Gdx.files.internal("Sans titre 2.png")
        texture = Texture(grille)
        //pion
        val pion = Gdx.files.internal("pion.png")
        Pion= Sprite(Texture(pion))
    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
    }

    override fun render() {
        //gestion camera
        batch.setProjectionMatrix(camera.combined)
        //fonction boucle
        handleInput()
        draw()
    }

    //coordonnée souris
    fun getMouseX(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x
    fun getMouseY(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y

    //test si on click avec la souris
    private fun handleInput() {
        val click = Gdx.input.isTouched
        if (click) {
            //ancienne coordonée
            //val X = Gdx.input.x.toFloat()
            //val Y = Gdx.graphics.getHeight() - Gdx.input.y.toFloat() pour inverser l'origine du click
            val X = getMouseX()
            val Y = getMouseY()
            val x = (X / 100).toInt()
            val y = (Y / 100).toInt()
            //println(x)
            //println(y)
            if (tab[x][y] == 0) {
                pion = Pion((x * 100 + 25).toFloat(), (y * 100 + 25).toFloat())
            }
        }
    }

    //dessin
     private fun draw(){
         batch.begin()
         batch.draw(texture, 0.toFloat(), 0.toFloat())
         batch.draw( Pion,pion.x,pion.y)
         batch.end()

     }



}