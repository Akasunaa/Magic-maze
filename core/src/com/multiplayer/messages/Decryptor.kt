package com.multiplayer.messages


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.multiplayer.messages.pawn.MovingPawn
import com.multiplayer.messages.pawn.PlacePawn
import com.multiplayer.messages.tile.GonnaMoveTile
import com.multiplayer.messages.tile.PlaceTile
import com.screens.GameScreens.*
import com.screens.game.board.Player
import com.screens.game.board.Queue
import com.screens.menu.MainMenu
import com.utils.Functions
import com.utils.Multiplayer.*
import com.utils.TileAndCases
import java.util.Objects.isNull

class Decryptor {
    fun decryptMessage(tempMessage: String, isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        val message = mapper.readValue(tempMessage, Message::class.java)
        println((if (message.action=="movingPawn") "    " else "") + suffix + " : " + message.message)
        if (!isServer) {
            message.sendToLog()
            if (message.sender == me.pseudo) {
                return
            }
        }
        when (message.action) {
            "answer" -> {
                courrier.answer = message.target.toBoolean()
                if (cyclicBarrier.parties >= 1) cyclicBarrier.await() // Pour synchroniser les threads
                //else cyclicBarrier.reset()
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
                            //playerList.add(tempPlayer)
                            // On s'en occupe déjà dans lobbyScreen
                            lobbyScreen.addPlayer(tempPlayer)
                        }
                    }
                    "Queue" -> {
                        println("$suffix: Getting a Queue")
                        if (TileAndCases.queue==null) TileAndCases.queue =
                            Queue(message.payload)
                        println("Blocking in Decryptor Queue")
                        cyclicBarrier.await()
                        println("Unblocking in Decryptor Queue")
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> {
//                if (!Multiplayer.isServer) {
//                    // Boh on est plus à un mysticisme prêt
//                    println("Blocking in Decryptor setAndGo")
//                    cyclicBarrier.await()
//                    println("Unblocking in Decryptor setAndGo")
//                }
                // En fait ça servait à rien de mettre une barrière ici étant donné que la
                // Création du courrier se fait de manière linéaire dans le thread principal
            }
            "beginGame" -> {
//                cyclicBarrier.await()
                lobbyScreen.setToPassToGameScreen()
            }
            "assign" -> {
                val tempPlayer = mapper.readValue(message.payload, Player::class.java)
                for (player in playerList) {
                    if (player.pseudo == message.target) {
                        player.setPlayer(tempPlayer)
                    }
                }
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
                    gameScreen.getInterface().needsToPing = true
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
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    tempPawn.setTarget(message.coordinates[0], message.coordinates[1])
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
                val tempCase = Functions.findCase(Vector2(message.coordinates[0],message.coordinates[1]))
                if (tempCase?.pawn == null || tempCase.pawn == tempPawn) {
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    if (message.sender != me.pseudo) clientList.getClient(message.sender).player.dropsPawn(tempPawn)
                    for (tempClient in clientList.clientList) {
                        tempClient.sendMessage(MovingPawn(message.sender,tempPawn,Vector2(message.coordinates[0],message.coordinates[1])))
                        Thread.sleep(10)
                        // Bizarre mais obligatoire
                        tempClient.sendMessage(PlacePawn(message.sender, message.target))
                    }
                } else clientList.getClient(message.sender).sendMessage(Answer(false))
            }
            "wantToTakeTile" -> {
                if (true) {
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList.clientList) {
                        tempClient.sendMessage(GonnaMoveTile(message.sender))
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
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                try {
                    TileAndCases.queue.setSpritePosition(message.coordinates[0],message.coordinates[1])
                } catch (e: Exception) {
                    println("Wrong Numbers Sent")
                }
            }
            "rotateTile" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else TileAndCases.queue.rotate(message.target.toInt())
            }
            "placeTile" -> {
                TileAndCases.queue.place(TileAndCases.queue.spritePosition)
            }
            "wantToPlaceTile" -> {
                if (true) { // Je vois pas trop ce qu'il faut demander mais bon
                    clientList.getClient(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList.clientList) {
                        tempClient.sendMessage(PlaceTile(message.sender, message.target))
                    }
                } else clientList.getClient(message.sender).sendMessage(Answer(false))
            }
            "droppedTile" -> {
                if (isServer) {
                    for (client in clientList.clientList) {
                        client.sendMessage(message)
                    }
                } else TileAndCases.queue.reset()
            }
            "quitting" -> {
                if (isServer) {
                    for (tempClient in clientList.clientList) {
                        if (tempClient.id != message.sender) {
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
                // Je suis mort de rire
                // IntelliJ pense que c'est équivalent à game.screen = MainMenu(game)
                // BWAHAHAHAHAHA
                courrier.killThread()
            }
            "changePseudo" -> {
                if (isServer) {
                    val tempClient = clientList.getClient(message.sender)
                    tempClient.id = message.target
                    for (tempClient in clientList.clientList) {
                        if (tempClient.id != message.sender) {
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
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    for (player: Player in playerList) {
                        if (player.pseudo == message.sender) {
                            player.avatarName = message.target
                            lobbyScreen.setToUpdateAvatar(message.sender)
                        }
                    }
                }
            }
            "setAvatar" -> {
                me.avatarName = message.target
                lobbyScreen.setToUpdateAvatar(me.pseudo)
            }
            "wantsToRestart" -> {
                numberPeopleWantRestart++
                if (numberPeopleWantRestart == 3) {
                    for (client in clientList.clientList) {
                        client.sendMessage(TextMessage("restart"))
                    }
                }
            }
            "restart" -> gameScreen.setToRestart()
            else -> println("$suffix: Action not recognized")
        }
    }

}