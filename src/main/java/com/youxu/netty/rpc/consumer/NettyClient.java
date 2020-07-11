package com.youxu.netty.rpc.consumer;

import com.youxu.netty.rpc.common.Constant;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NettyClient {
    private String host;
    private int port;
    private RpcClientHandler rpcClientHandler;
    private ExecutorService executorService;

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
        rpcClientHandler = new RpcClientHandler();
        executorService = Executors.newSingleThreadExecutor();
        init();
    }

    //返回一个代理对象
    public Object getBean(Class<?> serviceClass){
        return Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{serviceClass}, (proxy, method, args) -> {
            String className = method.getDeclaringClass().getName();
            StringBuilder msgBuilder = new StringBuilder();
            msgBuilder.append(Constant.MAGIC_NUMBER).append("#").append(className).append("#").append(method.getName()).append("#").append(args[0]);
            rpcClientHandler.setMsg(msgBuilder.toString());
            return executorService.submit(rpcClientHandler).get();
        });
    }

    private void init(){
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new StringEncoder()).addLast(new StringDecoder()).addLast(rpcClientHandler);
                        }
                    });
            bootstrap.connect(host, port).sync();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
