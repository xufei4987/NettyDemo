package com.youxu.netty.rpc.provider;

import com.youxu.netty.rpc.common.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        System.out.println("name = " + name);
        if(name != null && name.trim() != ""){
            return name + " 你好。";
        }
        return "default";
    }
}
