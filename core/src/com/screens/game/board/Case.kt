package com.screens.game.board

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.screens.GameScreens
import com.screens.game.BaseActor
import com.utils.*
import java.io.Serializable

class Case(number: CaseType, @field:Transient private var tile: Tile) : Serializable {
    @Transient
    var caseList = MutableList<Case?>(4) { null }
    var x = 0
    var y = 0
    var pawn: Pawn? = null
    var isValid = false
    private var isShowed = false

    @Transient
    private var shortcut: Case? = null

    @Transient
    private var elevator: Case? = null

    @Transient
    private var greenDot: BaseActor? = null

    @Transient
    private var redDot: BaseActor? = null
    fun getNeighbours(horizontalWalls: List<BooleanArray>, verticalWalls: List<BooleanArray>) {
        x = tile.getCaseCoordinates(this)[0]
        y = tile.getCaseCoordinates(this)[1]
        // Voisin en haut
        if (y == 0 || horizontalWalls[y - 1][x]) caseList[0] = null else caseList[0] = tile.caseList[y - 1][x]
        // Voisin de droite
        if (x == 3 || verticalWalls[y][x]) caseList[1] = null else caseList[1] = tile.caseList[y][x + 1]
        // Voisin du bas
        if (y == 3 || horizontalWalls[y][x]) caseList[2] = null else caseList[2] = tile.caseList[y + 1][x]
        // Voisin de gauche
        if (x == 0 || verticalWalls[y][x - 1]) caseList[3] = null else caseList[3] = tile.caseList[y][x - 1]
    }

    fun load(tile: Tile, caseList: List<Case?>) { // Comme d'habitude, obligatoire pour la sérialisation
        this.tile = tile
        this.caseList = caseList.toMutableList()
        greenDot = BaseActor(Texture("Game/Tiles/greenDot.png"))
        greenDot!!.isVisible = false
        redDot = BaseActor(Texture("Game/Tiles/redDot.png"))
        redDot!!.isVisible = false
        setSpriteCoordinates()
        GameScreens.gameScreen.mainStage.addActor(greenDot)
        GameScreens.gameScreen.mainStage.addActor(redDot)
    }

    fun getX(x: Int): Float {
        return tile.x + offset + x * caseSize
    }

    fun getY(y: Int): Float {
        return tile.y + offset + y * caseSize
    }

    private fun setSpriteCoordinates() {
        val tempX = getX(x)
        val tempY = getY(y)
        greenDot!!.x = tempX
        greenDot!!.y = tempY
        redDot!!.x = tempX
        redDot!!.y = tempY
    }

    fun setSize(size: Float) {
        greenDot!!.setSize(size, size)
        redDot!!.setSize(size, size)
    }

    fun updateCoordinates() {
        val xy = tile.getCaseCoordinates(this)
        if (tile.rotation == 0) {
            x = xy[0]
            y = xy[1]
        }
        if (tile.rotation == 3) {
            x = xy[1]
            y = 3 - xy[0]
        }
        if (tile.rotation == 2) {
            x = 3 - xy[0]
            y = 3 - xy[1]
        }
        if (tile.rotation == 1) {
            x = 3 - xy[1]
            y = xy[0]
        }
        setSpriteCoordinates()
    }

    fun show() {
        isShowed = true
        isValid = true
        redDot!!.isVisible = isShowed
        greenDot!!.isVisible = false
    }

    fun hide() {
        isShowed = false
        isValid = false
        redDot!!.isVisible = isShowed
        greenDot!!.isVisible = isValid
    }

    private fun explored() {
        isValid = true
        greenDot!!.isVisible = !isShowed
    }

    private fun unexplored() {
        isValid = false
        greenDot!!.isVisible = isValid
    }

    fun used() {
        val t = Texture(Gdx.files.internal("Game/Tiles/used.png"))
        val cross = BaseActor(t)
        cross.setOrigin((cross.width / 2).toInt())
        cross.setPosition(getX(x), getY(y))
        GameScreens.gameScreen.mainStage.addActor(cross)
    }

    fun draw() {
        if (isShowed) redDot!!.draw(MainConstants.batch, 1f) else if (isValid) greenDot!!.draw(MainConstants.batch, 1f)
    }

    private var seen = false

    private val accessible: Boolean
    val hasPortal: Boolean
    val entrance: Boolean
    var hasHourglass: Boolean
    var exit: Boolean
    val hasWeapon: Boolean
    val finalExit: Boolean
    val color: Color

