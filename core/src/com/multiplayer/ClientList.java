package com.multiplayer;

import com.utils.GameScreens;
import com.utils.Multiplayer;

import java.util.ArrayList;
import java.util.List;

public class ClientList {
    public List<Client> clientList;
    int numberClient = 0;

    public ClientList() {
        clientList = new ArrayList<>();
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

    public Client get (int i) {
        return clientList.get(i);
    }


}
