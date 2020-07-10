package com.youxu.netty.customprotocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

public class MyServerMsgDecoder extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        MsgProtocol msgProtocol = new MsgProtocol();
        msgProtocol.setLen(len);
        msgProtocol.setContent(bytes);
        out.add(msgProtocol);
    }
}
