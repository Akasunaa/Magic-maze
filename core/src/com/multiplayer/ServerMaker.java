package com.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.utils.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.utils.Multiplayer.key;


// Cette classe existe uniquement parce que j'ai aucune idée de comment faire ça en Kotlin
// Si jamais on arrive à le faire, cette classe pourra être supprimée simplement
// Mais en attendant, petit message informatif
// Quand j'ai écrit ce code, seuls quelques dieux et moi le comprennaient
// Maintenant, seuls les dieux le comprennent
// Alors je vous conseille de prier très fort Athéna pour qu'elle vous aide
// Cordialement

public class ServerMaker {
    Thread thread;

    public ServerMaker(final int port, final ClientList clientList) {
        //clientList = cL;
        //this.key = key;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // No timeout
                serverSocketHint.acceptTimeout = 0;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                ServerSocket serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);
                Socket socket;
                Client client;

                // Première boucle pour récupérer tous les clients
                System.out.println("Server: Beginning first loop");
                while (!clientList.isFull()) {
                    socket = serverSocket.accept(null); // On récupère une socket qui demande une connection
                    client = new Client(socket);
                    if (!clientList.isIn(client)) {
                        clientList.add(client); // On vérifie si le client n'est pas dans la liste, et on l'ajoute
                        System.out.println("Server: Client added: " + client.getIp() + " as " + client.getId());
                    }
                }

                // Deuxième boucle pour bien mettre en place les sockets
                System.out.println("Server: Beginning second loop");
                for (Client tempClient : clientList.clientList) {
                    try {
                        tempClient.getSendingSocket().getOutputStream().write(("Server send ReceivingSocket \n").getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = serverSocket.accept(null);
                    tempClient.receiveSocket(socket);
                }

                System.out.println("Server: Beginning third loop");
                BufferedReader buffer; // Le Buffer
                InputStream inputStream; // Et l'InputStream
                // Une boucle pour récupérer les avatars, rien à changer par rapport à une boucle normale
                for (Client tempClient : clientList.clientList) {
                    try {
                        tempClient.receivingSocket.getOutputStream().write("Server send Player \n".getBytes());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    socket = tempClient.getSendingSocket(); // On prends la socket du client
                    inputStream = socket.getInputStream();
                    try {
                        // On lit la data depuis la socket dans un buffer
                        buffer = new BufferedReader(new InputStreamReader(inputStream));
                        //Et on la décrypte
                        key.decryptMessage(buffer.readLine(), true);
                    } catch (IOException e) { //Standard Procedure for dealing with Sockets
                        e.printStackTrace();
                    }
                }
                int sleepTime = 30;

                // Une boucle pour redistribuer les players
                for (Client tempClient : Multiplayer.clientList.clientList) {
                    String tempString = null;
                    try {
                        tempString = Multiplayer.mapper.writeValueAsString(tempClient.player);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    for (Client tempTempClient : Multiplayer.clientList.clientList) {
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            tempTempClient.receivingSocket.getOutputStream().write((tempClient.getId() + " sending Player \n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            tempTempClient.receivingSocket.getOutputStream().write((tempString + " \n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Server: Redistributing the Player of " + tempClient.getId() + " to " + tempTempClient.getId());
                    }
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Je... ne suis pas sûr de pourquoi je suis obligé de faire ça
                // C'est très bizarre, je pense qu'il doit y avoir des histoires de voyage temporel
                // Ou un truc du genre
                // Parce que certains messages se retrouvent envoyés après ceux qui devraient l'être avant...

                for (Client tempClient : clientList.clientList) {
                    try {
                        tempClient.receivingSocket.getOutputStream().write(("Server setAndGo nothing \n").getBytes());
                        System.out.println("Server: sent setAndGo to "+ tempClient.getId());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                // On signal que le serveur est prêt et que normalement les clients ont tout reçu

                System.out.println("Server: Beginning last loop");
                while (true) {
                    for (Client tempClient : clientList.clientList) {
                        socket = tempClient.getSendingSocket(); // On prends la socket du client
                        inputStream = socket.getInputStream();
                        try {
                            if (inputStream.available() != 0) {
                                // On lit la data depuis la socket dans un buffer
                                buffer = new BufferedReader(new InputStreamReader(inputStream));
                                //Et on la décrypte
                                key.decryptMessage(buffer.readLine(), true);
                            }
                        } catch (IOException e) { //Standard Procedure for dealing with Sockets
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void startThread() {
        thread.start();
    }
}
