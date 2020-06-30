package com.youxu.nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class OldIoClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 7001);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        FileInputStream fileInputStream = new FileInputStream("IMG_6600.JPG");
        byte[] bytes = new byte[1024];
        long startTime = System.currentTimeMillis();
        int sendBytes = 0;
        while (true) {
            int read = fileInputStream.read(bytes);
            if (read == -1) {
                break;
            }
            sendBytes += read;
            dataOutputStream.write(bytes);
        }
        System.out.println("发送的总字节数为：" + sendBytes + ", 消耗的时间为：" + (System.currentTimeMillis() - startTime));

        dataOutputStream.close();
        socket.close();
        fileInputStream.close();
    }
}
