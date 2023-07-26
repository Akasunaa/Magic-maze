package tsp.genint.multiplayer.messages


import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import tsp.genint.multiplayer.messages.pawn.MovingPawn
import tsp.genint.multiplayer.messages.pawn.PlacePawn
import tsp.genint.multiplayer.messages.tile.GonnaMoveTile
import tsp.genint.multiplayer.messages.tile.PlaceTile
import tsp.genint.screens.GameScreens.gameScreen
import tsp.genint.screens.GameScreens.lobbyScreen
import tsp.genint.screens.game.board.Player
import tsp.genint.screens.game.board.Queue
import tsp.genint.utils.Multiplayer.*
import tsp.genint.utils.findCase
import tsp.genint.utils.getPawn
import tsp.genint.utils.queue
import tsp.genint.utils.quit

class Decryptor {
    fun decryptMessage(tempMessage: String, isServer: Boolean) {
        val suffix = if (isServer) "Server" else "Client"
        val message = mapper.readValue(tempMessage, Message::class.java)
        println(message)
        println((if (message.action=="movingPawn" || message.action=="movingTile") "    " else "") + suffix + " : " + message.message)
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
                            clientList.getClientFromPseudo(tempPlayer.pseudo).player = tempPlayer
                        } else {
                            //playerList.add(tempPlayer)
                            // On s'en occupe déjà dans lobbyScreen
                            lobbyScreen.addPlayer(tempPlayer)
                            courrier.sendMessage(Confirm())
                        }
                    }
                    "Queue" -> {
                        println("$suffix: Getting a Queue")
                        if (queue == null) queue = (Queue(message.payload))
//                        println("Blocking in Decryptor Queue")
//                        cyclicBarrier.await()
//                        println("Unblocking in Decryptor Queue")
                        courrier.sendMessage(Confirm())
                    }
                    "else" -> {
                    }
                }
            }
            "setAndGo" -> {
                // Boh on est plus à un mysticisme prêt
                println("Blocking in Decryptor setAndGo")
                cyclicBarrier.await()
                println("Unblocking in Decryptor setAndGo")
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
                        courrier.sendMessage(Confirm())
                        return
                    }
                }
            }
            "ping" -> {
                if (isServer) {
                    for (client in clientList) {
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
                val tempPawn = getPawn(message.target)
                if (tempPawn != null && tempPawn.player == null) {
                    clientList.getClientFromPseudo(message.sender).player.takesPawn(tempPawn)
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                } else {
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                }
            }
            "movingPawn" -> {
                val tempPawn = getPawn(message.target)
                if (isServer) {
                    for (tempClient in clientList) {
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                else {
                    tempPawn!!.setTarget(message.coordinates[0], message.coordinates[1])
                }
            }
            "placePawn" -> {
                val tempPawn = getPawn(message.target)!!
                tempPawn.sendToTarget()
                tempPawn.place(tempPawn.position)
            }
            "wantToPlacePawn" -> {
                val tempPawn = getPawn(message.target)
                val tempCase = findCase(Vector2(message.coordinates[0], message.coordinates[1]))
                if (tempCase?.pawn == null || tempCase.pawn == tempPawn) {
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                    if (message.sender != me.pseudo) clientList.getClientFromPseudo(message.sender).player.dropsPawn(tempPawn)
                    for (tempClient in clientList) {
                        tempClient.sendMessage(
                            MovingPawn(
                                message.sender,
                                tempPawn,
                                Vector2(message.coordinates[0], message.coordinates[1])
                            )
                        )
                        Thread.sleep(10)
                        // Bizarre mais obligatoire
                        tempClient.sendMessage(PlacePawn(message.sender, message.target))
                    }
                } else clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
            }
            "wantToTakeTile" -> {
                if (true) {
                    // FIXME C'est pas censé être un if true ici mais je sais plus ce que c'est censé être
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList) {
                        tempClient.sendMessage(GonnaMoveTile(message.sender))
                    }
                } else {
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
                    TODO("Vérifier qu'il a le bon rôle quand même, rien d'important pour le moment")
                }
            }
            "isGonnaMoveTile" -> {
                queue!!.makingMovable()
            }
            "movingTile" -> {
                if (isServer) {
                    for (tempClient in clientList) {
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                }
                try {
                    queue!!.setSpritePosition(message.coordinates[0], message.coordinates[1])
                } catch (e: Exception) {
                    println("Wrong Numbers Sent")
                }
            }
            "rotateTile" -> {
                if (isServer) {
                    for (tempClient in clientList) {
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(message)
                        }
                    }
                } else queue!!.rotate(message.target.toInt())
            }
            "placeTile" -> {
                queue!!.place(queue!!.spritePosition)
            }
            "wantToPlaceTile" -> {
                if (true) { // Je vois pas trop ce qu'il faut demander mais bon
                    clientList.getClientFromPseudo(message.sender).sendMessage(Answer(true))
                    for (tempClient in clientList) {
                        tempClient.sendMessage(PlaceTile(message.sender, message.target))
                    }
                } else clientList.getClientFromPseudo(message.sender).sendMessage(Answer(false))
            }
            "droppedTile" -> {
                if (isServer) {
                    for (client in clientList) {
                        client.sendMessage(message)
                    }
                } else queue!!.reset()
            }
            "quitting" -> {
                if (isServer) {
                    for (tempClient in clientList) {
                        if (tempClient.id != message.sender) {
                            tempClient.sendMessage(TextMessage("quitting",message.sender))
                        }
                    }
                    clientList.remove(clientList.getClientFromPseudo(message.sender))
                }
                else {
                    lobbyScreen.removePlayer(message.target)
                }
            }
            "stopping" -> {
                quit()
            }
            "changePseudo" -> {
                if (isServer) {
                    val tempClient = clientList.getClientFromPseudo(message.sender)
                    tempClient.id = message.target
                    for (tempClient in clientList) {
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
                    for (tempClient in clientList) {
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
                if (isServer) {
                    numberPeopleWantRestart++
                    for (client in clientList) {
                        client.sendMessage(message)
                    }
                    if (numberPeopleWantRestart == kotlin.math.max((clientList.size - 1), 2)) {
                        for (client in clientList) {
                            client.sendMessage(TextMessage("restart"))
                        }
                    }
                } else gameScreen.`interface`.wantsToRestart(message.sender)
            }
            "restart" -> gameScreen.setToRestart()
            else -> println("$suffix: Action not recognized")
        }
    }
}