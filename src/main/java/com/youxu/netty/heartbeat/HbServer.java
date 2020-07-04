package com.youxu.netty.heartbeat;

import com.youxu.netty.groupchar.GroupChartServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class HbServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boss = new NioEventLoopGroup(1);
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .handler(new LoggingHandler(LogLevel.INFO)) //增加日志处理器
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            /**
                             * IdleStateHandler是netty提供的空闲状态处理器
                             * long readerIdleTime：表示多长时间没有读client的数据，就会发送一个心跳检测包检测是否连接
                             * long writerIdleTime： 表示多长时间没有写数据给client，就会发送一个心跳检测包检测是否连接
                             * long allIdleTime： 表示多长时间没有client的读写数据，就会发送一个心跳检测包检测是否连接
                             * 当IdleStateEvent触发后，就会传递给管道的下一个handler进行处理，通过调用下一个handler的
                             * userEventTriggered方法处理（读空闲、写空闲、读写空闲）
                             */
                            ch.pipeline().addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS))
                                    .addLast(new HbServerHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7777).sync();
            System.out.println("服务器启动成功...");
            channelFuture.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
