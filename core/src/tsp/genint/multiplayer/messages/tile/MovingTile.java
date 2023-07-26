package tsp.genint.multiplayer.messages.tile;

import com.badlogic.gdx.math.Vector2;
import tsp.genint.multiplayer.messages.Message;

public class MovingTile extends Message {
    public MovingTile(Vector2 coordinates) {
        action = "movingTile";
        this.coordinates = new float[] {coordinates.x,coordinates.y};
    }
}
