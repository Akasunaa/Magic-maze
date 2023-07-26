package tsp.genint.multiplayer.messages.pawn;

import tsp.genint.multiplayer.messages.Message;
import tsp.genint.screens.game.board.Pawn;

public class AskTakePawn extends Message {
    public AskTakePawn(Pawn pawn) {
        action = "wantToTakePawn";
        target = pawn.getColor().toString();
    }
}
