package com.javaedge.im.common.model.message;

import com.javaedge.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Data
public class MessageReadedContent extends ClientInfo {

    private long messageSequence;

    private String fromId;

    private String groupId;

    private String toId;

    private Integer conversationType;

}
