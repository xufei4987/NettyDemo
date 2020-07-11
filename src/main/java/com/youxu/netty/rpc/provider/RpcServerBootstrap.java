package com.youxu.netty.rpc.provider;

import com.youxu.netty.rpc.common.HelloService;

/**
 * server启动类
 */
public class RpcServerBootstrap {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer(1234);
        nettyServer.startServer0();
    }
}
