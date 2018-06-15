package com.wangzai.myNetty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {


    int count;


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;

        System.out.println("servercount : " + (++count));

        String time = "TIME ORDER".equalsIgnoreCase(body) ?
                new SimpleDateFormat("yyyy年-mm月-dd日:hh:ss").format(new Date()).toLowerCase() :
                "BAD TOKEN";

        ByteBuf response = Unpooled.copiedBuffer((time + System.getProperty("line.separator")).getBytes());

        ctx.writeAndFlush(response);

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
