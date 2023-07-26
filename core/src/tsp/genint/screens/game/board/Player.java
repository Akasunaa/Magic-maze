package tsp.genint.screens.game.board;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.annotation.JsonIgnore;
import tsp.genint.screens.game.BaseActor;
import tsp.genint.screens.game.hud.Clock;
import tsp.genint.utils.Directions;

import java.io.Serializable;

import static tsp.genint.utils.FunctionsKt.modulo;

public class Player implements Serializable {
    public boolean north;
    public boolean south;
    public boolean east;
    public boolean west;
    public boolean escalatorTaker;
    public boolean portalTaker;
    public boolean cardChooser;

    public String pseudo = "Pseudo";
    public transient BaseActor avatar;
    public String avatarName;

    Pawn pawn = null;

    @JsonIgnore
    public boolean isHoldingPawn() {
        return pawn !=null;
    }

    public Player() {
    }


    public Player load() {
        avatar = new BaseActor(new Texture(Gdx.files.internal("Game/Avatars/" + avatarName + ".png")));
        return this;
    }

    private Player(boolean north, boolean south,
            boolean east, boolean west,
            boolean escalatorTaker, boolean portalTaker,
            boolean cardChooser, Pawn pawn) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.escalatorTaker = escalatorTaker;
        this.portalTaker = portalTaker;
        this.cardChooser = cardChooser;
        this.pawn = pawn;
    }
    public Player(boolean north, boolean south,
                  boolean east, boolean west,
                  boolean escalatorTaker, boolean portalTaker,
                  boolean cardChooser) {
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.escalatorTaker = escalatorTaker;
        this.portalTaker = portalTaker;
        this.cardChooser = cardChooser;
    }
    // Quel enfer

    /**
     * copy player stats into this player
     * @param player the player whose stats will be copied
     */
    public void setPlayer(Player player) {
        north = player.north;
        south = player.south;
        west = player.west;
        east = player.east;
        escalatorTaker = player.escalatorTaker;
        portalTaker = player.portalTaker;
        cardChooser = player.cardChooser;
    }

    Player rotate(int i) {
        if (i == 0) return this;
        else return
                (new Player(south, north, west, east, escalatorTaker, portalTaker, cardChooser, pawn))
                        .rotate(modulo(i - 1, Directions.values().length));
    }
    public void takesPawn(Pawn pawn) {
        Clock.clock.unpause();
        this.pawn = pawn;
        pawn.setPlayer(this);
    }
    public void dropsPawn(Pawn pawn) {
        this.pawn = null;
        pawn.setPlayer(null);
    }
}
