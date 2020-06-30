package com.youxu.nio;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 将文件内容读出并输出到控制台
 */
public class FileChannel02 {
    public static void main(String[] args) throws Exception{
        FileInputStream fis = new FileInputStream("1.txt");
        FileChannel fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        fileChannel.read(byteBuffer);

        System.out.println(new String(byteBuffer.array()));

        fileChannel.close();
        fis.close();
    }
}
