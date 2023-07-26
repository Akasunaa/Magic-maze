package tsp.genint.multiplayer.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import tsp.genint.screens.game.board.Player;
import tsp.genint.utils.Multiplayer;

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
