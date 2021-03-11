package com.utils;

import com.multiplayer.button.ButtonList;
import com.multiplayer.button.ClientList;
import com.multiplayer.button.Courrier;
import com.multiplayer.button.Decryptor;

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
