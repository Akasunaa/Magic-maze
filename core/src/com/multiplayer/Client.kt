package com.multiplayer

import com.badlogic.gdx.net.Socket
import com.multiplayer.messages.Message
import com.screens.game.board.Player
import java.io.BufferedReader
import java.io.InputStreamReader

class Client(var ip: String, val sendingSocket: Socket, var id: String) {
    constructor (socket: Socket) : this(ip = " ", id = " ", sendingSocket = socket) {
        val waitForIt = BufferedReader(InputStreamReader(socket.inputStream)).readLine().split(' ')
        id = waitForIt[0]
        ip = waitForIt[2]
    }

    private lateinit var receivingSocket: Socket
    fun receiveSocket(socket: Socket) {
        receivingSocket = socket
    }

    lateinit var player: Player

    fun sendMessage(message: Message) {
        receivingSocket.outputStream.write(message.serialize().toByteArray())
    }
}