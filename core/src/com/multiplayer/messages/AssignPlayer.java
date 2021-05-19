package com.multiplayer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.screens.game.board.Player;
import com.utils.Multiplayer;

public class AssignPlayer extends Message{
    public AssignPlayer(Player player, String target) {
        super("Server");
        this.target = target;
        action = "assign";
        try {
            payload = Multiplayer.mapper.writeValueAsString(player);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
