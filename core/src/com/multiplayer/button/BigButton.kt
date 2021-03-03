package com.multiplayer.button

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.multiplayer.button.NeededConstants.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import java.lang.System.load

@Serializable
class BigButton(val idlePath: String, val pushedPath: String,
                var x: Float, var y: Float,
                var width: Float, var height: Float,
                val cooldown: Int, val id: String) {

    @Transient
    lateinit var idle: Sprite
    @Transient
    lateinit var pushed: Sprite
    @Transient
    lateinit var active: Sprite

    fun load() {
        idle = Sprite(Texture(idlePath))
        pushed = Sprite(Texture(pushedPath))
        active = pushed
        updateSprite()
    }

    fun serialize() = Json.encodeToString(this)
    var startTime: Long = 0L // Utile pour le cooldown


    fun updateSprite() {
        idle.setPosition(x, y)
        pushed.setPosition(x, y)
        idle.setSize(width, height)
        pushed.setSize(width, height)
    }

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setSize(width: Float, height: Float) {
        this.width = width
        this.height = height
    }

    fun stringPosition(): String {
        return "x = $x; y = $y"
    }
    // Those three aren't needed anymore, but who knows, they might just be someday

    fun update() {
        active.draw(batch)
    }

    fun isClicked(): Boolean = (x <= mouseInput().x && mouseInput().x <= x + width) && (y <= mouseInput().y && mouseInput().y <= y + height)

    fun onClickedLocally(courrier: Courrier) {
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

    fun isClickable(): Boolean =
        if (System.currentTimeMillis() - startTime > cooldown) {
            active = idle
            true
        } else {
            false
        }

    fun checkAll() = (isClickable() && Gdx.input.isButtonPressed(Input.Buttons.LEFT) && isClicked())
    fun check() { if (checkAll()) onClickedLocally(courrier) }

    fun dispose() {
        idle.texture.dispose()
        pushed.texture.dispose()
    }

    fun getID(): String = id
}