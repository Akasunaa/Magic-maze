package tsp.genint.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import tsp.genint.multiplayer.messages.TextMessage
import tsp.genint.screens.game.board.Case
import tsp.genint.screens.game.board.Pawn
import tsp.genint.screens.game.board.Tile
import kotlin.math.roundToInt
import kotlin.system.exitProcess

fun modulo(a: Int, b: Int): Int {
    return (a % b + b) % b
}

fun mouseInput(): Vector2 {
    val temp: Vector3 =
        MainConstants.camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
    return Vector2(temp.x, temp.y)
}

fun mouseInput(camera: OrthographicCamera): Vector2 {
    val temp: Vector3 = camera.unproject(Vector3(Gdx.input.x.toFloat(), Gdx.input.y.toFloat(), 0f))
    return Vector2(temp.x, temp.y)
}

fun quit() {
    if (Multiplayer.isServer) {
        Multiplayer.serverMaker.killThread()
    } else {
        Multiplayer.courrier.sendMessage(TextMessage("quitting"))
    }
    Multiplayer.courrier.killThread()
    exitProcess(0)
}

fun snap(mousePosition: Vector2) {
    // Beaucoup de debug ici
    // C'est essentiellement un changement de base, un arrondissement à l'entier, puis on remet la bonne base
    //System.out.println(mousePosition);
    mousePosition.sub(origin)
    //System.out.println(mousePosition);
    mousePosition.mul(newBaseInvert)
    //System.out.println(mousePosition);
    mousePosition.x = mousePosition.x.roundToInt().toFloat()
    mousePosition.y = mousePosition.y.roundToInt().toFloat()
    //System.out.println(mousePosition);
    mousePosition.mul(newBase)
    //System.out.println(mousePosition);
    mousePosition.add(origin)
    //System.out.println(mousePosition);
}

val tile: Tile?
    get() {
        for (tile in tileList) {
            if (tile.x < mouseInput().x && mouseInput().x < tile.x + tileSize && tile.y < mouseInput().y && mouseInput().y < tile.y + tileSize) {
                return tile
            }
        }
        return null
    }

fun getTile(mousePosition: Vector2): Tile? {
    for (tile in tileList) {
        if (tile.x < mousePosition.x && mousePosition.x < tile.x + tileSize && tile.y < mousePosition.y && mousePosition.y < tile.y + tileSize) {
            return tile
        }
    }
    return null
}

var step = 20f
var target: Vector3? = null
fun updateCamera() {
    target = Vector3()
    if (Gdx.input.isKeyPressed(Input.Keys.Z)) target!!.add(0f, 1f, 0f)
    if (Gdx.input.isKeyPressed(Input.Keys.Q)) target!!.add(-1f, 0f, 0f)
    if (Gdx.input.isKeyPressed(Input.Keys.S)) target!!.add(0f, -1f, 0f)
    if (Gdx.input.isKeyPressed(Input.Keys.D)) target!!.add(1f, 0f, 0f)
    if (!target!!.isZero) {
        target!!.scl(step * MainConstants.camera.zoom)
        target!!.add(MainConstants.camera.position)
        for (loop in 1..50) {
            MainConstants.camera.position.interpolate(target, 0.1f, Interpolation.smooth)
            MainConstants.camera.update()
        }
    }
}

fun findCase(): Case? {
    for (tile in tileList) {
        if (tile.x <= mouseInput().x && mouseInput().x <= tile.x + tile.size && tile.y <= mouseInput().y && mouseInput().y <= tile.y + tile.size) {
            try {
                return tile.getCase(mouseInput())
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("Clicked border of Tile in Pawn.findCase")
            }
        }
    }
    return null
}

fun findCase(coordinates: Vector2): Case? {
    for (tile in tileList) {
        if (tile.x <= coordinates.x && coordinates.x <= tile.x + tile.size && tile.y <= coordinates.y && coordinates.y <= tile.y + tile.size) {
            try {
                return tile.getCase(coordinates)
            } catch (e: ArrayIndexOutOfBoundsException) {
                println("Clicked border of Tile in Pawn.findCase")
            }
        }
    }
    return null
}

fun getPawn(color: String): Pawn? {
    for (pawn in pawnList) {
        if (pawn.color.toString() == color) {
            return pawn
        }
    }
    return null
}
