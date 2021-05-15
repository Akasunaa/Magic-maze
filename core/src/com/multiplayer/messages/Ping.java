package com.multiplayer.messages;

public class Ping  extends Message{
    public Ping(String target) {
        action = "ping";
        this.target = target;
    }
}
