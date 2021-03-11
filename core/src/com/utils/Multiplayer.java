package com.utils;

import com.multiplayer.ButtonList;
import com.multiplayer.ClientList;
import com.multiplayer.Courrier;
import com.multiplayer.Decryptor;

public class Multiplayer {
    public static Courrier courrier;
    public static ButtonList buttonList;
    public static ClientList clientList;
    public static Decryptor key;
    public static boolean isServer;

    public static void launchServer() {
        isServer = true;
    }

    public static void stopServer() {
        isServer = false;
    }
}
