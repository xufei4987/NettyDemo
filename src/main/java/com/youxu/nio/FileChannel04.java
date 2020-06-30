package com.youxu.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 通过FileChannel实现文件拷贝2
 */
public class FileChannel04 {
    public static void main(String[] args) throws Exception{
        FileInputStream fileInputStream = new FileInputStream("1.txt");
        FileChannel srcChannel = fileInputStream.getChannel();

        FileOutputStream fileOutputStream = new FileOutputStream("2.txt");
        FileChannel destChannel = fileOutputStream.getChannel();

        destChannel.transferFrom(srcChannel,0,srcChannel.size());

        srcChannel.close();
        fileInputStream.close();
        destChannel.close();
        fileOutputStream.close();
    }
}
