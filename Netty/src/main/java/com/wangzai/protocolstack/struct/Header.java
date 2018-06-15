package com.wangzai.protocolstack.struct;

import java.util.HashMap;
import java.util.Map;

/**
 * 头信息
 *
 * @author wangzai
 * @date 2018/4/25 下午5:09
 */
public class Header {

    //Netty的消息校验码
    private int crcCode = 0xabef0101;

    //报文长度
    private int length;

    //会话ID
    private long sessionID;

    /**
     * 消息类型
     * 0.业务请求消息
     * 1.业务响应消息
     * 2.业务ONE WAY消息(即时请求又是响应)
     * 3.握手请求消息
     * 4.握手应答消息
     * 5.心跳请求消息
     * 6.心跳应答消息
     */
    private byte type;

    //消息优先级(0~255)
    private byte priority;

    //附件,可用于扩展消息头
    private Map<String, Object> attachment = new HashMap<String, Object>();//附件

    public int getCrcCode() {
        return crcCode;
    }

    public void setCrcCode(int crcCode) {
        this.crcCode = crcCode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public long getSessionID() {
        return sessionID;
    }

    public void setSessionID(long sessionID) {
        this.sessionID = sessionID;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }


    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    @Override
    public String toString() {
        return "Header{" +
                "crcCode=" + crcCode +
                ", length=" + length +
                ", sessionID=" + sessionID +
                ", type=" + type +
                ", priority=" + priority +
                ", attachment=" + attachment +
                '}';
    }
}