package com.youxu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws Exception{
        //创建serverSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //获取selector对象
        Selector selector = Selector.open();

        //绑定端口6666
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        //设置为非阻塞模式
        serverSocketChannel.configureBlocking(false);

        //把serversocketchannel注册到selector，关系的事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            if(selector.select(1000) == 0){
                System.out.println("服务器等待1S，无连接");
                continue;
            }
            //获取到被关心的相关的事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> ite = selectionKeys.iterator();
            while (ite.hasNext()) {
                SelectionKey selectionKey = ite.next();
                //根据相应的事件做处理
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    //设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    //将当前socketChannel注册到selector上,关注读事件,并绑定一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();

                    ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();

                    socketChannel.read(byteBuffer);

                    System.out.println("来自客户端的消息：" + new String(byteBuffer.array()));
                }
                //手动从集合中移除当前selectionKey，防止重复操作
                ite.remove();
            }
        }
    }
}
