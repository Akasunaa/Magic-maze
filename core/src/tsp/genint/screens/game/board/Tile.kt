package tsp.genint.screens.game.board

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import tsp.genint.screens.GameScreens
import tsp.genint.screens.game.BaseActor
import tsp.genint.screens.game.board.Case.Companion.link
import tsp.genint.utils.*
import java.io.Serializable
import kotlin.math.floor
import kotlin.math.roundToInt

class Tile internal constructor(val number: Int) : Serializable {
    private val path = "Game/Tiles/tile$number.png"

    @Transient
    var sprite: BaseActor? = null
        private set
    var caseList: List<List<Case>>
    private val caseListofCases: List<List<List<Case?>>>
    var rotation = 0 // Indicateur de rotation (dans le sens trigonométrique)
    var exits: BooleanArray
    var exitCases: List<Case>
    private var entrance = Directions.SOUTH
    private var entranceCase: Case? = null

    private var cooldown: Long = 0
    fun startCooldown() {
        cooldown = System.currentTimeMillis()
    }

    var x = 0f
    var y = 0f
    val size = tileSize
    fun updateAll() {
        sprite!!.x = x
        sprite!!.y = y
        updateSize()
    }

    private fun updateSize() {
        sprite!!.setSize(size, size)
        updateAllCases()
    }

    init {
        // On commence par créer le tableau de cases avec des Case à l'intérieur
        val tileArray = getTileArray(number)
        caseList = List(4) { tempY -> List(4) { tempX -> Case(tileArray[tempY][tempX], this) } }
        caseListofCases = List(4) { tempY -> List(4) { tempX -> caseList[tempY][tempX].caseList } }

        // Puis on créé les liaisons entre les cases
        val horizontal = getTileArrayWallHorizontal(number)
        val vertical = getTileArrayWallVertical(number)
        for (ligne in caseList) {
            for (tempCase in ligne) {
                tempCase.getNeighbours(horizontal, vertical)
            }
        }
        exitCases = listOf(caseList[0][1], caseList[1][3], caseList[3][2], caseList[2][0])
        exits = booleanArrayOf(caseList[0][1].exit, caseList[1][3].exit, caseList[3][2].exit, caseList[2][0].exit)

        if (caseList[0][1].entrance) {
            entrance = Directions.SOUTH
            entranceCase = caseList[0][1]
        }
        if (caseList[1][3].entrance) {
            entrance = Directions.EAST
            entranceCase = caseList[1][3]
        }
        if (caseList[2][0].entrance) {
            entrance = Directions.WEST
            entranceCase = caseList[2][0]
        }
        if (caseList[3][2].entrance) {
            entrance = Directions.NORTH
            entranceCase = caseList[3][2]
        }
        complete()
    }

    fun load() { // Obligatoire pour la serialization
        sprite = BaseActor(Texture(path)) // On se charge soit même
        sprite!!.setOrigin(tileSize / 2, tileSize / 2)
        GameScreens.gameScreen.mainStage.addActor(sprite)
        for (i in 0..3) {
            for (j in 0..3) caseList[j][i].load(this, caseListofCases[j][i]) // et on charge toutes les cases
        }
        // Et on rajoute les raccourcis et escalators
        updateAll()
    }

    fun getCaseCoordinates(self: Case): IntArray {
        // C'est juste pour récupérer le x,y d'une case parce que flemme de le faire autrement
        // Sous entendu, x et y valent 0,1,2 ou 3, c'est leur position dans caseList
        val xy = intArrayOf(0, 0)
        for (ligne in caseList) {
            xy[0] = 0
            for (comparator in ligne) {
                if (comparator == self) return xy
                xy[0]++
            }
            xy[1]++
        }
        return xy
    }

    fun getCase(mouseInput: Vector2): Case {
        // Pour récupérer la case située à une certaine coordonée
        var tempX = floor(((mouseInput.x - x - offset) / caseSize).toDouble()).toInt()
        var tempY = floor(((mouseInput.y - y - offset) / caseSize).toDouble()).toInt()
        var buffer: Int
        if (rotation == 1) {
            buffer = tempX
            tempX = tempY
            tempY = 3 - buffer
        }
        if (rotation == 2) {
            tempX = 3 - tempX
            tempY = 3 - tempY
        }
        if (rotation == 3) {
            buffer = tempX
            tempX = 3 - tempY
            tempY = buffer
        }
        return caseList[tempY][tempX]
    }

    fun rotate(angle: Int) {
        rotation += angle
        rotation = modulo(rotation, 4)
        sprite!!.rotateBy((angle * 90).toFloat()) // Dans le sens trigo
        updateAllCases()
    }

    private fun updateAllCases() {
        for (ligne in caseList) {
            for (tempCase in ligne) {
                tempCase.updateCoordinates()
                tempCase.setSize(128 * sprite!!.width / 600)
            }
        }
    }

