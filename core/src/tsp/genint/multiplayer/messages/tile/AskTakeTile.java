package tsp.genint.multiplayer.messages.tile;

import tsp.genint.multiplayer.messages.Message;

public class AskTakeTile extends Message {
    public AskTakeTile() {
        action = "wantToTakeTile";
    }
}