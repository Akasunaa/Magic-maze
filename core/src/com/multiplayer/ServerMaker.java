package com.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
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
    ServerSocket serverSocket;

    public ServerMaker(final int port, final ClientList clientList) {
        //clientList = cL;
        //this.key = key;
        thread = new Thread(new Runnable() {
            private void sleep() {
                int sleepTime = 30;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // No timeout
                serverSocketHint.acceptTimeout = 50;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);
                Socket socket;
                Client client;
                BufferedReader buffer; // Le Buffer
                InputStream inputStream; // Et l'InputStream
                String tempString=null;

                // Première boucle pour récupérer tous les clients
                System.out.println("Server: Beginning first loop");
                while (!clientList.isFull()) {
                    socket = serverSocket.accept(null); // On récupère une socket qui demande une connection
                    System.out.println("accepted");
                    client = new Client(socket);
                    if (!clientList.isIn(client)) {
                        clientList.add(client); // On vérifie si le client n'est pas dans la liste, et on l'ajoute
                        System.out.println("Server: Client added: " + client.getIp() + " as " + client.getId());
                        // On lui demande de renvoyer une nouvelle socket
                        try {
                            client.getSendingSocket().getOutputStream().write(("Server send ReceivingSocket \n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // On dit que la nouvelle socket c'est à lui (et on espère que c'est bien le cas
                        //TODO Vérifier que c'est bien le cas
                        socket = serverSocket.accept(null);
                        client.receiveSocket(socket);
                        // Maintenant on récupère les avatars des joueurs
                        client.sendMessage("send Player");
                        socket = client.getSendingSocket(); // On prends la socket du client
                        inputStream = socket.getInputStream(); // On prends l'inputStream
                        try {
                            // On lit la data depuis la socket dans un buffer
                            buffer = new BufferedReader(new InputStreamReader(inputStream));
                            //Et on la décrypte
                            key.decryptMessage(buffer.readLine(), true);
                        } catch (IOException e) { //Standard Procedure for dealing with Sockets
                            e.printStackTrace();
                        }
                        // Maintenant on renvois le joueur à tout le monde
                        // Et également on envois tout le monde au joueur
                        try {
                            tempString = Multiplayer.mapper.writeValueAsString(client.player);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        for (Client tempClient : Multiplayer.clientList.clientList) {
                            if (!tempClient.getId().equals(client.getId())) {
                                sleep();
                                tempClient.sendClearMessage(client.getId() + " sending Player");
                                sleep();
                                tempClient.sendClearMessage(tempString);
                                System.out.println("Server: Redistributing the Player of " + client.getId() + " to " + tempClient.getId());
                                sleep();
                                client.sendClearMessage(tempClient.getId() + " sending Player");
                                sleep();
                                try {
                                    client.sendClearMessage(Multiplayer.mapper.writeValueAsString(tempClient.player));
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                                // Très chiant ces try catch partout mais bon
                                System.out.println("Server: Redistributing the Player of " + tempClient.getId() + " to " + client.getId());
                                sleep();
                            }
                        }
                    }
                    else {
                        client.sendMessage("rejected you");
                    }
                }

                //sleep();
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

    public void killThread() {
        System.out.println("Server: Killing Server");
        thread.stop();
        serverSocket.dispose();
    }
}
