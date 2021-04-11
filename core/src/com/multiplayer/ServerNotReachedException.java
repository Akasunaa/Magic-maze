package com.multiplayer;

public class ServerNotReachedException extends Exception{
    String  errorMessage;
    public ServerNotReachedException(String errorMessage) {
        super();
        this.errorMessage = "Could not connect to server: " + errorMessage;
    }
    public void printError() {
        System.out.println(errorMessage);}

}
