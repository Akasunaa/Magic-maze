package com.multiplayer.messages.tile;

import com.multiplayer.messages.Message;

public class AskTakeTile extends Message {
    public AskTakeTile() {
        action = "wantToTakeTile";
    }
}