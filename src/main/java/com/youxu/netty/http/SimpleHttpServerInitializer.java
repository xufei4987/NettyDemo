package com.youxu.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

public class SimpleHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        /**
         * 添加Http编解码器,可以对http报文进行解析
         */
        ch.pipeline().addLast("MyHttpServerCodec",new HttpServerCodec());
        ch.pipeline().addLast("MyHttpServerHandler",new SimpleHttpServerHandler());
    }
}
