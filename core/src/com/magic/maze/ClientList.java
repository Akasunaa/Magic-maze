package com.magic.maze;

import java.util.ArrayList;
import java.util.List;

public class ClientList {
    List<Client> clientList;
    int numberClient = 0;
    int maxClient;

    ClientList(int max) {
        clientList = new ArrayList<>();
        maxClient = max;
    }

    public boolean isFull() {
        return numberClient >= maxClient;
    }

    public void add(Client client) {
        clientList.add(client);
        numberClient++;
    }

    public boolean isIn(Client client) {
        for (Client comparator : clientList) {
            if (comparator.getIp().equals(client.getIp())) {
                return true;
            }
        }
        return false;
    }


}
