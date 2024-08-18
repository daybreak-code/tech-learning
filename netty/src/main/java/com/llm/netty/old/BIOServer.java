package com.llm.netty.old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class BIOServer {

    public static void start(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(port), 2); //
        while (true) {
            final Socket clientSocket = serverSocket.accept();
            System.out.println("accept!");
            new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    String line = in.readLine();
                    while (line != null) {
                        out.println(line);
                        out.flush();
                        line = in.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        clientSocket.close();
                    } catch (IOException ee) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public static void main(String[] args) {
        try {
            start(8084);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
