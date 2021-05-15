package com.multiplayer.messages.tile;

import com.multiplayer.messages.Message;

public class GonnaMoveTile extends Message {
    public GonnaMoveTile(String sender) {
        super(sender);
        action = "isGonnaMoveTile";
    }
}
