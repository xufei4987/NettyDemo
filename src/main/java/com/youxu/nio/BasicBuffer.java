package com.youxu.nio;

import java.nio.IntBuffer;

/**
 * Buffer四大标志位
 * mark:标记位
 * limit:读取的限制
 * capacity:容量
 * position:下一个需要写入或读取的位置
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for(int i = 0; i < intBuffer.capacity(); i++){
            intBuffer.put(i * 2);
        }

        //读写切换
        intBuffer.flip();

        while (intBuffer.hasRemaining()) {
            System.out.println(intBuffer.get());
        }
    }
}
