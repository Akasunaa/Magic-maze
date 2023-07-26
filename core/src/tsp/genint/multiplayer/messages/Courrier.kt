package tsp.genint.multiplayer.messages

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import tsp.genint.multiplayer.ServerNotReachedException
import tsp.genint.screens.game.board.Player
import tsp.genint.utils.Multiplayer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class Courrier(id: String, port: Int, ip: String) {
    private val socket : Socket
    fun resetAnswer() {
        answer = false
    }
    var answer = false // This will be used to get answers from the server

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur
    private val clientListener: ClientListener

    init {
        println("Client : Trying to connect to $ip on port $port")
        val socketHints = SocketHints()
        socketHints.connectTimeout = 500
        socketHints.sendBufferSize = 1024 * 4
        socketHints.receiveBufferSize = 1024 * 4
        try {
            socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints)
            socket.outputStream.write("$id connected $ip\n".toByteArray())
        } catch (e: Exception) {
            throw ServerNotReachedException("Server not found")
        }
        try {
            val waitForIt = BufferedReader(InputStreamReader(socket.getInputStream())).readLine()
            if (waitForIt == "server rejected you") {
                throw ServerNotReachedException("Username is already taken")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        sendObject(Multiplayer.me)
        clientListener = ClientListener(Multiplayer.key, socket)
        clientListener.startThread()
    }

    fun sendMessage(message: Message) {
        try {
            println("Client: Sending $message")
            socket.outputStream.write(message.serialize().toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sendObject(player: Player?) {
        sendMessage(PayloadPlayer(player))
    }

    fun killThread() {
        clientListener.killThread()
    }
}
