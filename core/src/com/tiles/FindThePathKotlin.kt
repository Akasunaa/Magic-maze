package com.tiles

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.fasterxml.jackson.databind.ObjectMapper
import com.utils.Functions.*
import com.utils.MainConstants
import com.utils.TileAndCases
import com.utils.TileAndCases.origin
import com.utils.TileAndCases.tileSize
import java.io.File

class FindThePathKotlin : ApplicationAdapter() {
    lateinit var batch: SpriteBatch

    // Trucs de déboguage: pour afficher les coordonées de la souris et de la case cliquée
    lateinit var coordMouse: BitmapFont
    lateinit var numberCase: BitmapFont

    // tempCase sera utilisé plus tard pour stocker une case en mémoire
    lateinit var tempCase: Case

    // La liste des Tuiles qu'on va afficher
    lateinit var tileList: ArrayList<Tile> // Uh ça marche tiens, je pensais pas que Kotlin accepterait ça... On va pas se plaindre

    // La pile de carte
    lateinit var queue: Queue

    // Le pion
    lateinit var greenPawn: Pawn
    var pawntimeBaby = false

    // Le joueur
    lateinit var player: Player

    var tempTile: Tile? = null

    val mapper = ObjectMapper()

    // La caméra, toi même tu sais
    lateinit var camera: OrthographicCamera
    fun stringMousePosition(): String = "x = ${mouseInput().x.toInt()}; y = ${mouseInput().y.toInt()}"

    // Le truc pour faire le zoom
    lateinit var mouseWheelChecker: MouseWheelChecker

    override fun create() {
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        MainConstants.camera = camera
        // Cette caméra nous sert à avoir le bon système de coordonées
        mouseWheelChecker = MouseWheelChecker()
        Gdx.input.inputProcessor = mouseWheelChecker

        tileList = ArrayList()
        //tileList.add(Tile(2))
        for (tile in tileList) {
            tile.load()
        }
        TileAndCases.tileList = tileList

//        greenPawn = Pawn("green")
//        greenPawn.setCase = tileList.get(0).caseList[0][2]
//        greenPawn.load()
        // On sélectionne le bon numéro de case et on la charge
        // Le chargement est nécessaire pour le rendre sérializable

        player = Player(true, true, false, false, false, false,false)
        //player = Player(false, false, true, true, false, false)


        queue = Queue(9) // J'ai fait les cases uniquement jusqu'à la 9
        queue.load()
        queue.setCoordinates(1280f - tileSize - 50f, 50f)


        // Bon là c'ets le batch et des trucs pour écrire, rien d'important
        batch = SpriteBatch()
        MainConstants.batch = batch
        coordMouse = BitmapFont()
        coordMouse.setColor(0f, 0f, 0f, 1f)
        numberCase = BitmapFont()
        numberCase.setColor(0f, 0f, 0f, 1f)

    }

    override fun render() {
        Gdx.gl.glClearColor(125f / 255, 125f / 255, 125f / 255, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // Couleur d'arrière plan, et on clear tout
        batch.projectionMatrix = camera.combined // On change le système de coordonées

        batch.begin()
        coordMouse.draw(batch, stringMousePosition() + "\n$origin", 700f, 150f) // On écrit les coordonées

        queue.draw()
        queue.handleInput()
        for (tile in tileList) {
            tile.draw() // On dessine la tuile
        }


        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            mapper.writerWithDefaultPrettyPrinter().writeValue(File("core/assets/tuiles/tile${queue.head.number}.json"), queue.head)
        }

        // Gestion du déplacement de la caméra
        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        for (tile in tileList) {
            tile.dispose()
        }
        greenPawn.dispose()
    }
}