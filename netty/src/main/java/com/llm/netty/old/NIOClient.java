package com.llm.netty.old;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class NIOClient {

    public static final String[] commands = new String[] {
            "hi",
            "i am client",
            "helloworld",
            "java and netty"
    };

    public static void main(String[] args) {
        int concurrent = 1;
        Runnable task = () -> {
            try {
                Socket socket = new Socket("127.0.0.1", 8084);
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                for (String str : commands) {
                    out.writeByte('A');
                    out.writeByte('W');
                    int length = str.length();
                    out.writeShort(length * 2 + 2);
                    out.writeChars(str);
                }
                out.flush();
                BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String line = null;
                while (!((line = br.readLine()) == null)){
                    System.out.println(line);
                }
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }

}
