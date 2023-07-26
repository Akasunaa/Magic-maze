package tsp.genint.multiplayer.messages.pawn;

import tsp.genint.multiplayer.messages.Message;

public class PlacePawn extends Message {
    public PlacePawn(String sender,String color) {
        super(sender);
        action = "placePawn";
        target = color;
        logMessage = getSender() + " a posé le pion " + target;
    }
}
