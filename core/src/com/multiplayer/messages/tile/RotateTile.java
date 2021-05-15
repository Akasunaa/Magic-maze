package com.multiplayer.messages.tile;

import com.multiplayer.messages.Message;

public class RotateTile extends Message {
    public RotateTile(int i) {
        action = "rotateTile";
        target = Integer.toString(i);
    }
}
