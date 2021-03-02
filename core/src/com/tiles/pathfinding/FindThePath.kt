package com.tiles.pathfinding

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3


class FindThePath : ApplicationAdapter() {
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

    // Le joueur
    lateinit var player: Player

    // La caméra, toi même tu sais
    lateinit var camera: OrthographicCamera
    fun getMouseX(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x
    fun getMouseY(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y
    fun stringMousePosition(): String = "x = ${getMouseX().toInt()}; y = ${getMouseY().toInt()}"

    // Le truc pour faire le zoom
    lateinit var mouseWheelChecker: MouseWheelChecker

    override fun create() {
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        // Cette caméra nous sert à avoir le bon système de coordonées
        mouseWheelChecker = MouseWheelChecker(camera)
        Gdx.input.setInputProcessor(mouseWheelChecker)

        tileList = ArrayList<Tile>()
        tileList.add(Tile(2))
        for (tile in tileList) {
            tile.load()
            tile.setSize(400f)
        }
        greenPawn = Pawn("green")
        greenPawn.setCase = tileList.get(0).caseList[0][2]
        greenPawn.load()
        // On sélectionne le bon numéro de case et on la charge
        // Le chargement est nécessaire pour le rendre sérializable

        player = Player(true, true, true, true, true, true)

        queue = Queue(3) // J'ai fait les cases uniquement jusqu'à la 9
        queue.load()
        queue.setSize(300f)
        queue.setCoordinates(700f, 200f)


        // Bon là c'ets le batch et des trucs pour écrire, rien d'important
        batch = SpriteBatch()
        coordMouse = BitmapFont()
        coordMouse.setColor(0f, 0f, 0f, 1f)
        numberCase = BitmapFont()
        numberCase.setColor(0f, 0f, 0f, 1f)

    }

    override fun render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        // Couleur d'arrière plan, et on clear tout
        batch.setProjectionMatrix(camera.combined) // On change le système de coordonées

        batch.begin()
        coordMouse.draw(batch, stringMousePosition(), 700f, 150f) // On écrit les coordonées


        queue.draw(batch)
        queue.handleInput(getMouseX(), getMouseY(), tileList)
        for (tile in tileList) {
            tile.draw(batch) // On dessine la tuile
            //tile.handleInput(batch, player, getMouseX(),getMouseY(),numberCase) // On gère l'input
        }
        greenPawn.draw(batch)
        greenPawn.handleInput(player, getMouseX(), getMouseY(), tileList)


        // Gestion du déplacement de la caméra
        val step = 1f
        var displacement = Vector2()
        if (Gdx.input.isKeyPressed(Input.Keys.Z)) displacement.add( 0f, step)
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) displacement.add(-step, 0f)
        if (Gdx.input.isKeyPressed(Input.Keys.S)) displacement.add(0f, -step)
        if (Gdx.input.isKeyPressed(Input.Keys.D)) displacement.add(step, 0f)
        if (!displacement.isZero()) {
            for (i in 1..10) {
                camera.translate(displacement)
                camera.update()
            }
        }
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