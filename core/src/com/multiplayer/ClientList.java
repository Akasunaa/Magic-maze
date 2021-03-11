package com.multiplayer;

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
            if (comparator.getId().equals(client.getId())) {
                return true;
            }
        }
        return false;
    }

    public Client getClient(String id) {
        for (Client comparator : clientList) {
            if (comparator.getId().equals(id)) {
                return comparator;
            }
        }
        return null;
    }


}
