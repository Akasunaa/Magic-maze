package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont

class PushTheButton : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var button: BigRedButton
    lateinit var coordButton: BitmapFont
    lateinit var coordMouse: BitmapFont

    val id = "PC de Hadrien"
    val ip = "157.159.41.36" //L'ip de mon PC fixe
    val port = 6969

    override fun create() {
        batch = SpriteBatch()
        button = BigRedButton(
                Texture("redButtonIdle.png"),
                Texture("redButtonPushed.png"),
                250f, 0f, 200f, 450f, 1000)
        coordButton = BitmapFont()
        coordMouse = BitmapFont()
        coordButton.setColor(0f,0f,0f,1f)
        coordMouse.setColor(0f,0f,0f,1f)
        //ThreadMaker(port).getThread().start() // On commence l'écoute
    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()

        // Drawing the coordinates
        coordButton.draw(batch, button.stringPosition(), 100f,100f)
        coordMouse.draw(batch, stringMousePosition(), 100f,150f)

        if (button.isClickable())
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
                //println("Souris")
                //button.isClickedLocally(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat(), id, ip, port)
                button.isClickedRemotely()
            }

        button.update(batch)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        button.dispose()
    }

    fun stringMousePosition(): String {
        val x = Gdx.input.getX()
        val y = Gdx.input.getY()
        return "x = $x; y = $y"
    }
}