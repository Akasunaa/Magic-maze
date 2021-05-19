package com.multiplayer.messages.pawn;

import com.multiplayer.messages.Message;

public class PlacePawn extends Message {
    public PlacePawn(String sender,String color) {
        super(sender);
        action = "placePawn";
        target = color;
        logMessage = getSender() + " a posé le pion " + target;
    }
}
