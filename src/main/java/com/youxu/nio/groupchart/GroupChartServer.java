package com.youxu.nio.groupchart;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * 群聊系统
 */
public class GroupChartServer {
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    //初始化操作
    public GroupChartServer(){
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //监听
    public void listen(){
        try {
            while (true) {
                int select = selector.select(2000L);
                if(select > 0){//有event需要处理
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey selectionKey = iterator.next();

                        if (selectionKey.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " 上线了!");
                        }

                        if (selectionKey.isReadable()) {
                            readClientMsg(selectionKey);
                        }

                        iterator.remove();
                    }
                } else {
                    System.out.println("wait for client to connect");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }

    private void readClientMsg(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            int read = socketChannel.read(byteBuffer);
            if(read > 0){
                String msg = new String(byteBuffer.array());
                System.out.println("from client:" + msg);
                //向其他客户端转发消息
                sendMsg2OtherClient(msg,socketChannel);
            }
        }catch (IOException e){
            try {
                System.out.println(socketChannel.getRemoteAddress() + " 离线了。。。" );
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMsg2OtherClient(String msg, SocketChannel selfSocketChannel) throws IOException {
        System.out.println("服务器转发消息中。。。");
        //遍历所有注册到selector的socketchannel，并排除自己
        Set<SelectionKey> keys = selector.keys();
        for (SelectionKey key : keys){
            SelectableChannel channel = key.channel();
            if(channel instanceof SocketChannel && channel != selfSocketChannel){
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) channel).write(byteBuffer);
            }
        }
    }

    public static void main(String[] args) {
        GroupChartServer groupChartServer = new GroupChartServer();
        groupChartServer.listen();
    }
}
