package com.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import com.tiles.Player
import com.utils.Multiplayer
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

// Le but de cette classe est d'éviter de recréer une socket à chaque fois
// Et de faciliter l'envois de messages.

class Courrier(val id: String, port: Int, ip: String) {
    val sendingSocket: Socket
    val receivingSocket: Socket
    val socketHints: SocketHints

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur

    init {
        socketHints = SocketHints()
        socketHints.connectTimeout = 0
        sendingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints)
        sendMessage("connected $ip")
        val waitForIt = BufferedReader(InputStreamReader(sendingSocket.inputStream)).readLine()
        receivingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints)
        sendObject(Multiplayer.me)
        SimpleThread(receivingSocket).start()
    }

    fun sendMessage(message: String) {
        try {
            //println("Client: Sending $message")
            sendingSocket.getOutputStream().write(("$id $message \n").toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun sendObject(toSend: BigButton) {
        sendMessage("sending BigButton")
        //ObjectOutputStream(sendingSocket.outputStream).write(toSend.serialize().toByteArray())
        sendingSocket.getOutputStream().write(((toSend.serialize() + "\n").toByteArray()))
    }

    fun sendObject(toSend: Player) {
        sendMessage("sending Player")
        //ObjectOutputStream(sendingSocket.outputStream).write(toSend.serialize().toByteArray())
        sendingSocket.getOutputStream().write(((Multiplayer.mapper.writeValueAsString(toSend) + "\n").toByteArray()))
    }

}


class SimpleThread(val socket: Socket) : Thread() {
    override fun run() {
        val inputStream = socket.getInputStream()
        println("yes")
        while (true) {
            try {
                if (inputStream.available() != 0) {
                    // On lit la data depuis la socket dans un buffer
                    val buffer = BufferedReader(InputStreamReader(inputStream))
                    //Et on la décrypte
                    Multiplayer.key.decryptMessage(buffer.readLine())
                }
            } catch (e: IOException) { //Standard Procedure for dealing with Sockets
                e.printStackTrace()
            }
        }
    }
}

