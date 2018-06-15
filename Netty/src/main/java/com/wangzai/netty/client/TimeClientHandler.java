package com.wangzai.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {


    byte[] req;

    public TimeClientHandler() {
        req = ("TIME ORDER" + System.getProperty("line.separator")).getBytes();
    }

    /**
     * 当客户端和服务器TCP链路建立成功后，NIO线程会调用channelActive方法
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //发送查询时间的指令给服务端

        ByteBuf message = null;

        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);

        }


    }

    /**
     * 当服务端返回应答消息时调用
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;

        System.out.println("Now is : " + body);
    }

    /**
     * 当发生异常时
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }
}