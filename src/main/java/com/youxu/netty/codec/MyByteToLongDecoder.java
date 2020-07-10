package com.youxu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("decode called");
        //需判断可读的字节是否大于8,8个字节才能转换为Long
        while (in.readableBytes() >= 8){
            out.add(in.readLong());
        }

    }
}
