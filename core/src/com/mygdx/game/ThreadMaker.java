package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
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
    //BigRedButton button;
    Decryptor key;
    ClientList clientList;
    ThreadMaker(final int port, final BigButton button, final ClientList cL, final ButtonList bL) {
        clientList = cL;
        key = new Decryptor(bL);
        //this.button = button;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // 0 means no timeout.  Probably not the greatest idea in production!
                serverSocketHint.acceptTimeout = 0;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);
                Socket socket;
                Client client;

                // Première boucle pour récupérer tous les clients
                while (!clientList.isFull()) {
                    socket = serverSocket.accept(null); // On récupère une socket qui demande une connection
                    client = new Client(socket);
                    if (! clientList.isIn(client)) {
                        clientList.addClient(client); // On vérifie si le client n'est pas dans la liste, et on l'ajoute
                        System.out.println("Client added: " + client.getIp());
                    }
                }

                BufferedReader buffer; // Le Buffer
                InputStream inputStream; // Et l'InputStream
                // Deuxième boucle pour regarder les actions des clients
                while (true) {
                    for (Client tempClient : clientList.clientList) {
                        socket = tempClient.getSocket(); // On prends la socket du client
                        inputStream = socket.getInputStream();
                        try {
                            if (inputStream.available() != 0) {
                                // On lit la data depuis la socket dans un buffer
                                buffer = new BufferedReader(new InputStreamReader(inputStream));
                                //Et on la décrypte
                                key.decryptMessage(buffer.readLine());
                            }
                        } catch (IOException e) { //Standard Procedure for dealing with Sockets
                            e.printStackTrace();
                        }
                    }

                }
            }
        });
    }
}
