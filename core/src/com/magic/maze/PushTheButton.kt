package com.magic.maze

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import java.net.InetAddress


class PushTheButton : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var redButton: BigButton
    lateinit var bluButton: BigButton
    lateinit var greenButton: BigButton
    lateinit var coordMouse: BitmapFont

    val serverIP = "157.159.41.36" // L'ip de mon PC fixe
    val ip = InetAddress.getLocalHost().hostAddress // L'ip de ce pc
    val id = InetAddress.getLocalHost().hostName // L'id de ce pc
    val port = 6969 // Le port du serveur
    val isServer = true

    lateinit var courrier: Courrier
    lateinit var key: Decryptor
    val clientList = ClientList(1)
    val buttonList = ButtonList()

    val windowWidth = 1280f
    val windowHeight = 720f
    lateinit var camera: OrthographicCamera
    fun getMouseX(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x

    fun getMouseY(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y


    override fun create() {
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        // Cette caméra nous sert à avoir le bon système de coordonées
        batch = SpriteBatch()
        redButton = BigButton(
                Texture("button/redButtonIdle.png"),
                Texture("button/redButtonPushed.png"),
                200f, 0f, 207f, 570f,
                1000, "RedButton")
        bluButton = BigButton(
                Texture("button/bluButtonIdle.png"),
                Texture("button/bluButtonPushed.png"),
                450f, 0f, 207f, 570f,
                500, "BluButton")
        greenButton = BigButton(
                Texture("button/greenButtonIdle.png"),
                Texture("button/greenButtonPushed.png"),
                700f, 0f, 207f, 570f,
                500, "GreenButton")
        // C'est ce dernier bouton qu'on va envoyer au client
        buttonList.add(bluButton, redButton)
        key = Decryptor(buttonList, clientList)
        coordMouse = BitmapFont()
        coordMouse.setColor(0f, 0f, 0f, 1f)

        if (isServer) ServerMaker(port, clientList, key).thread.start()
        //else ClientListener(key,courrier.socket).thread.start()
        // Le principe du ClientListener est peut être nul en fait
        // Il faudrait mieux faire une socket Client -> Serveur et une socket Serveur -> Client ????
        // On commence l'écoute

        courrier = Courrier(id, port, serverIP)

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.setProjectionMatrix(camera.combined) // On change le système de coordonées
        batch.begin()

        // Drawing the coordinates
        coordMouse.draw(batch, stringMousePosition(), 50f, 150f)


        for (button in buttonList.buttonList) {
            button.check(courrier, getMouseX(), getMouseY())
            button.update(batch)
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        for (button in buttonList.buttonList) {
            button.dispose()
        }
    }

    fun stringMousePosition(): String {
        val x = getMouseX().toInt()
        val y = getMouseY().toInt()
        return "x = $x; y = $y"
    }
}