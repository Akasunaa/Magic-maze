package tsp.genint.multiplayer.messages


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import tsp.genint.screens.GameScreens.gameScreen
import tsp.genint.screens.GameScreens.lobbyScreen
import tsp.genint.screens.game.board.Player
import tsp.genint.screens.game.board.Queue
import tsp.genint.utils.Multiplayer.*
import tsp.genint.utils.findCase
import tsp.genint.utils.getPawn
import tsp.genint.utils.queue
import tsp.genint.utils.quit
import kotlin.math.max

class Decryptor {
    private fun broadcast(message: Message)  = broadcast(message, true)
    private fun broadcast(message: Message, sendBack:Boolean) {
        for (client in clientList){
            if (sendBack || client.id != message.sender)
            client.sendMessage(message)
        }
    }
    fun decryptMessage(tempMessage: String, isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        val message = Message.deserialize(tempMessage)
        if (!isServer) {
            message.sendToLog()
            if (message.sender == me.pseudo) {
                return
            }
        }
        when (message) {
            is SimpleMessage -> {
                when (message) {
                    is SetAndGo -> {
                        // Boh on est plus à un mysticisme prêt
                        println("Blocking in Decryptor setAndGo")
                        cyclicBarrier.await()
                        println("Unblocking in Decryptor setAndGo")
                        // En fait ça servait à rien de mettre une barrière ici étant donné que la
                        // Création du courrier se fait de manière linéaire dans le thread principal
                    }
                    is BeginGame -> {
                        lobbyScreen.setToPassToGameScreen()
                    }

                    is Confirm -> {
                        throw Exception("You're not supposed to decrypt those messages.")
                    }
                    is Quit -> {
                        if (isServer) {
                            for (tempClient in clientList) {
                                if (tempClient.id != message.sender) {
                                    tempClient.sendMessage(message)
                                }
                            }
                            clientList.remove(clientList.getClientFromPseudo(message.sender))
                        }
                        else {
                            lobbyScreen.removePlayer(message.sender)
                        }
                    }
                    is Restart -> gameScreen.setToRestart()
                    is Stop -> quit()

                }
            }
            is Answer -> {
                courrier.answer = message.answer
                if (cyclicBarrier.parties >= 1) cyclicBarrier.await()
                println("$suffix: Processed Answer " + message.answer)
            }
            is PayloadPlayer-> {
                println("$suffix: Getting a Player")
                val tempPlayer = mapper.readValue(message.payload, Player::class.java)
                if (isServer) {
                    clientList.getClientFromPseudo(tempPlayer.pseudo).player = tempPlayer
                } else {
                    //playerList.add(tempPlayer)
                    // On s'en occupe déjà dans lobbyScreen
                    lobbyScreen.addPlayer(tempPlayer)
                    courrier.sendMessage(Confirm())
                }
            }
            is PayloadQueue -> {
                println("$suffix: Getting a Queue")
                if (queue == null) queue = (Queue(message.payload))
                courrier.sendMessage(Confirm())
            }
            is Ping -> {
                if (isServer) {
                    broadcast(message)
                } else if (message.target == me.pseudo) {
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
            is WantsToRestart -> {
                if (isServer) {
                    numberPeopleWantRestart++
                    broadcast(message)
                    if (numberPeopleWantRestart == max((clientList.size - 1), 2)) {
                        broadcast(Restart())
                    }
                } else gameScreen.`interface`.wantsToRestart(message.sender)
            }

            is AssignPlayer -> {
                val tempPlayer = mapper.readValue(message.payload, Player::class.java)
                for (player in playerList) {
                    if (player.pseudo == message.target) {
                        player.setPlayer(tempPlayer)
                        courrier.sendMessage(Confirm())
                        return
                    }
                }
            }
            is ChangePseudo -> {
                if (isServer) {
                    val tempClient = clientList.getClientFromPseudo(message.sender)
                    tempClient.id = message.pseudo
                    broadcast(message, false)
                }
                else {
                    playerList.firstOrNull {it.pseudo == message.sender} ?.pseudo = message.pseudo
                    lobbyScreen.hasChangedPseudo = true

                }
            }
            is ChangeAvatar -> {
                if (isServer) {
                    broadcast(message, false)
                }
                else {
                    playerList.firstOrNull {it.pseudo == message.sender} ?.avatarName = message.avatar
                    lobbyScreen.setToUpdateAvatar(message.sender)
                }
            }
            is SetAvatar -> {
                me.avatarName = message.avatar
                lobbyScreen.setToUpdateAvatar(me.pseudo)
            }
            is PawnMessage -> {
                when (message) {
                    is WantToTakePawn -> {
                        val tempPawn = getPawn(message.color)
                        if (tempPawn.player == null) {
                            clientList.getClientFromPseudo(message.sender).player.takesPawn(tempPawn)
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                        } else {
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                        }
                    }
                    is MovingPawn -> {
                        val tempPawn = getPawn(message.color)
                        if (isServer) {
                            for (tempClient in clientList) {
                                if (tempClient.id != message.sender) {
                                    tempClient.sendMessage(message)
                                }
                            }
                        }
                        else {
                            tempPawn.setTarget(message.coordinates.first, message.coordinates.second)
                        }
                    }
                    is PlacePawn -> {
                        val tempPawn = getPawn(message.color)
                        tempPawn.sendToTarget()
                        tempPawn.place(tempPawn.position)
                    }
                    is WantToPlacePawn -> {
                        val tempPawn = getPawn(message.color)
                        val tempCase = findCase(Vector2(message.coordinates.first, message.coordinates.second))
                        if (tempCase?.pawn == null || tempCase.pawn == tempPawn) {
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                            if (message.sender != me.pseudo) clientList.getClientFromPseudo(message.sender).player.dropsPawn(tempPawn)
                            broadcast(MovingPawn(message.color, message.coordinates).also { it.sender = message.sender })
                            broadcast(PlacePawn(message.color).also {it.sender = message.sender})
                            Thread.sleep(10)
                        } else clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                    }
                }
            }
            is TileMessage -> {
                when (message) {
                    is MovingTile -> {
                        if (isServer) {
                            for (tempClient in clientList) {
                                if (tempClient.id != message.sender) {
                                    tempClient.sendMessage(message)
                                }
                            }
                        }
                        try {
                            queue!!.setSpritePosition(message.coordinates.first, message.coordinates.second)
                        } catch (e: Exception) {
                            println("Wrong Numbers Sent")
                        }
                    }

                    is WantToTakeTile -> {
                        if (true) {
                            // FIXME C'est pas censé être un if true ici mais je sais plus ce que c'est censé être
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                            broadcast(PrepareTileMovement())
                        } else {
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                            TODO("Vérifier qu'il a le bon rôle quand même, rien d'important pour le moment")
                        }
                    }
                    is DroppedTile -> {
                        if (isServer) {
                            for (client in clientList) {
                                client.sendMessage(message)
                            }
                        } else queue!!.reset()
                    }
                    is PlaceTile -> {
                        queue!!.place(queue!!.spritePosition)
                    }

                    is PrepareTileMovement -> {
                        queue!!.makingMovable()
                    }
                    is RotateTile -> {
                        if (isServer) {
                            for (tempClient in clientList) {
                                if (tempClient.id != message.sender) {
                                    tempClient.sendMessage(message)
                                }
                            }
                        } else queue!!.rotate(message.rotation)
                    }
                    is WantToPlaceTile -> {
                        if (true) { // Je vois pas trop ce qu'il faut demander mais bon
                            clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                            broadcast(PlaceTile().also { it.sender = message.sender })
                        } else clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                    }
                }
            }

            //is TextMessage -> System.err.println("You shouldn't have to decrypt text messages.")
        }
    }
}