package com.multiplayer


import com.tiles.Player
import com.utils.Multiplayer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class Decryptor() {
    fun decryptMessage(message: String,isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        val phrase = message.split(' ')
        val sender = phrase[0]
        val action = phrase[1]
        val receiver = phrase[2]
        println("$suffix: Message Received = $message")
        when (action) {
            "sending" -> {
                when (receiver) {
                    "Player" -> {
                        println("$suffix: Getting a Player")
                        val inputStream =
                                if (isServer) Multiplayer.clientList.getClient(sender).sendingSocket.inputStream
                                else Multiplayer.courrier.receivingSocket.inputStream
                        val tempString = BufferedReader(InputStreamReader(inputStream)).readLine()
                        val tempPlayer = Multiplayer.mapper.readValue(tempString, Player::class.java)
                        if (isServer) {
                            Multiplayer.clientList.getClient(sender).player = tempPlayer
                        }
                        else Multiplayer.playerList.add(tempPlayer)
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> Multiplayer.isServerSetAndGo = true;
            "ping" -> {
                TODO("Récupérer le pseudal du joueur pingé et le pinger")
            }
            "selectPawn" -> {
                TODO("Récupérer la couleur du pion sélectionné, et montrer le déplacement aux autres joueurs")
            }
            "placePawn" -> {
                TODO("Placer le pion là où le joueur l'a placé")
            }
            "selectTile" -> {
                TODO("Récupérer les coordonnées de la souris du joueur qui tiens la tuile, et la déplacer selon ces coordonnées")
            }
            "placeTile" -> {
                TODO("Placer la tuile au bon endroit")
            }


            else -> println("$suffix: Action not recognized")
        }
    }

}