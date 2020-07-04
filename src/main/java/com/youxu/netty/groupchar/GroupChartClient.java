package com.youxu.netty.groupchar;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChartClient {
    private int port;
    private String host;

    public GroupChartClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() throws InterruptedException {
        System.out.println("main = " + Thread.currentThread().getName());
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup(1);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new MyStringEncoder())
                                    .addLast(new GroupChartClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("");
            Channel channel = channelFuture.channel();
            chart(channel);
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventExecutors.shutdownGracefully();
        }
    }

    private void chart(Channel channel) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String msg = scanner.nextLine();
            channel.writeAndFlush(msg + "\r\n");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new GroupChartClient("127.0.0.1", 8888).startClient();
    }
}
