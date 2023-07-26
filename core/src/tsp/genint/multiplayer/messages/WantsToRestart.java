package tsp.genint.multiplayer.messages;

public class WantsToRestart extends Message{
    public WantsToRestart() {
        action = "wantsToRestart";
        logMessage = getSender() + " wants to restart";
    }
}
