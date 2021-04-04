package com.multiplayer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.math.Vector2
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
    var answer = false // This will be used to get answers from the server

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur

    init {
        socketHints = SocketHints()
        socketHints.connectTimeout = 0
        socketHints.sendBufferSize = 1024
        socketHints.receiveBufferSize = 1024
        sendingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints)
        sendMessage("connected $ip")
        var waitForIt = BufferedReader(InputStreamReader(sendingSocket.inputStream)).readLine()
        receivingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints)
        waitForIt = BufferedReader(InputStreamReader(receivingSocket.inputStream)).readLine()
        sendObject(Multiplayer.me)
        ClientListener(Multiplayer.key, receivingSocket).startThread()
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
        sendingSocket.getOutputStream().write(((Multiplayer.mapper.writeValueAsString(toSend) + " \n").toByteArray()))
    }

}