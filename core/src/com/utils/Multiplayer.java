package com.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.multiplayer.*;
import com.tiles.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Multiplayer {
    public static Courrier courrier;
    public static ButtonList buttonList;
    public static int numberOfPlayers = 2;
    public static ClientList clientList = new ClientList(Multiplayer.numberOfPlayers);
    public static Decryptor key = new Decryptor();
    public static boolean isServer = true;
    public static int port = 6969;
    public static String serverIP = "157.159.41.36";
    // L'ip de mon PC fixe
    public static String ip;

    // Pour la sérialisation
    public static ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static Player me = new Player();

    public static void launchServer() {
        isServer = true;
    }

    public static void stopServer() {
        isServer = false;
    }
}
