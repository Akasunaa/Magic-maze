package com.multiplayer.messages.pawn;
import com.badlogic.gdx.math.Vector2;
import com.multiplayer.messages.Message;
import com.screens.game.board.Pawn;
import com.utils.Colors;

public class AskPlacePawn extends Message {
    public AskPlacePawn(Pawn pawn, Vector2 coordinates) {
        action = "wantToPlacePawn";
        target = Colors.getColor(pawn.getColor());
        this.coordinates = new float[] {coordinates.x,coordinates.y};
    }
}