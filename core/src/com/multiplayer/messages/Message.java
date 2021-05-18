package com.multiplayer.messages;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.utils.Multiplayer;
import java.io.Serializable;

/*
Le principe des messages, c'est un peu le principe des enveloppes
Avant, j'envoyais tout bétement du texte, et parfois du JSON, et du coup
on se retrouvait avec du texte sans son "contexte" parfois, pour des raisons qui m'échappent
Ici, grace aux Messages, on peut facilement changer ça: tout est envoyé en un seul coup !
Et comme ça, tout est beaucoup plus facile
Même envoyer des objets devient beaucoup plus facile ! D'écriture en tout cas

Un truc important lorsqu'on utilise des Messages:
à la réception, tout est interprété comme un Message, rien d'autre
Donc ça ne sert à rien d'ajouter des attributs, ils feront juste bugger la machine
 */
public class Message implements Serializable {
    private final String sender;
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

    protected float[] coordinates;
    public float[] getCoordinates() {return coordinates;}

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
    /*
    Je profite d'être dans un petit fichier pour expliquer ça
    En gros, le parseur qui sérialise en JSON pense que toutes les méthodes qui commencent par un "get", "set", ou "is"
    sont des getter/setter pour des attributs de la classe, même si ce n'est pas le cas
    C'est pour ça qu'il faut lui dire que ce n'est pas le cas, sinon il y a des problèmes.
     */

    public String serialize() {
        try {
            return Multiplayer.mapper.writeValueAsString(this)+"\n";
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }
}
