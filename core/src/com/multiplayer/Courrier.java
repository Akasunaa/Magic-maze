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
    private Socket sendingSocket;

    public boolean getAnswer() {
        return answer;
    }

    public Socket getReceivingSocket() {
        return receivingSocket;
    }

    private Socket receivingSocket;
    private SocketHints socketHints;

    boolean answer = false; // This will be used to get answers from the server
    private String id;
    private int port;
    private String ip;

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur

    public Courrier(String id, int port, String ip) throws ServerNotReachedException {
        this.id = id;
        this.port = port;
        this.ip = ip;
        socketHints = new SocketHints();
        socketHints.connectTimeout = 0;
        socketHints.sendBufferSize = 1024;
        socketHints.receiveBufferSize = 1024;
        try {
            sendingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            sendMessage("connected $ip");
            String waitForIt = (new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()))).readLine();
            receivingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            waitForIt = (new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()))).readLine();
            sendObject(Multiplayer.me);
            new ClientListener(Multiplayer.key, receivingSocket).startThread();
        } catch (Exception e) {
            throw new ServerNotReachedException();
        }

    }

    public void sendMessage(String message) {
        try {
            //println("Client: Sending $message")
            sendingSocket.getOutputStream().write(("$id $message \n").getBytes());
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
}
