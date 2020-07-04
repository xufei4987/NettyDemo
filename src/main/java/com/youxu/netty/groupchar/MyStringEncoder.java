package com.youxu.netty.groupchar;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.string.StringEncoder;

import java.util.List;

public class MyStringEncoder extends StringEncoder {
    @Override
    protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
        System.out.println("112233");
        super.encode(ctx, msg, out);
    }
}
