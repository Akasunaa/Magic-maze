package com.mygdx.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.ServerSocketHints
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

// Cette classe existe pour rendre le code plus joli, mais a été écrit en Java à la base
// La supprimer serait futile, puisque le pattern matching de decryptMessage est super important
// Mais en attendant, petit message informatif
// Quand j'ai écrit ce code, seuls quelques dieux et moi le comprennaient
// Maintenant, seuls les dieux le comprennent
// Alors je vous conseille de prier très fort Athéna pour qu'elle vous aide
// Cordialement

class ThreadMaker internal constructor(val port: Int, val button: BigRedButton) {

    fun makeThread (): Thread {
        val thread = Thread {
            val serverSocketHint = ServerSocketHints()
            // 0 ça vuet dire pas de timeout, c'est peut être pas la meilleur idée ?
            // à priori ça fonctionne, s'il y a des problèmes ça vient d'içi en tout cas
            serverSocketHint.acceptTimeout = 0

            // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
            val serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint)

            // Loopity loop
            while (true) {
                // On créé une socket
                val socket = serverSocket.accept(null)

                // On lit la data depuis la socket dans un buffer
                val buffer = BufferedReader(InputStreamReader(socket.inputStream))
                try {
                    /* Affiche la ligne lue
                    Je vais sans doute créer une fonction plus tard,
                    dont le but sera de récupérer le buffer.readLine()
                    et fera les bonnes actions avec ça
                     */
                    decryptMessage(buffer.readLine())
                    button.onClickedRemotely()
                } catch (e: IOException) { //ça c'est les erreurs classique IO
                    e.printStackTrace()
                }
            }
        }
        return thread
    }

    fun decryptMessage(message: String) {
        println(message)
        when (message) {
            "test" -> println("Tout à fait")
            else -> println("Nope, not at all")
        }
    }
}