package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

// Cette classe existe uniquement parce que j'ai aucune idée de comment faire ça en Kotlin
// Si jamais on arrive à le faire, cette classe pourra être supprimée simplement
// Mais en attendant, petit message informatif
// Quand j'ai écrit ce code, seuls quelques dieux et moi le comprennaient
// Maintenant, seuls les dieux le comprennent
// Alors je vous conseille de prier très fort Athéna pour qu'elle vous aide
// Cordialement

public class ThreadMaker {
    Thread thread;
    BigRedButton button;
    Decryptor key = new Decryptor();
    ThreadMaker(final int port, final BigRedButton button) {
        this.button = button;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);

                // Loopity loop
                while (true) {
                    // On créé une socket
                    Socket socket = serverSocket.accept(null);

                    // On lit la data depuis la socket dans un buffer
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    try {
                        /*
                        Affiche la ligne lue
                        Je vais sans doute créer une fonction plus tard,
                        dont le but sera de récupérer le buffer.readLine()
                        et fera les bonnes actions avec ça
                         */
                        String message = (buffer.readLine());
                        System.out.println(message);
                        //key.decryptMessage(message,button);
                        button.onClickedRemotely();
                    } catch (IOException e) { //ça c'est les erreurs classique IO
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
