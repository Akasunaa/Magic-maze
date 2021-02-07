package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont

class PushTheButton : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var button: BigRedButton
    lateinit var coordButton: BitmapFont
    lateinit var coordMouse: BitmapFont

    override fun create() {
        batch = SpriteBatch()
        button = BigRedButton(250f, 0f, 200f, 450f)
        coordButton = BitmapFont()
        coordMouse = BitmapFont()
        coordButton.setColor(0f,0f,0f,1f)
        coordMouse.setColor(0f,0f,0f,1f)

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
                button.isClicked(Gdx.input.getX().toFloat(), Gdx.input.getY().toFloat())
            }

        // Useless but worth keeping
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            button.translateX( -1 )
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            button.translateX( 1 )
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            button.translateY( 1 )
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            button.translateY( -1 )

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