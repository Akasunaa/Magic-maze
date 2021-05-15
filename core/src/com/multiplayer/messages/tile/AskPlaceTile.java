package com.multiplayer.messages.tile;

import com.multiplayer.messages.Message;

public class AskPlaceTile extends Message {
    public AskPlaceTile() {
        action = "wantToPlaceTile";
    }
}