    init {
        accessible = number != CaseType.BasicCase.INACCESSIBLE
        entrance = number == CaseType.BasicCase.ENTRANCE
        hasHourglass = number == CaseType.BasicCase.HOURGLASS
        exit = number is CaseType.ColoredCase.Exit
        hasPortal = number is CaseType.ColoredCase.Portal
        hasWeapon = number is CaseType.ColoredCase.Weapon
        finalExit = number is CaseType.ColoredCase.FinalExit
        color = if (number is CaseType.ColoredCase) {
            number.color
        } else Color.NONE
    }

    fun explore(player: Player) {
        var index: Int // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        val tempPlayer = player.rotate(tile.rotation)
        if (!seen && accessible && (player.pawn === pawn || pawn == null)) {
            seen = true // Parcours de graphe classique pour éviter les StackOverflow
            explored()
            if (tempPlayer.north) {
                index = modulo(Directions.NORTH + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.explore(player)
                }
            }
            if (tempPlayer.west) {
                index = modulo(Directions.WEST + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.explore(player)
                }
            }
            if (tempPlayer.south) {
                index = modulo(Directions.SOUTH + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.explore(player)
                }
            }
            if (tempPlayer.east) {
                index = modulo(Directions.EAST + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.explore(player)
                }
            }
            if (player.escalatorTaker) {
                if (elevator != null) {
                    elevator!!.explore(player)
                }
            }
            if (player.pawn.color === Color.ORANGE) {
                if (shortcut != null) {
                    shortcut!!.explore(player)
                }
            }
            if (player.portalTaker && !isInPhaseB) {
                for (tempCase in portalList[player.pawn.color]!!) {
                    tempCase.explore(player)
                }
            }
        }
    }

    fun revert(player: Player) {
        var index: Int // On utilise index pour éviter de devoir réécrire les modulos trop de fois
        val tempPlayer = player.rotate(tile.rotation)
        if (seen) {
            seen = false
            if (tempPlayer.north) {
                index = modulo(Directions.NORTH + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.unexplored()
                    caseList[index]!!.revert(player)
                }
            }
            if (tempPlayer.west) {
                index = modulo(Directions.WEST + tile.rotation, Directions.values().size)
                // Les modulos en Java fonctionnent bizarrement, c'est pour s'assurer d'avoir un truc positif
                if (caseList[index] != null) {
                    caseList[index]!!.unexplored()
                    caseList[index]!!.revert(player)
                }
            }
            if (tempPlayer.south) {
                index = modulo(Directions.SOUTH + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.unexplored()
                    caseList[index]!!.revert(player)
                }
            }
            if (tempPlayer.east) {
                index = modulo(Directions.EAST + tile.rotation, Directions.values().size)
                if (caseList[index] != null) {
                    caseList[index]!!.unexplored()
                    caseList[index]!!.revert(player)
                }
            }
            if (tempPlayer.escalatorTaker) {
                if (elevator != null) {
                    elevator!!.unexplored()
                    elevator!!.revert(player)
                }
            }
            if (player.pawn.color === Color.ORANGE) {
                if (shortcut != null) {
                    shortcut!!.unexplored()
                    shortcut!!.revert(player)
                }
            }
            if (player.portalTaker && !isInPhaseB) {
                for (tempCase in portalList[player.pawn.color]!!) {
                    tempCase.unexplored()
                    tempCase.revert(player)
                }
            }
        }
    }

    fun dispose() {
        redDot!!.remove()
        greenDot!!.remove()
    }

    companion object {
        fun makeElevator(case1: Case, case2: Case) {
            case1.elevator = case2
            case2.elevator = case1
        }

        fun makeShortcut(case1: Case, case2: Case) {
            case1.shortcut = case2
            case2.shortcut = case1
        }

        fun link(case1: Case, case2: Case, direction: Int) {
            // Direction indique la direction de la case 2 par rapport à la case 1
            // Exemple: la case 2 est au nord de la case 1
            // alors direction = 2
            case1.caseList[modulo(direction - case1.tile.rotation, Directions.values().size)] = case2
            case2.caseList[modulo(direction + 2 - case2.tile.rotation, Directions.values().size)] = case1
            case1.exit = false
            case2.exit = false
            if (case1.pawn != null) case1.pawn!!.unlock()
            if (case2.pawn != null) case2.pawn!!.unlock()
        }
    }
}
