package com.lld.im.codec.proto;

import lombok.Data;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
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
