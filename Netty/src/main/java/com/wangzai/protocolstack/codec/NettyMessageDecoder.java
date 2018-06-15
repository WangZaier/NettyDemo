package com.wangzai.protocolstack.codec;

import com.wangzai.protocolstack.codec.Marshalling.MarshallingCodeCFactory;
import com.wangzai.protocolstack.codec.Marshalling.NettyMarshallingDecoder;
import com.wangzai.protocolstack.struct.Header;
import com.wangzai.protocolstack.struct.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.util.HashMap;
import java.util.Map;

/**
 * 解码
 * <p>
 * LengthFieldBasedFrameDecoder解码器支持自动的TCP粘包和半包处理
 * 只需要给出标识信息的长度的字段偏移量和消息长度自身所占字节数.
 * 对于业务解码器调用父类解码方法后,返回的就是整包/null
 * 如果是null说明是一个半包消息,直接返回由I/O线程读取后续的码流
 *
 * @author wangzai
 * @date 2018/4/25 下午6:52
 */
public class NettyMessageDecoder extends LengthFieldBasedFrameDecoder {

    private NettyMarshallingDecoder decoder;

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset,
                               int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment,
                initialBytesToStrip);
        this.decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

    public NettyMessageDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.decoder = MarshallingCodeCFactory.buildMarshallingDecoder();
    }

    /**
     * 解码
     */
    @Override
    public Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);

        if (frame == null) {
            return null;
        }

        System.out.println(ctx.channel().remoteAddress().toString() + ":" + ByteBufUtil.hexDump(frame));

        //新建报文,并填充数据
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setCrcCode(frame.readInt());
        header.setLength(frame.readInt());
        header.setSessionID(frame.readLong());
        header.setType(frame.readByte());
        header.setPriority(frame.readByte());


        /**
         * 读取附件,如果长度为0,代表没有,如果大于0说明有附件
         */
        int size = frame.readInt();
        if (size > 0) {
            Map<String, Object> attach = new HashMap<String, Object>(size);
            int keySize = 0;
            byte[] keyArray = null;
            String key = null;
            //根据长度遍历
            for (int i = 0; i < size; i++) {
                keySize = frame.readInt();//读取数据
                keyArray = new byte[keySize];//内容初始化
                ByteBuf temp = in.readBytes(keyArray);//读取内容
                key = new String(keyArray, "UTF-8");//转换key
                attach.put(key, decoder.decode(ctx, temp));//解码并放入HashMap
            }
            key = null;
            keyArray = null;
            //将HashMap放入报文
            header.setAttachment(attach);
        }

        //如果内容不为空则将他解码后放入报文
        if (frame.readableBytes() > 0) {
            message.setBody(decoder.decode(ctx, frame));
        }
        message.setHeader(header);
        return message;
    }


}
