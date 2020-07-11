package com.youxu.netty.rpc.provider;

import com.youxu.netty.rpc.common.Constant;
import com.youxu.netty.rpc.common.HelloService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RpcServerHandler extends ChannelInboundHandlerAdapter {

    private static Map<String, Object> instanceMap;
    private static Map<String, Map<String, Method>> registerMap;
    private static HelloService helloService;
    static {
        instanceMap = new HashMap<>();
        registerMap = new HashMap<>();
        helloService = new HelloServiceImpl();
        HashMap<String, Method> methodMap = new HashMap<>();
        for (Method method : helloService.getClass().getMethods()) {
            methodMap.put(method.getName(),method);
        }
        registerMap.put(helloService.getClass().getInterfaces()[0].getName(),methodMap);
        instanceMap.put(helloService.getClass().getInterfaces()[0].getName(),helloService);

    }
    /**
     * 接受消息并调用API
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("收到客户端消息:" + msg);
        //自定义消息格式 youux#com.youxu.netty.rpc.common.HelloService#hello#name
        //youux为魔数 com.youxu.netty.rpc.common.HelloService为接口全限定名 hello为方法 name为参数
        if(((String) msg).startsWith(Constant.MAGIC_NUMBER)){
            String[] strings = ((String) msg).split("#");
            Map<String, Method> methodMap = registerMap.get(strings[1]);
            Method method = methodMap.get(strings[2]);
            Object result = method.invoke(instanceMap.get(strings[1]), strings[3]);
            ctx.writeAndFlush(result);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
