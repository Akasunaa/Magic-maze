package com.multiplayer

import com.badlogic.gdx.net.Socket
import com.tiles.Player
import java.io.BufferedReader
import java.io.InputStreamReader

class Client(var ip: String, val sendingSocket: Socket, var id: String) {
    constructor (socket: Socket) : this(ip = " ", id = " ", sendingSocket = socket) {
        val waitForIt = BufferedReader(InputStreamReader(socket.inputStream)).readLine().split(' ')
        id = waitForIt[0]
        ip = waitForIt[2]
    }

    lateinit var receivingSocket: Socket
    fun receiveSocket(socket: Socket) {
        receivingSocket = socket
    }

    lateinit var player: Player

    fun sendMessage(message: String) {
        receivingSocket.outputStream.write("server $message \n".toByteArray())
    }

    fun sendClearMessage(message: String) {
        receivingSocket.outputStream.write("$message \n".toByteArray())
    }
}