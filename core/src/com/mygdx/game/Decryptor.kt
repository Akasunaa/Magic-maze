package com.mygdx.game

class Decryptor  {
    fun decryptMessage(message: String, button: BigRedButton) {
        println(message)
        val sender = message.split(' ')[0]
        val action = message.split(' ')[1]
        //val receiver = message.split(' ')[2]
        when (action) {
            "pressed" -> {
                println("Bouton presse par $sender")
                button.onClickedRemotely()
            }
            else -> println("Nope, not at all")
        }
    }

}