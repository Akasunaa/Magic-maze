package com.multiplayer.button

import com.multiplayer.button.NeededConstants.buttonList
import com.multiplayer.button.NeededConstants.clientList
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.ObjectInputStream

class Decryptor() {
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
                        println("Getting a BigButton")
                        val tempString = BufferedReader(InputStreamReader(clientList.getClient(sender).sendingSocket.inputStream)).readLine()
                        println(tempString)
                        buttonList.add(Json.decodeFromString<BigButton>(tempString))
                    }
                    "else" -> {}
                }
            }
            "nothing" -> {
            }
            else -> println("Nope, not at all")
        }
    }

}