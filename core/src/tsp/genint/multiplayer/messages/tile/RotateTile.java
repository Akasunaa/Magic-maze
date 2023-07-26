package tsp.genint.multiplayer.messages.tile;

import tsp.genint.multiplayer.messages.Message;

public class RotateTile extends Message {
    public RotateTile(int i) {
        action = "rotateTile";
        target = Integer.toString(i);
    }
}
