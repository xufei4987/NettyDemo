package com.youxu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过FileChannel实现文件拷贝1
 */
public class FileChannel03 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel srcChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel destChannel = fileOutputStream.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(512);

        while (true){
            int read = srcChannel.read(byteBuffer);
            if(read == -1){
                break;
            }
            byteBuffer.flip();
            destChannel.write(byteBuffer);
            byteBuffer.clear();
        }

        srcChannel.close();
        fileInputStream.close();
        destChannel.close();
        fileOutputStream.close();
    }
}
