package com.multiplayer.messages.pawn;

import com.badlogic.gdx.math.Vector2;
import com.multiplayer.messages.Message;
import com.screens.game.board.Pawn;
import com.utils.Colors;

public class AskTakePawn extends Message {
    public AskTakePawn(Pawn pawn) {
        action = "wantToTakePawn";
        target = Colors.getColor(pawn.getColor());
    }
}
