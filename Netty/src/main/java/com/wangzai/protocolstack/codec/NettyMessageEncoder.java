package com.wangzai.protocolstack.codec;

import com.wangzai.protocolstack.codec.Marshalling.MarshallingCodeCFactory;
import com.wangzai.protocolstack.codec.Marshalling.NettyMarshallingEncoder;
import com.wangzai.protocolstack.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;
import java.util.Map;


/**
 * 消息编码
 *
 * @author wangzai
 * @date 2018/4/25 下午5:55
 */
public class NettyMessageEncoder extends MessageToMessageEncoder<NettyMessage> {
    NettyMarshallingEncoder encoder;

    //验证编码是否为空
    public NettyMessageEncoder() {
        this.encoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg, List<Object> out) throws Exception {

        //验证编码是否为空
        if (msg == null || msg.getHeader() == null) {
            throw new Exception("The encode message is null");
        }

        //默认方式创建一个Bytebuf
        ByteBuf sendBuf = Unpooled.buffer();

        //我们将数据写入缓冲区
        sendBuf.writeInt(msg.getHeader().getCrcCode());
        sendBuf.writeInt(msg.getHeader().getLength());
        sendBuf.writeLong(msg.getHeader().getSessionID());
        sendBuf.writeByte(msg.getHeader().getType());
        sendBuf.writeByte(msg.getHeader().getPriority());
        sendBuf.writeInt(msg.getHeader().getAttachment().size());

        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment().entrySet()) {
            key = param.getKey();//获取key
            keyArray = key.getBytes("UTF-8");//转为byte
            sendBuf.writeInt(keyArray.length);//获取key长度
            sendBuf.writeBytes(keyArray);//写入key
            value = param.getValue();//获取value
            encoder.encode(ctx, value, sendBuf);//编码
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null) {
            encoder.encode(ctx, msg.getBody(), sendBuf);
        }

//      sendBuf.writeInt(0);
        // 在第4个字节出写入Buffer的长度
        int readableBytes = sendBuf.readableBytes();
        sendBuf.setInt(4, readableBytes);

        // 把Message添加到List传递到下一个Handler
        out.add(sendBuf);
    }
}

