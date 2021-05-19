package com.multiplayer.messages.tile;

import com.multiplayer.messages.Message;

public class PlaceTile extends Message {
    public PlaceTile(String sender, String target) {
        super(sender);
        action = "placeTile";
        this.target = target;
        logMessage = getSender() + " a posé une tuile";
    }
}
