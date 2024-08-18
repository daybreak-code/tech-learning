package com.llm.netty.old;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {

    public static void main(String[] args) {
        try {
            start(8084);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start(int port) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        InetSocketAddress address = new InetSocketAddress(port);
        serverSocketChannel.bind(address);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select();
            Set<SelectionKey> readKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = readKeys.iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                if (key.isAcceptable()){
                    ServerSocketChannel server = (ServerSocketChannel)key.channel();
                    SocketChannel socket = server.accept();
                    System.out.println("Accept! ");
                    socket.configureBlocking(false);
                    socket.register(selector, SelectionKey.OP_READ);
                }
                if (key.isReadable()){
                    SocketChannel socket = (SocketChannel)key.channel();
                    final ByteBuffer buffer = ByteBuffer.allocate(64);
                    int read = socket.read(buffer);
                    if (read > 0){
                        buffer.flip();
                        int ret = socket.write(buffer);
                        if (ret <= 0){
                            socket.register(selector, SelectionKey.OP_WRITE);
                        }
                        buffer.clear();
                    } else if (read < 0) {
                        key.cancel();
                        socket.close();
                        System.out.println("Client close");
                    }
                }
                it.remove();//don't forget, why need to manually move?
            }
        }
    }
}
