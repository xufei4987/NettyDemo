package com.youxu.nio.zerocopy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class OldIoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(7001);
        Socket socket = serverSocket.accept();

        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        byte[] bytes = new byte[1024];
        int count = 0;
        while (true) {
            int read = dataInputStream.read(bytes);
            if (read == -1) break;
            count += read;
        }
        System.out.println("数据读取完毕：" + count);
    }
}
