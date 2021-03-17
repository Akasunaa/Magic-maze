package com.multiplayer


import com.tiles.Player
import com.utils.Multiplayer
import com.utils.Multiplayer.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class Decryptor {
    fun decryptMessage(message: String) {
        val phrase = message.split(' ')
        val sender = phrase[0]
        val action = phrase[1]
        val receiver = phrase[2]
        println(message)
        when (action) {
            "pressed" -> {
                println("$receiver pressed by $sender")
                try {
                    buttonList.getButton(receiver).onClickedRemotely()
                } catch (e: NullPointerException) {
                    println("Reference not in database")
                }
            }
            "sending" -> {
                when (receiver) {
                    "BigButton" -> {
                        println("Server : Getting a BigButton")
                        val tempString = BufferedReader(InputStreamReader(clientList.getClient(sender).sendingSocket.inputStream)).readLine()
                        //println(tempString)
                        buttonList.add(Json.decodeFromString<BigButton>(tempString))
                    }
                    "Player" -> {
                        println("Server : Getting a Player")
                        val tempString = BufferedReader(InputStreamReader(clientList.getClient(sender).sendingSocket.inputStream)).readLine()
                        //println(tempString)
                        if (isServer) {
                            clientList.getClient(sender).player = mapper.readValue(tempString, Player::class.java)
                            for (client in clientList.clientList) {
                                client.receivingSocket.getOutputStream().write("Server sending Player".toByteArray())
                                client.receivingSocket.getOutputStream().write(tempString.toByteArray())                            }
                        }
                        playerList.add(mapper.readValue(tempString, Player::class.java))
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> isServerSetAndGo = true;
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


            else -> println("Server : Action not recognized")
        }
    }

}