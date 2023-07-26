package com.multiplayer.messages;

import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientListener {
    final public Thread thread;

    //private Decryptor key;
    final private Socket socket;
    public boolean isRunning = true;
    ClientListener(final Decryptor key, final Socket socket) {
        //this.key = key;
        this.socket = socket;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                InputStream inputStream = socket.getInputStream();
                while (isRunning) {
                    try {
                        if (inputStream.available() != 0) { // On vérifie qu'il y a des trucs à lire
                            //Et on la décrypte
                            key.decryptMessage(buffer.readLine(), false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error here");
                    }
                }
            }
        });
    }

    public void startThread() {
        thread.start();
    }
    public void killThread() {
        isRunning = false;
        socket.dispose();
        System.out.println("Client: Killing Courrier");
    }
}
