package com.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.menu.BaseScreen;
import com.menu.MainMenu;
import com.multiplayer.messages.PayloadPlayer;
import com.multiplayer.messages.TextMessage;
import com.tiles.Player;
import com.utils.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.utils.Multiplayer.*;


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
    private boolean isSetAndGo = false;
    int port;

    public ServerMaker(final int port, final ClientList clientList) {
        //clientList = cL;
        //this.key = key;
        this.port = port;
        thread = new Thread(new Runnable() {
            private void catchMessage() {
                // Une fonction qui choppe tous les messages de tout le monde
                for (Client tempClient : clientList.clientList) {
                    Socket socket = tempClient.getSendingSocket(); // On prends la socket du client
                    InputStream inputStream = socket.getInputStream();
                    try {
                        if (inputStream.available() != 0) {
                            // On lit la data depuis la socket dans un buffer
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                            //Et on la décrypte
                            key.decryptMessage(buffer.readLine(), true);
                        }
                    } catch (IOException e) { //Standard Procedure for dealing with Sockets
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void run() {
                while (!isSetAndGo) {
                    catchMessage();
                    clientList.addBackup();
                    clientList.removeBackup();
//                    int sleepTime = 30;
//                    try {
//                        Thread.sleep(sleepTime);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }

                //sleep();
                // Je... ne suis pas sûr de pourquoi je suis obligé de faire ça
                // C'est très bizarre, je pense qu'il doit y avoir des histoires de voyage temporel
                // Ou un truc du genre
                // Parce que certains messages se retrouvent envoyés après ceux qui devraient l'être avant...

                for (Client tempClient : clientList.clientList) {
                    tempClient.sendMessage(new TextMessage("beginGame"));
                    System.out.println("Server: sent beginGame to "+ tempClient.getId());
                }
                // On signal que le serveur est prêt et que normalement les clients ont tout reçu

                System.out.println("Server: Beginning last loop");
                while (isRunning) {
                    catchMessage();
                }
            }
        });
    }
    private boolean isInLobby = false;
    private boolean isRunning = false;
    private void startLobby() {
        new Thread(new Runnable() {
            Socket socket;
            Client client;
            final ClientList clientList = Multiplayer.clientList;
            InputStream inputStream;
            String tempString=null;

            private void sleep() {
                int sleepTime = 30;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            private String findFreeAvatar() {
                for (String avatarName : BaseScreen.animalNames) {
                    boolean isUsed = false;
                    for (Player player : playerList) {
                        if (player.avatarName.equals(avatarName)) {
                            isUsed = true;
                        }
                    }
                    if (!isUsed) {
                        return avatarName;
                    }
                }
                // Nom au cas où, mais ça ne devrait jamais arriver
                return "Robert";
            }

            @Override
            public void run() {
                ServerSocketHints serverSocketHint = new ServerSocketHints();
                // No timeout
                serverSocketHint.acceptTimeout = 0;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);
                System.out.println("Created serverSocket");
                while (isInLobby) {
                    System.out.println("looking for players");
                    socket = serverSocket.accept(null); // On récupère une socket qui demande une connection
                    if (!isInLobby) break;
                    // Petite ligne qui me perme de quitter automatiquement une fois que tout ça est fini.
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
                        // Maintenant on récupère les pseudos des joueurs et d'autres infos I guess
                        // En soit cette partie sert plus vraiment mais l'enlever serait chiant
                        client.sendMessage(new TextMessage("send Player"));
                        socket = client.getSendingSocket(); // On prends la socket du client
                        inputStream = socket.getInputStream(); // On prends l'inputStream
                        try {
                            // On lit la data depuis la socket dans un buffer
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                            //Et on la décrypte
                            key.decryptMessage(buffer.readLine(), true);
                        } catch (IOException e) { //Standard Procedure for dealing with Sockets
                            e.printStackTrace();
                        }
                        // Maintenant on sélectionne un avatar pour le joueur, et on lui fait savoir
                        client.player.avatarName = findFreeAvatar();
                        client.sendMessage(new TextMessage("setAvatar",client.player.avatarName));

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
                                tempClient.sendMessage(new PayloadPlayer(client.player));
                                sleep();
                                System.out.println("Server: Redistributing the Player of " + client.getId() + " to " + tempClient.getId());
                                sleep();
                                client.sendMessage(new PayloadPlayer(tempClient.player));
                                sleep();
                                System.out.println("Server: Redistributing the Player of " + tempClient.getId() + " to " + client.getId());
                                sleep();
                            }
                        }
                        // Et on lui dit que c'est bon de notre coté
                        client.sendMessage(new TextMessage("setAndGo"));
                    }
                    else {
                        client.sendMessage(new TextMessage("rejected"));
                    }
                }
            }
        }).start();
        }

    public void enterLobby() {
        isInLobby = true;
        startLobby();
    }

    public void quitLobby() {
        isInLobby = false;
        Socket temp = Gdx.net.newClientSocket(Net.Protocol.TCP, "127.0.0.1", port, new SocketHints());
        temp.dispose();
        // C'est pas propre mais ça fonctionne, pour déconnecter proprement on est obligé de faire ça
        isSetAndGo = true;
        isRunning = true;
    }
    public void startThread() {
        thread.start();
    }

    public void killThread() {
        System.out.println("Server: Killing Server");
        isInLobby = false;
        isRunning = false;
//        thread.stop();
        for (Client client: clientList.clientList) {
            client.sendMessage(new TextMessage("stopping"));
        }
        serverSocket.dispose();
    }
}
