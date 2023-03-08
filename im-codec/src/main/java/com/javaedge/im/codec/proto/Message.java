package com.javaedge.im.codec.proto;

import lombok.Data;

/**
 * 在 netty 的 channelhandler 处理的就是 message 对象，而不是一个个 bytebuf 了。
 * 将 bytebuf 转化成 message 即可。
 * @author JavaEdge
 */
@Data
public class Message {

    private MessageHeader messageHeader;

    private Object messagePack;

    @Override
    public String toString() {
        return "Message{" +
                "messageHeader=" + messageHeader +
                ", messagePack=" + messagePack +
                '}';
    }
}
