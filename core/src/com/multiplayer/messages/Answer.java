package com.multiplayer.messages;

public class Answer extends Message {
    public Answer(Boolean answer) {
        super("Server");
        action = "answer";
        target = answer.toString();
    }
}
