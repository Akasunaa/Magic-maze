package com.multiplayer.messages.pawn;
import com.badlogic.gdx.math.Vector2;
import com.multiplayer.messages.Message;
import com.screens.game.board.Pawn;

public class AskPlacePawn extends Message {
    public AskPlacePawn(Pawn pawn, Vector2 coordinates) {
        action = "wantToPlacePawn";
        target = pawn.getColor().toString();
        this.coordinates = new float[]{coordinates.x, coordinates.y};
    }
}