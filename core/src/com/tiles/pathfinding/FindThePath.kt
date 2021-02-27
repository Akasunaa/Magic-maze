package com.tiles.pathfinding

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3


class FindThePath : ApplicationAdapter() {
    lateinit var batch: SpriteBatch

    // Trucs de déboguage: pour afficher les coordonées de la souris et de la case cliquée
    lateinit var coordMouse: BitmapFont
    lateinit var numberCase: BitmapFont

    // tempCase sera utilisé plus tard pour stocker une case en mémoire
    lateinit var tempCase: Case

    // La Case qu'on affiche ici, rappel qu'on fait tout en déboguage pour le moment
    lateinit var tile: Tile

    // La caméra, toi même tu sais
    lateinit var camera: OrthographicCamera
    fun getMouseX(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).x
    fun getMouseY(): Float = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f)).y
    fun stringMousePosition(): String = "x = ${getMouseX().toInt()}; y = ${getMouseY().toInt()}"


    override fun create() {
        camera = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        camera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        // Cette caméra nous sert à avoir le bon système de coordonées

        tile = Tile(2)
        tile.load()
        // On sélectionne le bon numéro de case et on la charge
        // Le chargement est nécessaire pour le rendre sérializable

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

        tile.draw(batch) // On dessine la tuile

        // Puis si on clique gauche, boum, le pathfinding
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            try {
                tempCase = tile.getCase(getMouseX() - tile.x, getMouseY() - tile.y)
                tempCase.show(batch)
                tempCase.explore(batch,true,false,false,false,false,true)
                numberCase.draw(batch, "x = ${tempCase.x}; y = ${tempCase.y}; couleur = ${tempCase.color}, portal = ${tempCase.hasPortal}",700f, 200f)
            } catch (e: ArrayIndexOutOfBoundsException) {}
            // Bah oui parce que si on est pas dans les bornes de la tuile forcément getCase fonctionne moins bien lol
        }

        batch.end()
    }

    override fun dispose() {
        batch.dispose()
        tile.dispose()
    }

}