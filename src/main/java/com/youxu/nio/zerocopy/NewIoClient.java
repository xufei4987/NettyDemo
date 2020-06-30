package com.youxu.nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class NewIoClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8001));

        FileChannel fileChannel = new FileInputStream("IMG_6600.JPG").getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        long start = System.currentTimeMillis();
//        while (true) {
//            byteBuffer.clear();
//            int read = fileChannel.read(byteBuffer);
//            if(read == -1){
//                break;
//            }
//            count += read;
//            byteBuffer.flip();
//            socketChannel.write(byteBuffer);
//        }
        //底层使用了零拷贝，window下一次只能发送8M  大于8M需要分批传输
        long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        System.out.println("发送的总字节数为：" + count + ", 消耗的时间为：" + (System.currentTimeMillis() - start));
        fileChannel.close();
        socketChannel.close();
    }
}
