package com.youxu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * SimpleChannelInboundHandler 会帮我们把消息类型封装为我们指定的泛型类型
 */
public class SimpleHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if(msg instanceof HttpRequest && !((HttpRequest) msg).uri().contains("favicon.ico")){
            //这里打印2次是由于浏览器会发送 favicon.ico请求来请求图标，过滤即可
            System.out.println("msg的类型是：" + msg.getClass());
            System.out.println("客户端地址为：" + ctx.channel().remoteAddress().toString().substring(1));

            ByteBuf byteBuf = Unpooled.copiedBuffer("<b>hello, 我是http服务器</b>", Charset.forName("GBK"));

            DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);

            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/html");
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LENGTH,byteBuf.readableBytes());
            defaultFullHttpResponse.headers().set(HttpHeaderNames.CONTENT_LANGUAGE,"zh-cn");

            ctx.writeAndFlush(defaultFullHttpResponse);
        }
    }
}
