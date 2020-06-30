package com.youxu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MapedByteBufferTest {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("1.txt","rw");

        FileChannel channel = randomAccessFile.getChannel();
        /**
         * 参数1：读写模式
         * 参数2：可以直接修改的起始位置
         * 参数3：映射内存的大小（可以修改的内存范围就是0~4）
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);

        mappedByteBuffer.put(0, (byte) 'H');
        mappedByteBuffer.put(4, (byte) '0');

        channel.close();
        randomAccessFile.close();
    }
}
