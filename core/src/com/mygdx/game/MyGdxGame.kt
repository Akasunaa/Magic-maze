package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Input
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
    private lateinit var pion: Pion
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

    var startTime: Long = 0L // Utile pour le cooldown


    override fun create() {
        batch = SpriteBatch()
        //gestion camera
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        //fond
        val grille = Gdx.files.internal("Sans titre 2.png")
        texture = Texture(grille)
        //pion
        //val pion = Gdx.files.internal("pion.png")
        pion= Pion(Texture("pion.png"),0f,0f,50f,50f,500,"rouge")
    }

    override fun dispose() {
        batch.dispose()
        texture.dispose()
    }

    override fun render() {
        //gestion camera
        batch.setProjectionMatrix(camera.combined)
        //fonction boucle
        batch.begin()

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)){
            pion.movePawn(getMouseX(),getMouseY())
        }
        batch.draw(texture, 0.toFloat(), 0.toFloat())
        pion.update(batch)

        batch.end()

        //println(pion.x)
    }


    //coordonnée souris
    fun getMouseX(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x
    fun getMouseY(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y


}