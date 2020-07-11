package com.youxu.netty.rpc.consumer;

import com.youxu.netty.rpc.common.HelloService;

public class RpcClientBootstrap {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient("localhost",1234);
        HelloService helloService = (HelloService) nettyClient.getBean(HelloService.class);
        String hello = helloService.hello("游旭");
        System.out.println(hello);
    }
}
