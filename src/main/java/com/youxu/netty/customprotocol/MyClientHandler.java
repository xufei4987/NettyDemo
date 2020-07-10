package com.youxu.netty.customprotocol;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Long msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0 ; i < 5; i++){
            String content = "hello世界" + i;
            MsgProtocol msgProtocol = new MsgProtocol();
            msgProtocol.setContent(content.getBytes(Charset.forName("utf-8")));
            msgProtocol.setLen(content.getBytes(Charset.forName("utf-8")).length);
            ctx.writeAndFlush(msgProtocol);
        }
    }
}
