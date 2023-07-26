package com.multiplayer.messages.pawn;

import com.multiplayer.messages.Message;
import com.screens.game.board.Pawn;

public class AskTakePawn extends Message {
    public AskTakePawn(Pawn pawn) {
        action = "wantToTakePawn";
        target = pawn.getColor().toString();
    }
}
