package com.multiplayer


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.tiles.Player
import com.utils.Functions
import com.utils.Multiplayer
import com.utils.Multiplayer.*
import com.utils.TileAndCases
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.Objects.isNull

class Decryptor() {
    fun decryptMessage(message: String, isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        println("$suffix: Message Received = $message")
        val phrase = message.split(' ')
        val sender = phrase[0]
        val action = phrase[1]
        val receiver = phrase[2]
        when (action) {
            "answer" -> {
                courrier.answer = receiver.toBoolean()
            }
            "sending" -> {
                when (receiver) {
                    "Player" -> {
                        println("$suffix: Getting a Player")
                        val inputStream =
                            if (isServer) clientList.getClient(sender).sendingSocket.inputStream
                            else courrier.receivingSocket.inputStream
                        val tempString = BufferedReader(InputStreamReader(inputStream)).readLine()
                        val tempPlayer = mapper.readValue(tempString, Player::class.java)
                        if (isServer) {
                            clientList.getClient(sender).player = tempPlayer
                        } else playerList.add(tempPlayer)
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> Multiplayer.isServerSetAndGo = true;
            "ping" -> {
                if (isServer) {
                    for (client in clientList.clientList) {
                        client.sendClearMessage(message)
                    }
                } else if (receiver.equals(me.pseudo)) {
                    println("$suffix: I've been Pinged")
                    me.avatar.addAction(
                        Actions.sequence(
                            Actions.color(Color(1f, 0f, 0f, 1f), 0.20f),
                            Actions.color(Color(1f, 1f, 1f, 1f), 0.20f)
                        )
                    )
                }
            }
            "wantToTake" -> {
                val tempPawn = Functions.getPawn(receiver)
                if (isNull(tempPawn.player)) {
                    clientList.getClient(sender).player.takesPawn(tempPawn)
                    clientList.getClient(sender).sendMessage("answer true")
                } else {
                    clientList.getClient(sender).sendMessage("answer false")
                }
            }
            "moving" -> {
                val tempPawn = Functions.getPawn(receiver)
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage(message)
                        }
                    }
                }
                try {
                    val x = message.split(' ')[2].toFloat()
                    val y = message.split(' ')[3].toFloat()
                    // Try and to a bit of interpolation here
                    tempPawn.setSpritePosition(x, y)
                } catch (e: Exception) {
                    println("Wrong Numbers Sent")
                }
            }
            "wantToPlace" -> {
                val tempPawn = Functions.getPawn(receiver)
                if (Functions.findCase(tempPawn.position).pawn.equals(null)) {
                    clientList.getClient(sender).sendMessage("answer true")
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendMessage("place $receiver")
                        }
                    }
                } else clientList.getClient(sender).sendMessage("answer false")
            }
            else -> println("$suffix: Action not recognized")
        }
    }

}