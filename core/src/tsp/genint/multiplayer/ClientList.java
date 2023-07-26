package tsp.genint.multiplayer;

import java.util.ArrayList;
import java.util.List;

public class ClientList {
    final public List<Client> clientList;
    int numberClient = 0;

    public ClientList() {
        clientList = new ArrayList<>();
    }

    private Client clientToAdd;
    private boolean hasClientToAdd;
    public void add(Client client) {
        clientToAdd = client;
        hasClientToAdd = true;
    }
    public void addBackup() {
        if (hasClientToAdd) {
            clientList.add(clientToAdd);
            numberClient ++;
            clientToAdd = null;
            hasClientToAdd = false;
        }
    }

    private Client clientToRemove;
    private boolean hasClientToRemove;
    public void remove (Client client) {
        clientToRemove = client;
        hasClientToRemove = true;
    }
    public void removeBackup() {
        if (hasClientToRemove) {
            clientList.remove(clientToRemove);
            numberClient --;
            clientToRemove = null;
            hasClientToRemove = false;
        }
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
