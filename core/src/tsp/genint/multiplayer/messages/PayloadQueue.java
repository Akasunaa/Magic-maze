package tsp.genint.multiplayer.messages;

import tsp.genint.screens.game.board.Queue;

public class PayloadQueue extends Message{
    public PayloadQueue(Queue queue) {
        action = "sending";
        target = "Queue";
        payload = queue.serialize();
    }

}
