package tsp.genint.utils;

import tsp.genint.multiplayer.Client;
import tsp.genint.multiplayer.ClientList;
import tsp.genint.multiplayer.ServerMaker;
import tsp.genint.multiplayer.messages.Courrier;
import tsp.genint.multiplayer.messages.Decryptor;
import tsp.genint.screens.game.board.Player;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import com.fasterxml.jackson.module.kotlin.KotlinModule;
public class Multiplayer {
    public static Courrier courrier;
    public static ServerMaker serverMaker;
    final public static ClientList clientList = new ClientList();
    final public static Decryptor key = new Decryptor();
    public static boolean isServer = false;
    final public static int port = 8080;
    public static String serverIP = "127.0.0.1";
    public static String ip;

    public static int numberPeopleWantRestart = 0;

    final public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    // Pour la sérialisation
    public static ObjectMapper mapper = new ObjectMapper().registerModule(new KotlinModule());

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static final Player me = new Player();

    final public static List<Player> playerList = new ArrayList<>(Collections.singletonList(me));
    // ça c'est juste pour que la liste me contienne moi de base

    public static void startServer() {
        isServer = true;
        serverIP = "127.0.0.1";
    }

    public static void stopServer() {
        isServer = false;
    }
}
