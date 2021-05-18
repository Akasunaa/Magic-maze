package com.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.multiplayer.messages.PayloadQueue;
import com.screens.BaseScreen;
import com.multiplayer.messages.PayloadPlayer;
import com.multiplayer.messages.TextMessage;
import com.screens.game.board.Player;
import com.screens.game.board.Queue;
import com.utils.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.utils.Multiplayer.*;

/*
Cette classe existe uniquement parce que j'ai aucune idée de comment faire ça en Kotlin
Si jamais on arrive à le faire, cette classe pourra être supprimée simplement
Mais en attendant, petit message informatif
Quand j'ai écrit ce code, seuls quelques dieux et moi le comprenaient
Maintenant, seuls les dieux le comprennent
Alors je vous conseille de prier très fort Athéna pour qu'elle vous aide
Cordialement
Hadrien

Edito:
Depuis que j'ai écrit ce gros pavé, les choses se sont améliorées, j'ai fait de mon mieux pour rendre tout ça
le plus lisible possible, j'ai mis des commentaires, j'ai tenté de bien organiser les choses, etc.
Mais bon, c'est du multithreading, c'est pas simple à gérer, bref, bon courage quand même
 */

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
                    // En gros on doit faire ça parce qu'on peut pas modifier une liste pendant qu'on loop dessus
                    // Et c'aura clairement été mon plus gros némésis ça
                    // Donc je dois les rajouter à l'extérieur de la boucle
                    // Et encore, parce qu'il me semble que dans un cas très précis, même ça peut échouer
                }
                /*
                ça c'est la première boucle de ce thread, en gros il permet de lire les messages
                venant des joueurs tant qu'on est dans le lobby
                 */

                for (Client tempClient : clientList.clientList) {
                    tempClient.sendMessage(new TextMessage("beginGame"));
                    System.out.println("Server: sent beginGame to "+ tempClient.getId());
                }
                // On signal que le serveur est prêt et que normalement les clients ont tout reçu
                PayloadQueue temp = new PayloadQueue(new Queue(9));
                for (Client tempClient : clientList.clientList) {
                    tempClient.sendMessage(temp);
                    System.out.println("Server: sent Queue to "+ tempClient.getId());
                }

                for (Client tempClient : clientList.clientList) {
                    tempClient.sendMessage(new TextMessage("setAndGo"));
                    System.out.println("Server: sent setAndGo to "+ tempClient.getId());
                }

                System.out.println("Server: Beginning last loop");
                while (isRunning) {
                    catchMessage();
                }
                // Boucle qui tourne quand on est en jeu
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
                            break;
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
                // Pas de timeout, c'est un lobby après tout
                serverSocketHint.acceptTimeout = 0;

                // On créé la socket serveur en utilisant le protocol TCP, et en écoutant le port donné
                serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, serverSocketHint);
                System.out.println("Server : Created serverSocket");
                while (isInLobby) {
                    System.out.println("Server : looking for players");
                    socket = serverSocket.accept(null); // On récupère une socket qui demande une connection
                    if (!isInLobby) break;
                    // Petite ligne qui me permet de quitter automatiquement une fois que tout ça est fini.
                    client = new Client(socket);
                    if (!clientList.isIn(client)) {
                        clientList.add(client); // On vérifie si le client n'est pas dans la liste, et on l'ajoute
                        System.out.println("Server: Client added: " + client.getIp() + " as " + client.getId());
                        // On lui demande de renvoyer une nouvelle socket
                        try {
                            client.getSendingSocket().getOutputStream().write(("Server send ReceivingSocket\n").getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        // On dit que la nouvelle socket c'est à lui (et on espère que c'est bien le cas)
                        //TODO Vérifier que c'est bien le cas
                        socket = serverSocket.accept(null);
                        client.receiveSocket(socket);
                        // Maintenant on récupère les pseudos des joueurs et d'autres infos I guess
                        // En soit cette partie sert plus vraiment mais l'enlever serait embêtant
                        client.sendMessage(new TextMessage("send Player"));
                        socket = client.getSendingSocket(); // On prends la socket du client
                        inputStream = socket.getInputStream(); // On prends l'inputStream
                        try {
                            // On lit la data depuis la socket dans un buffer
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream));
                            //Et on la décrypte/déchiffre/traduit
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
                            tempString = mapper.writeValueAsString(client.player);
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
                        System.out.println("Server : rejected incoming request");
                        try {
                            socket.getOutputStream().write("server rejected you\n".getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
