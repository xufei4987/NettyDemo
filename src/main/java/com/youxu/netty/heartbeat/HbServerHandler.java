package com.youxu.netty.heartbeat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class HbServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            Channel channel = ctx.channel();
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    System.out.println(channel.remoteAddress() + " 读空闲");
                    break;
                case WRITER_IDLE:
                    System.out.println(channel.remoteAddress() + " 写空闲");
                    break;
                case ALL_IDLE:
                    System.out.println(channel.remoteAddress() + " 读写空闲");
                    break;
            }
        }
    }
}
