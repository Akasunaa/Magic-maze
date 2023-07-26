package com.multiplayer.messages.pawn;

import com.badlogic.gdx.math.Vector2;
import com.multiplayer.messages.Message;
import com.screens.game.board.Pawn;

public class MovingPawn extends Message {
    public MovingPawn(Pawn pawn, Vector2 coordinates) {
        action = "movingPawn";
        target = pawn.getColor().toString();
        this.coordinates = new float[]{coordinates.x, coordinates.y};
    }
    public MovingPawn(String sender,Pawn pawn, Vector2 coordinates) {
        super(sender);
        action = "movingPawn";
        target = pawn.getColor().toString();
        this.coordinates = new float[]{coordinates.x, coordinates.y};
    }
}
