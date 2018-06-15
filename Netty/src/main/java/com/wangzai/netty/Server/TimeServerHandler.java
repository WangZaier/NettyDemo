package com.wangzai.netty.Server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    private int count;//计数器


    //读取事件
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        String body = (String) msg;

        //每次读取消息输出一次
        System.out.println("server:" + (++count));

        String currentTime = "TIME ORDER".equalsIgnoreCase(body) ?
                new SimpleDateFormat("yyyy年-mm月-dd日:hh:ss").format(new Date(System.currentTimeMillis())).toString() :
                "BAD ORDER";
        //response
        ByteBuf resp = Unpooled.copiedBuffer((currentTime + System.getProperty("line.separator")).getBytes());
        //异步发送应答消息给客户端: 这里并没有把消息直接写入SocketChannel,而是放入发送缓冲数组中
        ctx.writeAndFlush(resp);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        //将发送缓冲区中数据全部写入SocketChannel
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //释放资源
        ctx.close();
    }
}