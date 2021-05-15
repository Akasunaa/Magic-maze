package com.multiplayer.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.utils.Multiplayer;

import java.io.Serializable;

import static com.utils.Multiplayer.isServer;

public class Message implements Serializable {
    private String sender;
    public String getSender() {
        return sender;
    }

    protected String action;
    public String getAction() {
        return action;
    }

    protected String target;
    public String getTarget() {
        return target;
    }

    protected String payload;
    public String getPayload() {
        return payload;
    }

    public Message(String sender) {
        this.sender = sender;
    }
    public Message() {
        sender = Multiplayer.me.pseudo;
    }

    @JsonIgnore
    public String getMessage() {
        return sender + " " + action + " " + target;
    }

    public String serialize() {
        try {
            return Multiplayer.mapper.writeValueAsString(this)+"\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
