package com.javaedge.im.common.model.message;

import com.javaedge.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Data
public class MessageContent extends ClientInfo {

    private String messageId;

    private String fromId;

    private String toId;

    private String messageBody;

    private Long messageTime;

    private String extra;

    private Long messageKey;

    private long messageSequence;

}
