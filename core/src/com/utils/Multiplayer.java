package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplayer.*;
import com.tiles.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Multiplayer {
    public static Courrier courrier;
    public static ServerMaker serverMaker;
    public static ButtonList buttonList;
    public static int numberOfPlayers = 1;
    public static ClientList clientList = new ClientList(Multiplayer.numberOfPlayers);
    public static Decryptor key = new Decryptor();
    public static boolean isServer = false;
    public static int port = 6969;
    public static String serverIP = "127.0.0.1"; //"157.159.41.36";
    // L'ip de mon PC fixe
    public static String ip;

    public volatile static boolean isServerSetAndGo = false;
    public static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    // Pour la sérialisation
    public static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static final Player me = new Player();

    public static List<Player> playerList = new ArrayList<>(Collections.singletonList(me));
    // ça c'est juste pour que la liste me contienne moi de base

    public static void startServer() {
        isServer = true;
        serverIP = "127.0.0.1";
    }

    public static void stopServer() {
        isServer = false;
    }
}
