package com.multiplayer


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.tiles.Player
import com.utils.Functions
import com.utils.Multiplayer
import com.utils.Multiplayer.clientList
import com.utils.Multiplayer.me
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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
                                if (isServer) clientList.getClient(sender).sendingSocket.inputStream
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
                if (isServer) {
                    for (client in clientList.clientList) {
                        client.receivingSocket.outputStream.write((message + " \n").toByteArray())
                    }
                }
                else if (receiver.equals(me.pseudo)) {
                    println("$suffix: I've been Pinged")
                    me.avatar.addAction(Actions.sequence(
                            Actions.color(Color(1f, 0f, 0f, 1f), 0.20f),
                            Actions.color(Color(1f, 1f, 1f, 1f), 0.20f)))
                }
            }
            "selectPawn" -> {
                // Le serveur ne recoit normalement jamais cette commande
                val tempPawn = Functions.getPawn(receiver)
                val inputStream = Multiplayer.courrier.receivingSocket.inputStream
                GlobalScope.launch {
                    while (true) {
                        val message = BufferedReader(InputStreamReader(inputStream)).readLine()
                        if (message.split(' ')[1].equals("placePawn")) {
                            break
                        }

                        else if (!isServer) {
                                val x = message.split(' ')[1].toFloat()
                                val y = message.split(' ')[2].toFloat()
                                tempPawn.setSpritePosition(x, y)
                            }
                            else {
                            }
                        }
                    }
                TODO("Récupérer la couleur du pion sélectionné, et montrer le déplacement aux autres joueurs")
            }
            "wantsToSelectPawn" -> {
                // seul le serveur recoit ce message
                val tempPawn = Functions.getPawn(receiver)
                val inputStream = clientList.getClient(sender).sendingSocket.inputStream
                val coordinates = BufferedReader(InputStreamReader(inputStream)).readLine()
                if (true) {
                    clientList.getClient(sender).receivingSocket.outputStream.write("Server allowed nothing \n".toByteArray())
                    GlobalScope.launch {
                        while (true) {
                            val message = BufferedReader(InputStreamReader(inputStream)).readLine()
                            if (message.split(' ')[1].equals("wantsToPlacePawn")) {
                                for (client in clientList.clientList) {
                                    client.receivingSocket.outputStream.write("$sender PlacePawn $receiver \n".toByteArray())
                                }
                                TODO("Vérifier que le placement est correct")
                            }
                            else {
                                for (client in clientList.clientList) {
                                    if (!client.player.pseudo.equals(sender))
                                        client.receivingSocket.outputStream.write("$message \n".toByteArray())
                                }
                            }
                        }
                    }
                }
                TODO("Le joueur veut selectionner un pion, il faut lui répondre pour voir si c'est possible")
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