package com.multiplayer.messages;

import com.screens.game.board.Queue;

public class PayloadQueue extends Message{
    public PayloadQueue(Queue queue) {
        action = "sending";
        target = "Queue";
        payload = queue.serialize();
    }
}
