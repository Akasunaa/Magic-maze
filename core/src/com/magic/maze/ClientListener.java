package com.magic.maze;

import com.badlogic.gdx.net.Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClientListener {
    public Thread thread;

    //private Decryptor key;
    //private Socket socket;
    ClientListener(final Decryptor key, final Socket socket) {
        //this.key = key;
        //this.socket = socket;
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader buffer;
                InputStream inputStream;
                while (true) {
                    try {
                        inputStream = socket.getInputStream();
                        buffer = new BufferedReader(new InputStreamReader(inputStream));
                        key.decryptMessage(buffer.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
