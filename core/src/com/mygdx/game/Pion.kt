package com.mygdx.game


import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import java.lang.System.load

class Pion (Texture: Texture,
           var x: Float, var y: Float,
           var width: Float, var height: Float,
           val cooldown: Int, val pioncolor: String) {

    var isMovable = false

    val texture: Sprite = Sprite(Texture)

    init {
        updateSprite()
    }

    fun updateSprite() {
        texture.setPosition(x, y)
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
    }

    //dit si le pion est cliqué
    //fun isClicked(iX: Float, iY: Float): Boolean = (x <= iX && iX <= x + width) && (y <= iY && iY <= y + height)

    fun movePawn(inputX: Float, inputY: Float) {
        if (isMovable) {
            setPosition(inputX - width / 2, inputY - height / 2)
            updateSprite()
            isMovable = false
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                isMovable = false

            }
        } else {
            isMovable = isMovable || (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && (x < inputX) && (inputX < x + width && (y < inputY) && (inputY < y + height)))

        }
    }

    fun dispose() {
        texture.texture.dispose()
    }

    fun getColor(): String = pioncolor
}













