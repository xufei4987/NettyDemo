package com.youxu.netty.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyServerHandler extends SimpleChannelInboundHandler<MsgProtocol> {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MsgProtocol msg) throws Exception {
        System.out.println("消息的字节大小：" + msg.getLen());
        System.out.println("消息的内容：" + new String(msg.getContent(), Charset.forName("utf-8")));
    }
}
