package com.mygdx.game;

public class GameClients {
    String[] listIP;
    int clients;

    GameClients(String serverIP) {
        listIP = new String[4];
        listIP[0] = serverIP;
        clients = 1;
    }

    public boolean isFull() { return clients >=4; }

    public void addClient(String serverIP) {
        listIP[clients] = serverIP;
        clients ++;
    }


}
