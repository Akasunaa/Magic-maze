package com.multiplayer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.screens.game.board.Player;
import com.utils.Multiplayer;

public class PayloadPlayer extends Message{
    public PayloadPlayer(Player player) {
        target = "Player";
        action = "sending";
        try {
            payload = Multiplayer.mapper.writeValueAsString(player);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
