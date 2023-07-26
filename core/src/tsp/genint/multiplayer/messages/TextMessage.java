package tsp.genint.multiplayer.messages;

public class TextMessage extends Message{
    public TextMessage(String action) {
        this.action = action;
    }

    public TextMessage(String action, String target) {
        this.action = action;
        this.target = target;
    }
}
