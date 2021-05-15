package com.multiplayer.messages.pawn;

import com.multiplayer.messages.Message;

public class PlacePawn extends Message {
    public PlacePawn(String color) {
        super("Server");
        action = "placePawn";
        target = color;
    }
}
