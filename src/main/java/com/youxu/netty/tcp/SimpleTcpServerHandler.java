package com.youxu.netty.tcp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.TimeUnit;

public class SimpleTcpServerHandler extends ChannelInboundHandlerAdapter {
    //定义一个全局的业务线程池,这样就不会阻塞netty的IO线程
//    private static EventExecutorGroup group = new DefaultEventExecutorGroup(8);
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        System.out.println("server ctx:" + ctx);
//        System.out.println("客户端地址是：" + ctx.channel().remoteAddress());
//        ByteBuf byteBuf = (ByteBuf) msg;
//        System.out.println("收到客户端的信息：" + byteBuf.toString(CharsetUtil.UTF_8));
        System.out.println("channelRead线程：" + Thread.currentThread().getName());
        //可以将耗时操作提交给当前的eventLoop排队执行，实际上是将任务放到taskQueue中
        System.out.println(ctx.pipeline().hashCode());
//        ctx.channel().eventLoop().execute(()->{
////        group.submit( () -> {
//            try {
//                Thread.sleep(5000);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 2",CharsetUtil.UTF_8));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("任务1线程：" + Thread.currentThread().getName());
//        });
//        ctx.channel().eventLoop().execute(()->{
//            try {
//                Thread.sleep(5000);
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 3",CharsetUtil.UTF_8));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            System.out.println("任务2线程：" + Thread.currentThread().getName());
//        });
//        ctx.channel().eventLoop().schedule(()->{
//            try {
//                ctx.writeAndFlush(Unpooled.copiedBuffer("hello client 4",CharsetUtil.UTF_8));
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        },5, TimeUnit.SECONDS);
//        System.out.println("任务3线程：" + Thread.currentThread().getName());
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
