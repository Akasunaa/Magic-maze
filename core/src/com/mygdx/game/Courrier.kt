package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.net.SocketHints
import java.io.IOException

// Le but de cette classe est d'éviter de recréer une socket à chaque fois
// Et de faciliter l'envois de messages.

class Courrier(val id: String, port: Int, ip: String){
    var socket: Socket
    val socketHints: SocketHints
    init {
        socketHints = SocketHints()
        socketHints.connectTimeout = 4000
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP,ip,port,socketHints)
        //sendMessage("connected $ip")
    }


    fun sendMessage(message: String) {
        try {
            //println("Sending message to server")
            socket.getOutputStream().write(("$id $message \n").toByteArray())
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}