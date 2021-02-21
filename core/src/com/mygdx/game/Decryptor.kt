package com.mygdx.game

import java.io.IOException

class Decryptor(val buttonList: ButtonList, val clientList: ClientList) {
    fun decryptMessage(message: String) {
        val sender = message.split(' ')[0]
        val action = message.split(' ')[1]
        val receiver = message.split(' ')[2]
        when (action) {
            "pressed" -> {
                println("$receiver pressed by $sender")
                try {
                    buttonList.getButton(receiver).onClickedRemotely()
                } catch (e: NullPointerException) {
                    println("Reference not in database")
                }

//                Need to add a listener for everyone
//                for (client in clientList.clientList) {
//                    if (client.ip != sender) {
//                        try {
//                            client.socket.getOutputStream().write(("message").toByteArray())
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                    }
//                }
            }
            "nothing" -> {
            }
            else -> println("Nope, not at all")
        }
    }

}