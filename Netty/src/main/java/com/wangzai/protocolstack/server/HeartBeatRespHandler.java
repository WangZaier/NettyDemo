package com.wangzai.protocolstack.server;

import com.wangzai.protocolstack.base.MessageType;
import com.wangzai.protocolstack.struct.Header;
import com.wangzai.protocolstack.struct.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HeartBeatRespHandler extends ChannelInboundHandlerAdapter {
    private final static int MAXIDLECOUNT = 5;
    private volatile Map<String, Integer> idleCount = new ConcurrentHashMap<String, Integer>();

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;
        String nodeIndex = ctx.channel().remoteAddress().toString();
        if (idleCount.containsKey(nodeIndex)) {
//			int i = idleCount.get(nodeIndex);
            idleCount.remove(nodeIndex);
        }
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTHBEAT_REQ.value()) {
            System.out.println("Server recive client hearth beat message: --->" + message);
            //响应心跳
            ctx.writeAndFlush(buildHearthBeatResp());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private NettyMessage buildHearthBeatResp() {
        NettyMessage hearthBeat = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.HEARTHBEAT_RESP.value());
        hearthBeat.setHeader(header);
        return hearthBeat;
    }

    /**
     * 超时重连机制
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            //读写空闲
            if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("读写空闲.");
                String nodeIndex = ctx.channel().remoteAddress().toString();
                int i = 1;
                if (idleCount.containsKey(nodeIndex)) {
                    i = idleCount.get(nodeIndex);
                    if (++i == MAXIDLECOUNT) {
                        ctx.close();//超过一定次数断掉客户端连接
                    } else {
                        idleCount.put(nodeIndex, i);
                    }
                } else {
                    idleCount.put(nodeIndex, i);
                }
                System.out.println(nodeIndex + "空闲次数:[" + i + "]");
            }
        } else {
            ctx.fireUserEventTriggered(evt);
        }
    }

}