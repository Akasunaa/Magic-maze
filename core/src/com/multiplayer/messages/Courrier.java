package com.multiplayer.messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.multiplayer.ServerNotReachedException;
import com.screens.game.board.Player;
import com.utils.Multiplayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Courrier {
    private final Socket sendingSocket;

    public boolean getAnswer() {
        return answer;
    }
    public void setAnswer() {
        answer = false;
    }

    public Socket getReceivingSocket() {
        return receivingSocket;
    }

    private Socket receivingSocket;

    boolean answer = false; // This will be used to get answers from the server

    // sendingSocket: La socket pour envoyer des messages au serveur
    // receivingSocket: La socket pour recevoir des messages du serveur

    private ClientListener clientListener;

    public Courrier(String id, int port, String ip) throws ServerNotReachedException {
        System.out.println("Client: Trying to connect to " + ip + " on port " + port);
        SocketHints socketHints = new SocketHints();
        socketHints.connectTimeout = 500;
        socketHints.sendBufferSize = 1024/2;
        socketHints.receiveBufferSize = 1024/2;
        try {
            sendingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            sendingSocket.getOutputStream().write((id + " connected " + ip + "\n").getBytes());
        } catch (Exception e) {
            throw new ServerNotReachedException("Server not found");
        }
        try {
            String waitForIt = (new BufferedReader(new InputStreamReader(sendingSocket.getInputStream()))).readLine();
            if (waitForIt.equals("server rejected you")) {
                throw new ServerNotReachedException("Username is already taken");
            }
            receivingSocket = Gdx.net.newClientSocket(Net.Protocol.TCP, ip, port, socketHints);
            waitForIt = (new BufferedReader(new InputStreamReader(receivingSocket.getInputStream()))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sendObject(Multiplayer.me);
        clientListener = new ClientListener(Multiplayer.key, receivingSocket);
        clientListener.startThread();


    }

    public void sendMessage(Message message) {
        try {
            //println("Client: Sending $message")
            sendingSocket.getOutputStream().write(message.serialize().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendObject(Player player) {
        sendMessage(new PayloadPlayer(player));
        //ObjectOutputStream(sendingSocket.outputStream).write(toSend.serialize().toByteArray())

    }

    public void killThread() {
        clientListener.killThread();
    }
}
