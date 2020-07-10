package com.youxu.netty.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.concurrent.EventExecutorGroup;

public class MyClientMsgEncoder extends MessageToByteEncoder<MsgProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MsgProtocol msg, ByteBuf out) throws Exception {
        System.out.println("MyMsgEncoder被调用");
        out.writeInt(msg.getLen());
        out.writeBytes(msg.getContent());
    }
}
