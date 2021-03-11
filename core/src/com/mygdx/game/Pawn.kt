package com.mygdx.game


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import java.lang.System.load

class Pawn (Texture: Texture,
           var x: Float, var y: Float,
           var width: Float, var height: Float, val pioncolor: String) {

    val colonne0: IntArray = intArrayOf(0,1,1,0,1)
    val colonne1: IntArray = intArrayOf(0,1,1,0,1)
    val colonne2: IntArray = intArrayOf(0,1,1,0,1)
    val colonne3: IntArray = intArrayOf(0,0,0,0,1)
    val colonne4: IntArray = intArrayOf(1,1,1,1,1)
    public val tab: Array<IntArray> = arrayOf(colonne0,colonne1,colonne2,colonne3,colonne4)

    var isMovable = false

    var highlight: Sprite = Sprite(Texture("highlight.png"))

    val texture: Sprite = Sprite(Texture)

    init {
        updateSprite()
    }

    fun updateSprite() {
        texture.setPosition(x, y)
        highlight.setPosition(x-25,y-25)
        texture.setSize(width, height)

    }
    //donne la position au pion
    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }
    //donne la taille du sprite
    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    fun update(batch: SpriteBatch) {
        texture.draw(batch)
        highlight.draw(batch)
    }

    //dit si le pion est cliqué
    //fun isClicked(iX: Float, iY: Float): Boolean = (x <= iX && iX <= x + width) && (y <= iY && iY <= y + height)

    fun movePawn(inputX: Float, inputY: Float) {
        val highlight = Gdx.files.internal("highlight.png")
        var nbX = (inputX/100).toInt()
        var nbY = (inputY/100).toInt()
        var X=(nbX*100+25).toFloat()
        var Y=(nbY*100+25).toFloat()
        if (isMovable) {
            if (tab[nbX][nbY] == 0) {
                setPosition(X, Y)
                updateSprite()
                isMovable = false
                if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                    isMovable = false
                }
            }
        } else {
            isMovable = isMovable || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && (x < inputX) && (inputX < x + width && (y < inputY) && (inputY < y + height)))

        }
    }

    fun dispose() {
        texture.texture.dispose()
        //highlight.texture.dispose()
    }

    fun getColor(): String = pioncolor
}













