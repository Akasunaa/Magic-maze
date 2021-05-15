package com.multiplayer.messages;

import com.tiles.Queue;

public class PayloadQueue extends Message{
    public PayloadQueue(Queue queue) {
        action = "sending";
        target = "Queue";
        payload = queue.serialize();
    }

}
