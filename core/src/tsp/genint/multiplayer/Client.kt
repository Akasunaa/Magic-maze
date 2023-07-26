package tsp.genint.multiplayer

import com.badlogic.gdx.net.Socket
import tsp.genint.multiplayer.messages.Message
import tsp.genint.screens.game.board.Player
import java.io.BufferedReader
import java.io.InputStreamReader

class Client(var ip: String, val socket: Socket, var id: String) {
    constructor (socket: Socket) : this(ip = " ", id = " ", socket = socket) {
        val waitForIt = BufferedReader(InputStreamReader(socket.inputStream)).readLine().split(' ')
        id = waitForIt[0]
        ip = waitForIt[2]
    }

    lateinit var player: Player

    fun sendMessage(message: Message) {
        socket.outputStream.write(message.serialize().toByteArray())
    }
}