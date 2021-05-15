package com.multiplayer.messages


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.menu.MainMenu
import com.multiplayer.messages.pawn.PlacePawn
import com.multiplayer.messages.tile.GonnaMoveTile
import com.multiplayer.messages.tile.PlaceTile
import com.tiles.Player
import com.tiles.Queue
import com.utils.Functions
import com.utils.GameScreens.*
import com.utils.Multiplayer
import com.utils.Multiplayer.*
import com.utils.TileAndCases
import java.util.Objects.isNull

class Decryptor {
    fun decryptMessage(tempMessage: String, isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        val message = mapper.readValue(tempMessage, Message::class.java)
        println(suffix + " : " + message.getMessage())
        when (message.action) {
            "answer" -> {
                courrier.answer = message.target.toBoolean()
                cyclicBarrier.await() // Pour synchroniser les threads
                println("$suffix: Processed Answer " + message.target)

            }
            "sending" -> {
                when (message.target) {
                    "Player" -> {
                        println("$suffix: Getting a Player")
                        val tempPlayer = mapper.readValue(message.payload, Player::class.java)
                        if (isServer) {
                            clientList.getClient(tempPlayer.pseudo).player = tempPlayer
                        } else {
                            playerList.add(tempPlayer)
                            lobbyScreen.addPlayer(tempPlayer)
                        }
                    }
                    "Queue" -> {
                        println("$suffix: Getting a Queue")
                        if (TileAndCases.queue==null) TileAndCases.queue = Queue(message.payload)
                        println("Blocking in Decryptor Queue")
                        cyclicBarrier.await()
                        println("Unblocking in Decryptor Queue")
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> {
                if (!Multiplayer.isServer) {
                    // Boh on est plus à un mysticisme prêt
                    println("Blocking in Decryptor setAndGo")
                    cyclicBarrier.await()
                    println("Unblocking in Decryptor setAndGo")
                }
                // En fait ça servait à rien de mettre une barrière ici étant donné que la
                // Création du courrier se fait de manière linéaire dans le thread principal
            }
            "beginGame" -> {
//                cyclicBarrier.await()
                if (!Multiplayer.isServer) lobbyScreen.passToGameScreen()
            }
            "ping" -> {
                if (isServer) {
                    for (client in clientList.clientList) {
                        client.sendMessage(message)
                    }
                } else if (message.target.equals(me.pseudo)) {
                    println("$suffix: I've been Pinged")
                    me.avatar.addAction(
                        Actions.sequence(
                            Actions.color(Color(1f, 0f, 0f, 1f), 0.20f),
                            Actions.color(Color(1f, 1f, 1f, 1f), 0.20f)
                        )
                    )
                }
            }
            "wantToTakePawn" -> {
                val tempPawn = Functions.getPawn(message.target)
                if (isNull(tempPawn.player)) {
                    clientList.getClient(message.sender).player.takesPawn(tempPawn)
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                } else {
                    clientList.getClient(message.sender).sendMessage(Answer(false))
                }
            }
            "movingPawn" -> {
                val tempPawn = Functions.getPawn(message.target)
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    val x = message.payload.split(' ')[3].toFloat()
                    val y = message.payload.split(' ')[4].toFloat()
                    tempPawn.setTarget(x, y)
//                    try {
//                        // Try and do a bit of interpolation here
//                        // Edit: it's mostly done i think
//                        tempPawn.sendToTarget()
//                    } catch (e: Exception) {
//                        println("Wrong Numbers Sent")
//                    }
                // Des reliques du vieux code, je les garde au kazoo
                // j'ai de nouveau besoin de faire de l'interpolation
                }
            }
            "placePawn" -> {
                val tempPawn = Functions.getPawn(message.target)
                tempPawn.sendToTarget()
                tempPawn.place(tempPawn.position)
            }
            "wantToPlacePawn" -> {
                val tempPawn = Functions.getPawn(message.target)
                val tempCase = Functions.findCase(tempPawn.position)
                if (tempCase?.pawn == null) {
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(PlacePawn(message.target))
                        }
                    }
                } else clientList.getClient(message.sender).sendMessage(Answer(false))
            }
            "wantToTakeTile" -> {
                if (true) {
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(GonnaMoveTile(message.sender))
                        }
                    }
                } else {
                    clientList.getClient(message.sender).sendMessage(Answer(false))
                    TODO("Vérifier qu'il a le bon rôle quand même, rien d'important pour le moment")
                }
            }
            "isGonnaMoveTile" -> {
                TileAndCases.queue.makingMovable()
            }
            "movingTile" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                try {
                    val x = message.payload.split(' ')[2].toFloat()
                    val y = message.payload.split(' ')[3].toFloat()
                    // There's no need for a specification of what tile we're sending
                    // Try and do a bit of interpolation here
                    // Edit: it's mostly done i think
                    TileAndCases.queue.setSpritePosition(x,y)
                } catch (e: Exception) {
                    println("Wrong Numbers Sent")
                }
            }
            "rotateTile" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else TileAndCases.queue.rotate(message.target.toInt())
            }
            "placeTile" -> {
                TileAndCases.queue.placeHandleAll(TileAndCases.queue.spritePosition)
            }
            "wantToPlaceTile" -> {
                if (true) { // Je vois pas trop ce qu'il faut demander mais bon
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(PlaceTile(message.sender, message.target))
                        }
                    }
                } else clientList.getClient(message.sender).sendMessage(Answer(false))
            }
            "quitting" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(TextMessage("quitting",message.sender))
                        }
                    }
                    clientList.remove(clientList.getClient(message.sender))
                }
                else {
                    lobbyScreen.removePlayer(message.target)
                }
            }
            "stopping" -> {
                game.setScreen(MainMenu(game))
                courrier.killThread()
            }
            "changePseudo" -> {
                if (isServer) {
                    val tempClient = clientList.getClient(message.sender)
                    tempClient.id = message.target
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            println(tempClient.id)
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    for (player: Player in playerList) {
                        if (player.pseudo == message.sender) {
                            player.pseudo = message.target
                            lobbyScreen.hasChangedPseudo = true
                        }
                    }
                }
            }
            "changeAvatar" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(message.sender)) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    for (player: Player in playerList) {
                        if (player.pseudo == message.sender) {
                            player.avatarName = message.target
                            lobbyScreen.setToUpdateAvatar(message.sender);
                        }
                    }
                }
            }
            "setAvatar" -> {
                me.avatarName = message.target
                lobbyScreen.setToUpdateAvatar(me.pseudo)
            }
            else -> println("$suffix: Action not recognized")
        }
    }

}