    private fun complete() {
        // Petite fonction pour rajouter les escalators et les raccourcis
        if (number == 2) Case.makeElevator(caseList[2][0], caseList[0][1])
        if (number == 7) Case.makeElevator(caseList[0][1], caseList[2][2])
        if (number == 10) Case.makeElevator(caseList[2][2], caseList[1][1])
        if (number == 12) {
            Case.makeElevator(caseList[3][1], caseList[2][2])
            Case.makeElevator(caseList[1][0], caseList[0][1])
        }
        if (number == 13) Case.makeShortcut(caseList[2][2], caseList[2][3])
        if (number == 14) {
            Case.makeElevator(caseList[2][0], caseList[1][2])
            Case.makeShortcut(caseList[1][2], caseList[0][2])
        }
        if (number == 15) {
            Case.makeElevator(caseList[3][0], caseList[2][2])
            Case.makeShortcut(caseList[1][0], caseList[1][1])
        }
        if (number == 16) Case.makeShortcut(caseList[1][2], caseList[1][3])
        if (number == 19) Case.makeShortcut(caseList[1][2], caseList[1][3])
        if (number == 24) Case.makeShortcut(caseList[0][2], caseList[0][3])

        // C'est du bon gros nesting mais bon, ça fonctionne, et c'est """compact"""
    }

    fun dispose() {
        sprite!!.remove()
        for (ligne in caseList) {
            for (tempCase in ligne) tempCase.dispose()
        }
    }

    private val neighbouringTiles: Array<Tile?>
        get() {
            val mousePosition = Vector2(sprite!!.x, sprite!!.y)
            mousePosition.sub(origin)
            //        mousePosition.sub(getTileSize( / 2, getTileSize( / 2);
            mousePosition.mul(newBaseInvert)
            val x = mousePosition.x.roundToInt()
            val y = mousePosition.y.roundToInt()
            return arrayOf( //South
                getTile(
                    Vector2(x.toFloat(), (y - 1).toFloat()).mul(newBase).add(origin).add(tileSize / 2, tileSize / 2)
                ),  // East
                getTile(
                    Vector2((x + 1).toFloat(), y.toFloat()).mul(newBase).add(origin).add(tileSize / 2, tileSize / 2)
                ),  // North
                getTile(
                    Vector2(x.toFloat(), (y + 1).toFloat()).mul(newBase).add(origin).add(tileSize / 2, tileSize / 2)
                ),  // West
                getTile(
                    Vector2((x - 1).toFloat(), y.toFloat()).mul(newBase).add(origin).add(tileSize / 2, tileSize / 2)
                )
            )
        }

    private fun noOverlap(): Boolean {
        for (tile in tileList) {
            if (tile != this && tile.x < mouseInput().x && mouseInput().x < tile.x + tileSize && tile.y < mouseInput().y && mouseInput().y < tile.y + tileSize) {
                return false
            }
        }
        return true
    }

    fun canPlaceThere(): Boolean {
        val neighbors = neighbouringTiles
        val direction = modulo(entrance + rotation, Directions.values().size)
        return isValidPlacement(neighbors[direction], direction) && noOverlap()
    }

    fun place() {
        val neighbors = neighbouringTiles
        var direction = modulo(entrance + rotation, Directions.values().size)
        if (isValidPlacement(neighbors[direction], direction) && noOverlap()) {
            link(
                entranceCase!!,
                neighbors[direction]!!.exitCases[modulo(
                    direction + 2 - neighbors[direction]!!.rotation,
                    Directions.values().size
                )],
                direction
            )
            // Puis prise en charge des exits et du link des exits
            for (i in Directions.values().indices) {
                if (exits[i]) { // On commence par vérifier que i est bien une sortie
                    direction = modulo(i + rotation, Directions.values().size)
                    if (neighbors[direction] != null && neighbors[direction]!!.exits[modulo(
                            direction + 2 - neighbors[direction]!!.rotation,
                            Directions.values().size
                        )]
                    ) {
                        // On fait la même chose qu'avant, mais on considère simplement
                        // notre exit comme une entrance
                        link(
                            exitCases[i],
                            neighbors[direction]!!.exitCases[modulo(
                                direction + 2 - neighbors[direction]!!.rotation,
                                Directions.values().size
                            )],
                            direction
                        )
                    }
                }
            }
        }
        for (ligne in caseList) {
            for (tempCase in ligne) {
                if (tempCase.hasPortal) {
                    portalList[tempCase.color]!!.add(tempCase)
                }
            }
        }
    }

    companion object {
        private fun isValidPlacement(tileToJoin: Tile?, direction: Int): Boolean {
            // direction corresponds à la direction de tileToJoin
            // Relativement à la tile que l'on cherche à poser
            // On vérifie simplement qu'il y a bien une case, qu'il s'agit bien d'une sortie
            // Et, le cas échéant, qu'il y a bien un pion de la bonne couleur
            if (tileToJoin != null && tileToJoin.exits[modulo(
                    direction + 2 - tileToJoin.rotation,
                    Directions.values().size
                )]
            ) {
                val exitCase =
                    tileToJoin.exitCases[modulo(direction + 2 - tileToJoin.rotation, Directions.values().size)]
                return exitCase.pawn != null && exitCase.color === exitCase.pawn!!.color
            }
            return false
        }
    }
}
