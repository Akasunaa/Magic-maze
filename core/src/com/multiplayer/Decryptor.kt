package com.multiplayer

import com.utils.Multiplayer.buttonList
import com.utils.Multiplayer.clientList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

class Decryptor {
    fun decryptMessage(message: String) {
        val sender = message.split(' ')[0]
        val action = message.split(' ')[1]
        val receiver = message.split(' ')[2]
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
                        println(tempString)
                        buttonList.add(Json.decodeFromString<BigButton>(tempString))
                    }
                    "else" -> {
                    }
                }
            }
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