package com.multiplayer.messages.pawn;

import com.multiplayer.messages.Message;
import com.tiles.Pawn;
import com.utils.Colors;

public class AskTakePawn extends Message {
    public AskTakePawn(Pawn pawn) {
        action = "wantToTakePawn";
        target = Colors.getColor(pawn.getColor());
    }
}
