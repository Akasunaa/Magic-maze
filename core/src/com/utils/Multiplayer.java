package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplayer.*;
import com.tiles.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class Multiplayer {
    public static Courrier courrier;
    public static ButtonList buttonList;
    public static int numberOfPlayers = 2;
    public static ClientList clientList = new ClientList(Multiplayer.numberOfPlayers);
    public static Decryptor key = new Decryptor();
    public static boolean isServer = false;
    public static int port = 6969;
    public static String serverIP = "157.159.41.36";
    // L'ip de mon PC fixe
    public static String ip;

    public static boolean isHoldingPawn = false;

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

    public static List<Player> playerList = new ArrayList<Player>();

    public static Player me = new Player();

    public static void launchServer() {
        isServer = true;
    }

    public static void stopServer() {
        isServer = false;
    }
}
