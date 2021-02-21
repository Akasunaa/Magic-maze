package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import java.lang.System.load



class BigButton(idleTexture: Texture, pushedTexture: Texture,
                var x:Float, var y:Float,
                var width:Float, var height:Float,
                val cooldown: Int, val id: String) {



    val idle :  Sprite = Sprite(idleTexture)
    val pushed: Sprite = Sprite(pushedTexture)
    var active:Sprite = idle

    var startTime:Long = 0 // Utile pour le cooldown

    init {
        updateSprite()
    }

    fun updateSprite() {
        idle.setPosition(x,y)
        pushed.setPosition(x,y)
        idle.setSize(width, height)
        pushed.setSize(width, height)
    }

    fun setPosition(x:Float, y:Float) {
        this.x = x
        this.y = y
    }
    fun setSize(width:Float, height:Float) {
        this.width = width
        this.height = height
    }

    fun stringPosition (): String {
        return "x = $x; y = $y"
    }

    fun update(batch: SpriteBatch) {
        active.draw(batch)
    }

    fun isClicked(iX:Float, iY:Float): Boolean {return ((x <= iX && iX <= x + width) &&  (y <= iY && iY <= y + height))}

    fun onClickedLocally(courrier:Courrier) {
        // Envoie du message
        println("$id Clicked Locally")
        courrier.sendMessage("pressed $id")
        // Fin de l'envoi du message
        active = pushed
        startTime = System.currentTimeMillis()
    }

    fun onClickedRemotely() {
        println("$id Clicked Remotely")
        active = pushed
        startTime = System.currentTimeMillis()
    }

    fun isClickable(): Boolean {
        if (System.currentTimeMillis() - startTime > cooldown) {
            active = idle
            return true
        } else return false
    }

    fun check(courrier: Courrier,inputX: Float,inputY:Float) {
        if (isClickable() && Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            if (isClicked(inputX,inputY)) {
                onClickedLocally(courrier)
            }
    }
    fun dispose() {
        idle.texture.dispose()
        pushed.texture.dispose()
    }

    fun getID(): String {return id}
}