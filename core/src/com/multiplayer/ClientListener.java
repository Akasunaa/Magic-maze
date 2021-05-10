package com.multiplayer;

import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static com.utils.Multiplayer.key;

public class ClientListener {
    public Thread thread;

    //private Decryptor key;
    private Socket socket;
    public boolean isRunning = true;
    ClientListener(final Decryptor key, final Socket socket) {
        //this.key = key;
        this.socket = socket;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader buffer;
                InputStream inputStream = socket.getInputStream();
                while (isRunning) {
                    try {
                        if (inputStream.available() != 0) {
                            // On lit la data depuis la socket dans un buffer
                            buffer = new BufferedReader(new InputStreamReader(inputStream));
                            //Et on la décrypte
                            key.decryptMessage(buffer.readLine(), true);
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
