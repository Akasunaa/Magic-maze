package com.multiplayer;

import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientListener {
    public Thread thread;

    //private Decryptor key;
    //private Socket socket;
    private boolean exit = false;
    ClientListener(final Decryptor key, final Socket socket) {
        //this.key = key;
        //this.socket = socket;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader buffer;
                InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
                while (true) {
                    try {
                        buffer = new BufferedReader(inputStreamReader);
                        String message = buffer.readLine();
                        key.decryptMessage(message, false);
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
        exit = true;
        thread.stop();
        System.out.println("Client: Killing Courrier");
    }
}
