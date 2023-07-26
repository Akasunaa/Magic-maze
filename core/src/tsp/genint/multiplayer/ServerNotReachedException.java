package tsp.genint.multiplayer;

public class ServerNotReachedException extends Exception{
    final String errorMessage;
    public ServerNotReachedException(String errorMessage) {
        super();
        this.errorMessage = "Could not connect to server: " + errorMessage;
    }
    public void printError() {
        System.out.println(errorMessage);
    }
}
