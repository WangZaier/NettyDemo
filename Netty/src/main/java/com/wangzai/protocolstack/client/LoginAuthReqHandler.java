package com.wangzai.protocolstack.client;

import com.wangzai.protocolstack.struct.Header;
import com.wangzai.protocolstack.struct.NettyMessage;
import com.wangzai.protocolstack.base.MessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {

        System.out.println(cause.getMessage());
        ctx.close();
    }



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        NettyMessage message = (NettyMessage) msg;
        //如果是应答消息,需要判断是否认证成功
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGING_RESP.value()) {

            byte body = (byte) message.getBody();
            //如果应答结果不是0说明认证失败,关闭链路,重新发起链接
            if (body != (byte) 0) {
                System.out.println("握手失败,关闭连接");
                ctx.close();
            } else {
                System.out.println("login is OK: " + message);
                ctx.fireChannelRead(msg);
            }
        } else {
            //如果不是应答消息,则直接在这里处理
            ctx.fireChannelRead(msg);
        }
    }

    /**
     * 当客户端与服务器TCP三次握手成功以后由客户端构造握手请求消息发送给服务端
     * 由于IP采用白名单认证机制,因此不需要携带消息体,消息题为空
     * 消息类型为"3:握手请求消息".握手请求发送后按照协议规范服务端需要返回握手应答消息
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(buildLoginReq());
    }

    //创建一个Login请求实体
    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGING_REQ.value());
        message.setHeader(header);
        return message;
    }
}
