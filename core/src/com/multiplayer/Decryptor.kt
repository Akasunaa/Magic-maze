package com.multiplayer


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.menu.MainMenu
import com.tiles.Player
import com.tiles.Queue
import com.utils.Functions
import com.utils.GameScreens
import com.utils.GameScreens.lobbyScreen
import com.utils.Multiplayer
import com.utils.Multiplayer.*
import com.utils.TileAndCases
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.Objects.isNull

class Decryptor {
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
                cyclicBarrier.await() // Pour synchroniser les threads
                println("Client: Processed Answer " + receiver)

            }
            "sending" -> {
                when (receiver) {
                    "Player" -> {
                        println("$suffix: Getting a Player")
                        val inputStream =
                            if (isServer) clientList.getClient(sender).sendingSocket.inputStream
                            else courrier.receivingSocket.inputStream
                        val tempString = BufferedReader(InputStreamReader(inputStream)).readLine()
                        println(tempString)
                        val tempPlayer = mapper.readValue(tempString, Player::class.java)
                        if (isServer) {
                            clientList.getClient(sender).player = tempPlayer
                        } else {
                            playerList.add(tempPlayer)
                            lobbyScreen.addPlayer(tempPlayer)
                        }
                    }
                    "Queue" -> {
                        println("$suffix: Getting a Queue")
                        val inputStream = courrier.receivingSocket.inputStream
                        val tempString = BufferedReader(InputStreamReader(inputStream)).readLine()
                        if (TileAndCases.queue==null) TileAndCases.queue = Queue(tempString)
                        println("Blocking in decryptor queue")
                        cyclicBarrier.await()
                        println("Unlocking in decryptor queue")
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> {
                //cyclicBarrier.await()
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
            "wantToTakePawn" -> {
                val tempPawn = Functions.getPawn(receiver)
                if (isNull(tempPawn.player)) {
                    clientList.getClient(sender).player.takesPawn(tempPawn)
                    clientList.getClient(sender).sendMessage("answer true")
                } else {
                    clientList.getClient(sender).sendMessage("answer false")
                }
            }
            "movingPawn" -> {
                val tempPawn = Functions.getPawn(receiver)
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage(message)
                        }
                    }
                }
                else {
                    try {
                        val x = message.split(' ')[3].toFloat()
                        val y = message.split(' ')[4].toFloat()
                        // Try and do a bit of interpolation here
                        // Edit: it's mostly done i think
                        tempPawn.sendToTarget()
                        tempPawn.setTarget(x, y)
                    } catch (e: Exception) {
                        println("Wrong Numbers Sent")
                    }
                }
            }
            "placePawn" -> {
                val tempPawn = Functions.getPawn(receiver)
                tempPawn.sendToTarget()
                tempPawn.place(tempPawn.position)
            }
            "wantToPlacePawn" -> {
                val tempPawn = Functions.getPawn(receiver)
                val tempCase = Functions.findCase(tempPawn.position)
                if (tempCase?.pawn == null) {
                    clientList.getClient(sender).sendMessage("answer true")
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendMessage("placePawn $receiver")
                        }
                    }
                } else clientList.getClient(sender).sendMessage("answer false")
            }
            "wantToTakeTile" -> {
                if (true) {
                    clientList.getClient(sender).sendMessage("answer true")
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage("$sender isGonnaMoveTile none")
                        }
                    }
                } else {
                    clientList.getClient(sender).sendMessage("answer false")
                    TODO("Vérifier qu'il a le bon rôle quand même, rien d'important pour le moment")
                }
            }
            "isGonnaMoveTile" -> {
                TileAndCases.queue.makingMovable()
            }
            "movingTile" -> {
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
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage(message)
                        }
                    }
                }
                else TileAndCases.queue.rotate(receiver.toInt())
            }
            "placeTile" -> {
                TileAndCases.queue.placeHandleAll(TileAndCases.queue.spritePosition)
            }
            "wantToPlaceTile" -> {
                if (true) { // Je vois pas trop ce qu'il faut demander mais bon
                    clientList.getClient(sender).sendMessage("answer true")
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendMessage("placeTile $receiver")
                        }
                    }
                } else clientList.getClient(sender).sendMessage("answer false")
            }
            "quitting" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendMessage("quitting $sender")
                        }
                    }
                }
                else {
                    lobbyScreen.removePlayer(receiver)
                }
            }
            "stopping" -> {
                GameScreens.game.setScreen(MainMenu(GameScreens.game))
                courrier.killThread()
            }
            "changePseudo" -> {
                if (isServer) {
                    val tempClient = clientList.getClient(sender)
                    tempClient.id = receiver;
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage(message)
                        }
                    }
                }
                else {
                    for (player: Player in playerList) {
                        if (player.pseudo == sender) {
                            player.pseudo = receiver
                            lobbyScreen.hasChangedPseudo = true
                        }
                    }
                }
            }
            "changeAvatar" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (!tempClient.id.equals(sender)) {
                            tempClient.sendClearMessage(message)
                        }
                    }
                }
                else {
                    for (player: Player in playerList) {
                        if (player.pseudo == sender) {
                            player.avatarName = receiver;
                            lobbyScreen.setToUpdateAvatar(sender);
                        }
                    }
                }
            }
            "setAvatar" -> {
                me.avatarName = receiver
                lobbyScreen.setToUpdateAvatar(sender)
            }
            else -> println("$suffix: Action not recognized")
        }
    }

}