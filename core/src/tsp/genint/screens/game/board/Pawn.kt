package tsp.genint.screens.game.board

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.math.Vector2
import tsp.genint.multiplayer.messages.pawn.AskPlacePawn
import tsp.genint.multiplayer.messages.pawn.AskTakePawn
import tsp.genint.multiplayer.messages.pawn.MovingPawn
import tsp.genint.screens.GameScreens
import tsp.genint.screens.game.BaseActor
import tsp.genint.screens.game.hud.Clock
import tsp.genint.screens.game.hud.GameInterface
import tsp.genint.utils.*
import java.io.Serializable
import java.util.concurrent.BrokenBarrierException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class Pawn(val color: Color) : Serializable {
    var player: Player? = null
    private var isLocked = false

    // Un petit booléen qui permet d'éviter qu'on bouge les pions tant qu'on a pas mis une tuile à la sortie
    fun unlock() {
        isLocked = false
    }

    private var setCase: Case? = null // La case sur laquelle est le pion

    @Transient
    private var sprite: BaseActor? = null
    fun setCase(tempCase: Case) {
        setCase?.pawn = null
        setCase = tempCase
        setCase!!.pawn = this
        if (setCase!!.exit && setCase!!.color === color) {
            if (color === Color.GREEN) {
                GameScreens.gameScreen.getInterface().unmute()
            }
            queue!!.reveal()
            isLocked = !queue!!.isEmpty
        }
        if (setCase!!.hasHourglass) {
            Clock.clock.reset()
            Clock.clock.pause()
            setCase!!.hasHourglass = false
            setCase!!.used()
            GameInterface.logs.newMessage("${player!!.pseudo} a réinitialisé le compteur")
        }
        if (!isInPhaseB) {
            if (setCase!!.hasWeapon && setCase!!.color === color) {
                numberWeaponsRetrieved++
                isLocked = true
                GameInterface.logs.newMessage("${player!!.pseudo} a récupéré l'arme $color")
            }
        }
        if (setCase!!.finalExit && isInPhaseB) {
            numberPawnsOut++
            GameInterface.logs.newMessage("${player!!.pseudo} a sorti le pion $color")
            GameScreens.gameScreen.removePawn(this)
            setCase!!.pawn = null
            dispose()
        }
    }

    var size = 0f
        // Je le met parce que Compte me l'a demandé
        private set(size) {
            field = size
            sprite!!.setSize(size / 2, size)
            println("Youhou")
        }

    // Fonctions classiques pour gérer la taille
    private var isMovable = false // Même principe que pour la Queue
    fun load() { // Pour la sérialization
        sprite = BaseActor(Texture("Game/Pawns/" + color.toString().toLowerCase() + ".png"))
        GameScreens.gameScreen.mainStage.addActor(sprite)
        size = caseSize / 0.95f
        sprite!!.setSize(size / 2, size)
        updateCoordinates()
    }

    private fun setSpritePosition(target: Vector2) {
        sprite!!.x = target.x
        sprite!!.y = target.y
        Companion.position.x = target.x
        Companion.position.y = target.y
    }

    var hasTarget = false
    fun setTarget(x: Float, y: Float) {
        target.x = x
        target.y = y
        hasTarget = true
    }

    fun interpolate(alpha: Float, interpolation: Interpolation?) {
        val temp = Companion.position.interpolate(target, alpha, interpolation)
        // J'ai supprimé l'interpolation, ça causait des problèmes
        setSpritePosition(temp)
        hasTarget = false
    }

    fun sendToTarget() {
        setSpritePosition(target)
        hasTarget = false
    }

    private fun updateCoordinates() {
        setSpritePosition(
            Vector2(
                setCase!!.getX(setCase!!.x) + (caseSize - sprite!!.width) / 2,
                setCase!!.getY(setCase!!.y) + caseSize / 10
            )
        )
    }

    fun dispose() {
        sprite!!.remove()
    }

    val position: Vector2
        get() = Companion.position

    private fun canPlaceHere(coordinates: Vector2, player: Player): Boolean {
        try {
            val nextCase: Case? = findCase(coordinates) // Est-on sur une case ?
            if (nextCase != null && nextCase.isValid && checkServerForPlaceable(coordinates) || nextCase == player.pawn.setCase) { // Pour checker le cas où on veut la replacer là où elle était de base i guess
                setCase!!.revert(player) // On annule le pathfinding... avec un autre pathfinding
                setCase!!.hide() // On cache la case de départ
                isMovable = false
                return true
            }
        } catch (e: NullPointerException) {
            println("No Tile found in Pawn.handleInput")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private var count = 3
    fun place(coordinates: Vector2) {
        val nextCase = findCase(coordinates) // Est-on sur une case ?
        if (nextCase == null) {
            System.err.println("No Tile found in Pawn.handleInput")
            return
        } else {
            setCase(nextCase) // On change la case
            updateCoordinates() // Et on met à jour les coordonées, qui sont entièrement calculées à partir de la case
        }
    }

    fun handleInput(player: Player) {
        // On me fait remarquer que la seule valeur que prends player c'est Multiplayer.me
        // Ce ne serait pas le cas si le serveur checkait vraiment pour voir si tu peux le placer quelque part je crois
        // Ou alors c'est géré autre part et je me souviens plus d'où
        // Trop fatigué pour vérifier, je regarderai ça demain
        if (isMovable) {
            val x: Float = mouseInput().x - sprite!!.width / 2
            val y: Float = mouseInput().y - sprite!!.height / 2
            setSpritePosition(Vector2(x, y))
            if (count == 3) { // On veut pas avoir à le faire trop souvent
                // Edit: ça vient d'un vieux fragment de code où je n'envoyais ma position que toutes les actualisations
                // Ca causait des problèmes que je ne comprends pas, où le thread de render restait bloqué
                // J'ai aucune idée de pourquoi, mais bon en mettant 1 ça fonctionne
                // ça rends aussi totalement inutile mon code d'interpolation mais bon
                Multiplayer.courrier.sendMessage(MovingPawn(this, Vector2(x, y)))
                //System.out.println("Sending coordinates");
                count = 0
            }
            count++
            if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) { // Puis si on clique quelque part
                if (canPlaceHere(mouseInput(), player)) {
                    place(mouseInput())
                    player.dropsPawn(this)
                }
            }
        } else {
            isMovable =
                Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) && sprite!!.x < mouseInput().x && mouseInput().x < sprite!!.x + sprite!!.width && sprite!!.y < mouseInput().y && mouseInput().y < sprite!!.y + sprite!!.height &&
                        !isLocked && !player.isHoldingPawn && checkServerForClickable()
            if (isMovable) {
                setCase!!.show() // On montre la case de départ
                player.takesPawn(this)
                setCase!!.explore(player) // On lance le pathfinding (structure récursive)
            }
        }
    }

    private fun checkServerForClickable(): Boolean {
        Multiplayer.courrier.sendMessage(AskTakePawn(this))
        Multiplayer.courrier.setAnswer()
        try {
            println("Blocking in pawn check click")
            Multiplayer.cyclicBarrier.await(500, TimeUnit.MILLISECONDS)
            //Multiplayer.cyclicBarrier.reset();
            println("Unlocking in pawn check click")
            // Pour synchroniser les threads
        } catch (e: TimeoutException) {
            Multiplayer.cyclicBarrier.reset()
            return false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Multiplayer.courrier.answer
    }

    private fun checkServerForPlaceable(coordinates: Vector2): Boolean {
        Multiplayer.courrier.sendMessage(AskPlacePawn(this, coordinates))
        Multiplayer.courrier.setAnswer()
        println("Client: checked for placeable")
        try {
            println("Blocking in pawn check place")
            Multiplayer.cyclicBarrier.await(500, TimeUnit.MILLISECONDS)
            //Multiplayer.cyclicBarrier.reset();
            println("Unlocking in pawn check place")
            // Pour synchroniser les threads
        } catch (e: TimeoutException) {
            Multiplayer.cyclicBarrier.reset()
            return false
        } catch (_: BrokenBarrierException) {
        } catch (_: InterruptedException) {
        }
        return Multiplayer.courrier.answer
    }

    companion object {
        private val target = Vector2(0f, 0f)
        private val position = Vector2(0f, 0f)
    }
}
