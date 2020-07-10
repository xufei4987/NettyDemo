package com.youxu.netty.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

public class SimpleTcpServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx:" + ctx);
//        System.out.println("客户端地址是：" + ctx.channel().remoteAddress());
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("收到客户端的信息：" + byteBuf.toString(CharsetUtil.UTF_8));
        //可以将耗时操作提交给当前的eventLoop排队执行，实际上是将任务放到taskQueue中
        System.out.println(ctx.pipeline().hashCode());
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 2",CharsetUtil.UTF_8));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().execute(()->{
            try {
                Thread.sleep(5000);
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 3",CharsetUtil.UTF_8));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        ctx.channel().eventLoop().schedule(()->{
            try {
                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 4",CharsetUtil.UTF_8));
            }catch (Exception e){
                e.printStackTrace();
            }
        },5, TimeUnit.SECONDS);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 1",CharsetUtil.UTF_8));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.channel().close();
    }
}
