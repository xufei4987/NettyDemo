package com.youxu.netty.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

public class SimpleTcpServer {
    private static EventExecutorGroup group = new DefaultEventExecutorGroup(8);

    public static void main(String[] args) throws InterruptedException {

        //bossGroup处理connect请求，workerGroup处理read、write等业务处理
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            //创建服务器端的启动对象
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)//设置服务器端通道的实现
                    /**
                     *   服务器端TCP内核模块维护有2个队列，我们称之为A，B吧
                     *   客户端向服务端connect的时候，发送带有SYN标志的包（第一次握手）
                     *   服务端收到客户端发来的SYN时，向客户端发送SYN ACK 确认(第二次握手)
                     *   此时TCP内核模块把客户端连接加入到A队列中，然后服务器收到客户端发来的ACK时（第三次握手）
                     *   TCP没和模块把客户端连接从A队列移到B队列，连接完成，应用程序的accept会返回
                     *   也就是说accept从B队列中取出完成三次握手的连接
                     *  
                     *   A队列和B队列的长度之和是backlog,当A，B队列的长之和大于backlog时，新连接将会被TCP内核拒绝
                     *  
                     *   所以，如果backlog过小，可能会出现accept速度跟不上，A.B 队列满了，导致新客户端无法连接
                     */
                    .option(ChannelOption.SO_BACKLOG, 128)//等待accept的连接的最大个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //这里可以拿到所有建立连接的channel,放入集合中可以做推送业务
                            ChannelPipeline pipeline = ch.pipeline();
                            System.out.println("current线程：" + Thread.currentThread().getName());
                            pipeline.addLast(group,new SimpleTcpServerHandler());
                        }
                    });
            System.out.println("------server is ready!------");
            //绑定一个端口并且同步，生成一个ChannelFuture对象
            ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
            //由于netty的所有操作都是异步的（提交给channel所绑定的NioEventLoop执行），所以这里添加listener其实是添加一个任务给NioEventLoop的taskQueue，
            //在上一个任务结束后自然就调用下一个任务，即addListener中的任务，实现监听机制
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("绑定6666端口成功");
                } else {
                    System.out.println("绑定6666端口失败");
                }
            });

            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } finally {
            //优雅的关闭服务端
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
