package com.wangzai.protocolstack.struct;


/**
 * 报文数据结构
 *
 * @author wangzai
 * @date 2018/4/25 下午5:13
 */
public class NettyMessage {

    //消息头
    private Header header;

    //消息体
    private Object body;

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}

