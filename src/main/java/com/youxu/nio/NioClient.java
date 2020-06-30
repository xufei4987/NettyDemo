package com.youxu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

public class NioClient {
    public static void main(String[] args) throws Exception{
        //获取一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //配置为非阻塞模式
        socketChannel.configureBlocking(false);
        //连接服务器
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);

        if(!socketChannel.connect(inetSocketAddress)){
            //未完成连接时做一些其他事情
            while (!socketChannel.finishConnect()){
                System.out.println("等待连接。。。");
                TimeUnit.MILLISECONDS.sleep(1000L);
            }
        }
        ByteBuffer byteBuffer = ByteBuffer.wrap("hello,NioServer!".getBytes());
        //将buffer数据写入channel
        socketChannel.write(byteBuffer);

        System.in.read();
    }
}
