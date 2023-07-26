package tsp.genint.multiplayer.messages

import com.badlogic.gdx.math.Vector2
import tsp.genint.screens.game.board.Player
import tsp.genint.screens.game.board.Queue
import tsp.genint.screens.game.hud.GameInterface
import tsp.genint.utils.Color
import tsp.genint.utils.Multiplayer
import java.io.*
import kotlin.reflect.KClass
import kotlin.reflect.cast

sealed class Message (
    var sender: String
): Serializable {
    constructor() : this(Multiplayer.me.pseudo)
    open val logMessage: String ? = null

    override fun toString() = "Message {from:$sender}"

    private class MessageWrapper(val type: Class<Message>, val byteArray: ByteArray): Serializable
    fun serialize(): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(this)
        objectOutputStream.flush()
        val result = byteArrayOutputStream.toByteArray()
        byteArrayOutputStream.close()
        objectOutputStream.close()
        return Multiplayer.mapper.writeValueAsString(MessageWrapper(javaClass, result)) + "\n"
    }

    companion object {
        fun deserialize(json: String): Message {
            val wrapper = Multiplayer.mapper.readValue(json, MessageWrapper::class.java)
            val byteArrayInputStream = ByteArrayInputStream(wrapper.byteArray)
            val objectInput = ObjectInputStream(byteArrayInputStream)
            val result = wrapper.type.cast(objectInput.readObject());
            objectInput.close()
            byteArrayInputStream.close()
            return result
        }
    }

    fun asServer() = also { sender = "Server" }
    fun sendToLog() {
        if (logMessage != null)
            GameInterface.logs.newMessage(logMessage)
    }
}
class Answer(val answer: Boolean) : Message("Server")
class ChangePseudo(val pseudo: String) : Message()
class SetAvatar(val avatar: String) : Message()
class ChangeAvatar(val avatar: String): Message()

/**
 * A special class of messages that don't have any special characteristic. See this as kind of an enum.
 */
sealed class SimpleMessage: Message()

class Confirm: SimpleMessage()
class SetAndGo: SimpleMessage()
class BeginGame: SimpleMessage()
class Quit: SimpleMessage()
class Stop: SimpleMessage()
class Restart: SimpleMessage()

class WantsToRestart: Message() {
    override val logMessage = "$sender wants to restart"
}
sealed class TargetedMessage(sender: String, val target: String): Message(sender) {
    constructor(target: String) : this(Multiplayer.me.pseudo,target)
}

/**
 * Messages about Pawn movement and all that.
 */
sealed class PawnMessage(val color: Color) : Message()
class WantToTakePawn(color: Color): PawnMessage(color)
class MovingPawn(color: Color, val coordinates: Pair<Float, Float>): PawnMessage(color) {
    constructor(color: Color, vector: Vector2) : this(color, vector.x to vector.y)
}
class WantToPlacePawn(color: Color, val coordinates: Pair<Float, Float>): PawnMessage(color) {
    constructor(color: Color, vector: Vector2) : this(color, vector.x to vector.y)
}
class PlacePawn(color: Color): PawnMessage(color) {
    override val logMessage = "$sender a posé le pion $color"
}
/**
 * Small class for every message related to Tiles.
 */
sealed class TileMessage() : Message()
class DroppedTile: TileMessage()
class MovingTile(val coordinates: Pair<Float, Float>): TileMessage() {
    constructor(vector: Vector2) : this(vector.x to vector.y)
}
class RotateTile(val rotation: Int): TileMessage()
class WantToPlaceTile() : TileMessage()
class PrepareTileMovement(): TileMessage() {
    override val logMessage = "$sender a pris une tuile"
}
class WantToTakeTile: TileMessage()
class PlaceTile: TileMessage() {
    override val logMessage = "$sender a posé une tuile"
}

class Ping(target: String): TargetedMessage(target) {
    override val logMessage = "$sender a pingé $target"
}
sealed class PayloadMessage(sender: String, target: String, val payload: String): TargetedMessage(sender, target) {
    constructor(target: String, payload:String) : this(Multiplayer.me.pseudo,target, payload)
}
class AssignPlayer(player: Player, target: String):
    PayloadMessage("Server", target, Multiplayer.mapper.writeValueAsString(player))
class PayloadPlayer(player: Player):
    PayloadMessage(Multiplayer.me.pseudo, "Player", Multiplayer.mapper.writeValueAsString(player))

class PayloadQueue(queue: Queue):
    PayloadMessage(Multiplayer.me.pseudo, "Queue", queue.serialize())
