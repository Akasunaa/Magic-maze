package tsp.genint.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.ServerSocket;
import com.badlogic.gdx.net.ServerSocketHints;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import tsp.genint.screens.BaseScreen;
import tsp.genint.screens.game.board.Player;
import tsp.genint.screens.game.board.Queue;
import tsp.genint.utils.Multiplayer;
import tsp.genint.multiplayer.messages.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

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
    final Thread thread;
    ServerSocket serverSocket;
    private boolean isSetAndGo = false;
    final int port;

    public ServerMaker(final int port, final List<Client> clientList) {
        this.port = port;
        thread = new Thread(new Runnable() {
            private void sleep() {
                int sleepTime = 10;
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            private void askForConfirm(Client client) {
                sleep();
                BufferedReader buffer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
                try {
                    String line = buffer.readLine();
                    if (Multiplayer.mapper.readValue(line, Message.class).getAction().equals("confirm")) {
                        System.out.println("Server: " + client.getId() + " confirmed reception");
                        return;
                    } else {
                        System.out.println("Serveur: Erreur de confirmation");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // En gros là on dit au serveur d'attendre que tous les clients aient bien recu la Queue pour continuer
            }

            private List<Player> choosePlayer() {
                // Mais au moins ça fonctionne
                Player[] output = new Player[]{new Player(true, true, true, true, true, true, true)};

                if (Multiplayer.playerList.size() == 2) {
                    output = new Player[]{
                            new Player(true, true, false, false, true, true, false),
                            new Player(false, false, true, true, false, false, true)};
                }
                if (Multiplayer.playerList.size() == 3) {
                    output = new Player[]{
                            new Player(true, true, false, false, false, true, false),
                            new Player(false, false, true, false, false, false, true),
                            new Player(false, false, false, true, true, false, false)};
                }
                if (Multiplayer.playerList.size() == 4) {
                    output = new Player[]{
                            new Player(false, false, false, true, true, true, false),
                            new Player(false, false, true, false, true, false, false),
                            new Player(false, true, false, false, true, false, true),
                            new Player(true, false, true, false, false, false, false)};
                }
                List<Player> temp = Arrays.asList(output);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                Collections.shuffle(temp);
                return temp;
            }

            /**
             * Catches messages from everyone.
             */
            private void catchMessage() {
                // On doit utiliser une boucle a itérateur parce qu'on rajoute des clients à la boucle.
                Iterator<Client> it = clientList.iterator();
                while (it.hasNext()) {
                    Client tempClient = it.next();
                    try {
                        if (tempClient.getSocket().getInputStream().available() != 0) {
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(tempClient.getSocket().getInputStream()));
                            Multiplayer.key.decryptMessage(buffer.readLine(), true);
                        }
                    } catch (IOException e) { //Standard Procedure for dealing with Sockets
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void run() {
                // Tant que le host n'a pas lancé la partie, on attends les autres joueurs
                while (!isSetAndGo) {
                    catchMessage();
                    sleep();
                }
                // Why do I have to sleep there ??? Occult stuff going on...
                System.out.println("Server thread ready to go");
                // Le host a lancé la partie
                System.out.println(clientList);

                for (Client tempClient : clientList) {
                    tempClient.sendMessage(new TextMessage("beginGame").asServer());
                    System.out.println("Server: sent beginGame to "+ tempClient.getId());
                }
                // On signale que le serveur est prêt et que normalement les clients ont tout reçu
                Message temp = new PayloadQueue(new Queue(9)).asServer();
                for (Client tempClient : clientList) {
                    tempClient.sendMessage(temp);
                    System.out.println("Server: sent Queue to "+ tempClient.getId());
                    askForConfirm(tempClient);
                }

                List<Player> newPlayerList = choosePlayer();
                for (int i = 0; i < Multiplayer.playerList.size(); i++) {
                    AssignPlayer message = new AssignPlayer(newPlayerList.get(i), Multiplayer.playerList.get(i).pseudo);
                    // I am once again asking you not to overload the client
                    for (Client tempClient : clientList) {
                        System.out.println("Server: Sent Assigned " + message.getTarget() + " to " + tempClient.getPlayer().pseudo);
                        tempClient.sendMessage(message);
                        askForConfirm(tempClient);
                    }
                }

                for (Client tempClient : clientList) {
                    tempClient.sendMessage(new TextMessage("setAndGo").asServer());
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
            final List<Client> clientList = Multiplayer.clientList;
            InputStream inputStream;

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
                    for (Player player : Multiplayer.playerList) {
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
                    if (!isInLobby) {
                        System.out.println("Server: Not in lobby anymore");
                        return;
                    }
                    // Petite ligne qui me permet de quitter automatiquement une fois que tout ça est fini.
                    client = new Client(socket);
                    boolean alreadyKnown = false;
                    for (Client tempClient:clientList) {
                        if (client.getId().equals(tempClient.getId())) {
                            alreadyKnown = true;
                        }
                    }
                    if (!alreadyKnown) {
                        clientList.add(client); // On vérifie si le client n'est pas dans la liste, et on l'ajoute
                        System.out.println("Server: Client added: " + client.getIp() + " as " + client.getId());
                        // Maintenant on récupère les pseudos des joueurs et d'autres infos I guess
                        // En soit cette partie sert plus vraiment mais l'enlever serait embêtant
                        client.sendMessage(new TextMessage("send Player").asServer());
                        try {
                            BufferedReader buffer = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
                            Multiplayer.key.decryptMessage(buffer.readLine(), true);
                        } catch (IOException e) { //Standard Procedure for dealing with Sockets
                            e.printStackTrace();
                        }
                        // Maintenant on sélectionne un avatar pour le joueur, et on lui fait savoir
                        client.player.avatarName = findFreeAvatar();
                        client.sendMessage(new TextMessage("setAvatar",client.player.avatarName).asServer());

                        // Maintenant on renvois le joueur à tout le monde
                        // Et également on envois tout le monde au joueur
                        for (Client tempClient : Multiplayer.clientList) {
                            if (!tempClient.getId().equals(client.getId())) {
                                sleep();
                                tempClient.sendMessage(new PayloadPlayer(client.player).asServer());
//                                askForConfirm(tempClient);
                                sleep();
                                System.out.println("Server: Redistributing the Player of " + client.getId() + " to " + tempClient.getId());
                                client.sendMessage(new PayloadPlayer(tempClient.player).asServer());
//                                askForConfirm(client);
                                sleep();
                                System.out.println("Server: Redistributing the Player of " + tempClient.getId() + " to " + client.getId());
                            }
                            // On ne peut pas utiliser les askForConfirm parce qu'il y a le thread a coté qui vole les messages
                        }
                        // Et on lui dit que c'est bon de notre coté
                        sleep();
                        sleep();
                        client.sendMessage(new TextMessage("setAndGo").asServer());
                        System.out.println("Server: sent setAndGo to " + client.getId());
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
        // Pour débloquer le thread principal ?
        Socket temp = Gdx.net.newClientSocket(Net.Protocol.TCP, "127.0.0.1", port, new SocketHints());
        System.out.println("Server: Quitting lobby successful");
        temp.dispose();
        isSetAndGo = true;
        isRunning = true;
    }
    public void startThread() {
        thread.start();
    }

    public void killThread() {
        System.out.println("Server: Killing Server");
        for (Client client: Multiplayer.clientList) {
            if (!client.getIp().equals("127.0.0.1"))
                client.sendMessage(new TextMessage("stopping").asServer());
        }
        isRunning = false;
        if (isInLobby) {
            isInLobby = false;
            Socket temp = Gdx.net.newClientSocket(Net.Protocol.TCP, "127.0.0.1", port, new SocketHints());
            temp.dispose();
        }
        serverSocket.dispose();
    }
}
