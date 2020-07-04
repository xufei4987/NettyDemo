package com.youxu.netty.groupchar;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GroupChartServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组  管理所有的channel
    //GlobalEventExecutor.INSTANCE 全局时间执行器
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 表示连接建立，一旦建立第一个执行
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //遍历所有channel并发送消息
        channels.writeAndFlush("[客户端]-" + channel.remoteAddress().toString().substring(1) + "-加入了聊天室 [" + sdf.format(new Date()) + "]\n");
        //添加新加入的channel
        channels.add(channel);
    }
    /**
     * 表示断开连接
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.writeAndFlush("[客户端]-" + channel.remoteAddress().toString().substring(1) + "-离开了聊天室");
        System.out.println("ChannelGroup size:" + channels.size());
    }

    /**
     * 表示channel 处于活动的状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端]-" + ctx.channel().remoteAddress().toString().substring(1) + "-上线了");
    }
    /**
     * 表示channel 处于不活动的状态
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("[客户端]-" + ctx.channel().remoteAddress().toString().substring(1) + "-下线了");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();

        channels.forEach( ch -> {
            if(channel != ch){
                ch.writeAndFlush("[用户]-" + ch.remoteAddress().toString().substring(1) + "-发送消息:" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
