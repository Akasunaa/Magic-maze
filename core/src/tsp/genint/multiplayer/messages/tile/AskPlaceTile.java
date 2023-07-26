package tsp.genint.multiplayer.messages.tile;

import tsp.genint.multiplayer.messages.Message;

public class AskPlaceTile extends Message {
    public AskPlaceTile() {
        action = "wantToPlaceTile";
    }
}
