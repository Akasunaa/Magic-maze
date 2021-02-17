package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.scenes.scene2d.InputEvent
import ktx.app.clearScreen
import ktx.graphics.color
import ktx.graphics.use
import java.awt.Color.red
import java.awt.Shape
import java.util.Arrays.toString
import com.badlogic.gdx.scenes.scene2d.ui.Button
import ktx.actors.*
import com.badlogic.gdx.graphics.g2d.Sprite






data class Santa(val position: Float)
data class ChristmasGift(
    var height: Float = 720f,
    val position: Float = (40..1200).random().toFloat()
)

class MyGdxGame : ApplicationAdapter() {
    private var batch: SpriteBatch? = null
    private var texture: Texture? = null
    private var sprite: Sprite? = null
    private lateinit var renderer: ShapeRenderer
    private var player = Santa(40f)
    private var gifts = emptyList<ChristmasGift>()

    override fun create() {
        renderer = ShapeRenderer()
        val w = Gdx.graphics.width.toFloat()
        val h = Gdx.graphics.height.toFloat()
        batch = SpriteBatch()

        texture = Texture(Gdx.files.internal("redButtonIdle.png"))
        sprite = Sprite(texture)
        sprite!!.setPosition(w / 2 - sprite!!.getWidth() / 2, h / 2 - sprite!!.getHeight() / 2)
    }

    override fun dispose() {
        batch!!.dispose()
        texture!!.dispose()
    }

    override fun render() {
        handleInput()
        logic()
        draw()
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);


        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            sprite!!.setPosition(Gdx.input.getX() - sprite!!.getWidth()/2,
                Gdx.graphics.getHeight() - Gdx.input.getY() - sprite!!.getHeight()/2);
        }
        if(Gdx.input.isButtonPressed(Input.Buttons.RIGHT)){
            sprite!!.setPosition(Gdx.graphics.getWidth()/2 -sprite!!.getWidth()/2,
                Gdx.graphics.getHeight()/2 - sprite!!.getHeight()/2);
        }
        batch!!.begin();
        sprite!!.draw(batch!!);
        batch!!.end();
    }
    override fun resize(width: Int, height: Int) {}
    override fun pause() {}
    override fun resume() {}
    private fun handleInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.A)){
            player = Santa(player.position - 5f)
        } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            player = Santa(player.position + 5f)
        }
    }
    private fun logic() {
        if (Math.random() > 0.95) {
            gifts = gifts + ChristmasGift()
        }
        gifts.forEach {
            it.height -= 1f
        }
    }

    private fun draw() {
        clearScreen(0f, 0f, 0f, 0f)

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            renderer.color = color(0.0F,1F, 0.0F)
            gifts.forEach {
                renderer.rect(it.position, it.height, 60f, 60f)
            }
        }

        renderer.use(ShapeRenderer.ShapeType.Filled) {
            renderer.color = color(0.0F,1F, 0.0F)
            renderer.rect(player.position, 80f, 80f, 80f)
        }
    }
}