package com.youxu.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {
    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();

        ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("bio server start!!");

        while (true) {
            //监听 等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("client connected : " + socket.getInetAddress() + "-" +socket.getPort());

            executorService.execute(
                    () -> handler(socket)
            );
        }
    }

    private static void handler(Socket socket) {
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            while (true) {
                int read = inputStream.read(bytes);
                if(read != -1){
                    String str = new String(bytes, 0, read);
                    System.out.println(str);
                }else {
                    break;
                }
            }
        }catch (Exception e){
             e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
