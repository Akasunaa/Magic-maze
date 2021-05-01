package com.multiplayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.tiles.Player;
import com.utils.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Courrier {
    private final Socket sendingSocket;

    public boolean getAnswer() {
        return answer;
    }

    public Socket getReceivingSocket() {
        return receivingSocket;
    }

    private Socket receivingSocket;

    boolean answer = false; // This will be used to get answers from the server
    private final String id;

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur

    private final ClientListener clientListener;

    public Courrier(String id, int port, String ip) throws ServerNotReachedException {
        this.id = id;
        SocketHints socketHints = new SocketHints();
        socketHints.connectTimeout = 5000;
        socketHints.sendBufferSize = 1024;
        socketHints.receiveBufferSize = 1024;
        try {
            sendingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            sendMessage("connected " + ip);
        } catch (Exception e) {
            throw new ServerNotReachedException("Server not found");
        }
        String waitForIt = null;
        try {
            waitForIt = (new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()))).readLine();
            if (waitForIt.equals("server rejected you"))
                throw new ServerNotReachedException("Username is already taken");
            receivingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            waitForIt = (new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendObject(Multiplayer.me);
        clientListener = new ClientListener(Multiplayer.key, receivingSocket);
        clientListener.startThread();

    }

    public void sendMessage(String message) {
        try {
            //println("Client: Sending $message")
            sendingSocket.getOutputStream().write((id + " " + message + "\n").getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(BigButton toSend) {
        sendMessage("sending BigButton");
        //ObjectOutputStream(sendingSocket.outputStream).write(toSend.serialize().toByteArray())
        try {
            sendingSocket.getOutputStream().write(((toSend.serialize() + "\n").getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Player toSend) {
        sendMessage("sending Player");
        //ObjectOutputStream(sendingSocket.outputStream).write(toSend.serialize().toByteArray())
        try {
            sendingSocket.getOutputStream().write(((Multiplayer.mapper.writeValueAsString(toSend) + " \n").getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void killThread() {
        clientListener.killThread();
    }
}
