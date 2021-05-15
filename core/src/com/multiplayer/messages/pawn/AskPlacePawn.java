package com.multiplayer.messages.pawn;
import com.multiplayer.messages.Message;
import com.tiles.Pawn;
import com.utils.Colors;

public class AskPlacePawn extends Message {
    public AskPlacePawn(Pawn pawn) {
        action = "wantToPlacePawn";
        target = Colors.getColor(pawn.getColor());
    }
}