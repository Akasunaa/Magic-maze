package tsp.genint.multiplayer.messages.tile;

import tsp.genint.multiplayer.messages.Message;

public class GonnaMoveTile extends Message {
    public GonnaMoveTile(String sender) {
        super(sender);
        action = "isGonnaMoveTile";
        logMessage = getSender() + " a pris une tuile";
    }
}
