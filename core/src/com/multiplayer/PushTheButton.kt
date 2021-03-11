package com.multiplayer

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.utils.Functions.mouseInput
import com.utils.MainConstants
import com.utils.Multiplayer
import com.utils.Multiplayer.isServer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.InetAddress


class PushTheButton : ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    lateinit var redButton: BigButton
    lateinit var bluButton: BigButton
    lateinit var greenButton: BigButton
    lateinit var coordMouse: BitmapFont

    val serverIP = "157.159.41.36" // L'ip de mon PC fixe
    val ip = InetAddress.getLocalHost().hostAddress // L'ip de ce pc

    //val id = InetAddress.getLocalHost().hostName // L'id de ce pc
    val port = 6969 // Le port du serveur
    val id = "PC-Fixe-Server=$isServer"


    lateinit var courrier: Courrier
    lateinit var key: Decryptor
    val clientList = ClientList(1)
    val buttonList = ButtonList()

    val windowWidth = 1280f
    val windowHeight = 720f
    lateinit var camera: OrthographicCamera
    var count = 1


    override fun create() {
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        // Cette caméra nous sert à avoir le bon système de coordonées
        batch = SpriteBatch()
        redButton = BigButton(
                "button/redButtonIdle.png",
                "button/redButtonPushed.png",
                200f, 0f, 207f, 570f,
                1000, "RedButton")
        bluButton = BigButton(
                "button/bluButtonIdle.png",
                "button/bluButtonPushed.png",
                450f, 0f, 207f, 570f,
                500, "BluButton")
        greenButton = BigButton(
                "button/greenButtonIdle.png",
                "button/greenButtonPushed.png",
                700f, 0f, 207f, 570f,
                500, "GreenButton")
        // C'est ce dernier bouton qu'on va envoyer au client

        buttonList.add(redButton)
        buttonList.load()
        key = Decryptor()
        coordMouse = BitmapFont()
        coordMouse.setColor(0f, 0f, 0f, 1f)

        if (isServer) ServerMaker(port, clientList).thread.start()

        // Il faut abandonner les mutilples thread et juste le faire dans render

        //else ClientListener(key,courrier.socket).thread.start()
        // Le principe du ClientListener est peut être nul en fait
        // Il faudrait mieux faire une socket Client -> Serveur et une socket Serveur -> Client ????
        // On commence l'écoute

        courrier = Courrier(id, port, serverIP)

        MainConstants.camera = camera
        Multiplayer.courrier = courrier
        Multiplayer.buttonList = buttonList
        Multiplayer.clientList = clientList
        MainConstants.batch = batch
        Multiplayer.key = key
    }


    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        batch.setProjectionMatrix(camera.combined) // On change le système de coordonées
        batch.begin()

        // Drawing the coordinates
        coordMouse.draw(batch, stringMousePosition(), 50f, 150f)


        if (redButton.isClickedAndValid()) {
            if (count == 1) GlobalScope.launch { courrier.sendObject(greenButton) }
            if (count == 2) GlobalScope.launch { courrier.sendObject(bluButton) }
            count++

        }
        for (button in buttonList.buttonList) {
            button.check()
            button.draw()
        }
        buttonList.load()

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        for (button in buttonList.buttonList) {
            button.dispose()
        }
    }

    fun stringMousePosition(): String {
        val x = mouseInput().x.toInt()
        val y = mouseInput().y.toInt()
        return "x = $x; y = $y"
    }
}