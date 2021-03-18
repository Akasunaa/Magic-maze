package com.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Sprite
import com.utils.Functions.mouseInput
import com.utils.MainConstants.batch
import com.utils.Multiplayer.courrier
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class BigButton(private val idlePath: String, private val pushedPath: String,
                var x: Float, var y: Float,
                var width: Float, var height: Float,
                private val cooldown: Int, val id: String) {

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


    private fun updateSprite() {
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

    fun draw() {
        if (isClickable()) active = idle
        active.draw(batch)
    }

    private fun onClickedLocally() {
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

    private fun isClickable() = System.currentTimeMillis() - startTime > cooldown

    fun isClickedAndValid() = (isClickable() &&
            Gdx.input.isButtonPressed(Input.Buttons.LEFT) &&
            (x <= mouseInput().x && mouseInput().x <= x + width) &&
            (y <= mouseInput().y && mouseInput().y <= y + height))

    fun check() {
        if (isClickedAndValid()) onClickedLocally()
    }


    fun dispose() {
        idle.texture.dispose()
        pushed.texture.dispose()
    }

    fun getID(): String = id
}