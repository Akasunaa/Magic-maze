package com.mygdx.game

class Decryptor (val buttonList: ButtonList) {
    fun decryptMessage(message: String) {
        val sender = message.split(' ')[0]
        val action = message.split(' ')[1]
        val receiver = message.split(' ')[2]
        when (action) {
            "pressed" -> {
                println("$receiver pressed by $sender")
                try {
                    buttonList.getButton(receiver).onClickedRemotely()
                }
                catch (e: NullPointerException) {println("Reference not in database")}
            }
            "nothing" -> {
            }
            else -> println("Nope, not at all")
        }
    }

}