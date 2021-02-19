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
import com.badlogic.gdx.math.Rectangle;


class MyGdxGame : ApplicationAdapter() {

    //fond
    private var texture: Texture? = null
    private var batch: SpriteBatch? = null

    //pion






    //représentation tuile
    val colonne0: IntArray = intArrayOf(1,2,3,4,5)
    val colonne1: IntArray = intArrayOf(0,0,0,0,0)
    val colonne2: IntArray = intArrayOf(1,1,1,1,1)
    val colonne3: IntArray = intArrayOf(0,0,0,0,0)
    val colonne4: IntArray = intArrayOf(0,0,0,0,0)
    val tab: Array<IntArray> = arrayOf(colonne0,colonne1,colonne2,colonne3,colonne4)




    override fun create() {
        batch = SpriteBatch()

        //fond
        val worldFile = Gdx.files.internal("Sans titre 1.png")
        texture = Texture(worldFile)







    }

    override fun dispose() {
        batch!!.dispose()
        texture!!.dispose()
    }




    override fun render() {
        //Coordonnée souris
        val estTouche = Gdx.input.isTouched
        if(estTouche){
            val X = Gdx.input.x.toFloat()
            val Y = Gdx.input.y.toFloat()
            val x=(X/100).toInt()
            val y = (Y/100).toInt()
            println(tab[x][y])
        }
        //affiche du fond
        Gdx.gl.glClearColor(1f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch!!.begin()
        batch!!.draw(texture, 0f, 0f)
        batch!!.draw(pionSprite,0f,0f)
        batch!!.end()
    }

}