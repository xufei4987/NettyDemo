package com.youxu.nio;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过FileChannel将内容写入到文件
 */
public class FileChannel01 {
    public static void main(String[] args) throws Exception {
        FileOutputStream fileOutputStream = new FileOutputStream("1.txt");
        FileChannel fileChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        byteBuffer.put("hello world!!".getBytes());

        byteBuffer.flip();

        fileChannel.write(byteBuffer);

        fileChannel.close();
        fileOutputStream.close();
    }
}
