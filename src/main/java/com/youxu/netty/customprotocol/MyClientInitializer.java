package com.youxu.netty.customprotocol;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class MyClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new MyClientMsgEncoder()).addLast(new MyClientHandler());
    }
}
