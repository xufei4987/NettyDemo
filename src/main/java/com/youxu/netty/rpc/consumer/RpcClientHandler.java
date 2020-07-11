package com.youxu.netty.rpc.consumer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RpcClientHandler extends ChannelInboundHandlerAdapter implements Callable<String> {
    private ChannelHandlerContext context;
    private String result;
    private String msg;
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        lock.lock();
        result = (String) msg;
        condition.signal();//唤醒等待的线程
        lock.unlock();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public String call() throws Exception {
        lock.lock();
        context.writeAndFlush(msg);
        condition.await();
        lock.unlock();
        return result;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }
}
