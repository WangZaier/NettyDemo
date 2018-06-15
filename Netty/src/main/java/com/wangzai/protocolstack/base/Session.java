package com.wangzai.protocolstack.base;

import io.netty.channel.ChannelId;

public class Session {
    private String sessionId;

    private ChannelId channelId;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public ChannelId getChannelId() {
        return channelId;
    }

    public void setChannelId(ChannelId channelId) {
        this.channelId = channelId;
    }

    @Override
    public int hashCode() {
        // TODO Auto-generated method stub
        return sessionId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this.sessionId == null || obj == null || !(obj instanceof Session)) {
            return false;
        }
        return this.sessionId.equals(((Session) obj).getSessionId());
    }

    @Override
    public String toString() {

        return "Session:[sessionId=" + sessionId + ",channelId=" + channelId.toString() + "]";
    }


}
