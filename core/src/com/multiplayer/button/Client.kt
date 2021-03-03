package com.multiplayer.button

import com.badlogic.gdx.net.Socket
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
}