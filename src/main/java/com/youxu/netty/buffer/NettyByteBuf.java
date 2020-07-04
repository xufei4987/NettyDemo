package com.youxu.netty.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 *  0 <= readerIndex <= writeIndex <= capacity
 */
public class NettyByteBuf {
    public static void main(String[] args) {

        ByteBuf buffer = Unpooled.buffer();

        for(int i = 0; i < 10; i++){
            buffer.writeByte(i);
        }

        System.out.println(buffer.capacity());

        for (int i = 0; i < buffer.readableBytes(); i++){
            System.out.println(buffer.getByte(i));
        }

    }
}
