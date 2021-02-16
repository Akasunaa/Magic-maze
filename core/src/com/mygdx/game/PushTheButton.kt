package com.mygdx.game

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import java.net.InetAddress

class PushTheButton : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var redButton: BigButton
    lateinit var bluButton: BigButton
    lateinit var coordMouse: BitmapFont
    lateinit var buttonList:ButtonList

    val serverIP = "157.159.41.36" //L'ip de mon PC fixe
    val ip = InetAddress.getLocalHost().hostAddress
    val id = InetAddress.getLocalHost().hostName
    val port = 6969
    val isServer = true

    lateinit var courrier: Courrier
    val clientList  = ClientList(1)


    override fun create() {
        batch = SpriteBatch()
        redButton = BigButton(
                Texture("redButtonIdle.png"),
                Texture("redButtonPushed.png"),
                200f, 0f, 200f, 450f,
                1000, "RedButton")
        bluButton = BigButton(
                Texture("bluButtonIdle.png"),
                Texture("bluButtonPushed.png"),
                400f, 0f, 200f, 450f,
                500, "BluButton")
        buttonList = ButtonList(redButton,bluButton)
        coordMouse = BitmapFont()
        coordMouse.setColor(0f,0f,0f,1f)

        if (isServer) ThreadMaker(port, redButton, clientList, buttonList).thread.start()
        // On commence l'écoute
        courrier = Courrier(id,port,serverIP)

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.begin()

        // Drawing the coordinates
        coordMouse.draw(batch, stringMousePosition(), 50f,150f)

        redButton.check(courrier)
        bluButton.check(courrier)


        redButton.update(batch)
        bluButton.update(batch)
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        redButton.dispose()
    }

    fun stringMousePosition(): String {
        val x = Gdx.input.getX()
        val y = Gdx.input.getY()
        return "x = $x; y = $y"
    }
}