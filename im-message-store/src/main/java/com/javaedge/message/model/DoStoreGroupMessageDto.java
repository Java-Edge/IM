package com.javaedge.message.model;

import com.javaedge.im.common.model.message.GroupChatMessageContent;
import com.javaedge.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class DoStoreGroupMessageDto {

    private GroupChatMessageContent groupChatMessageContent;

    private ImMessageBodyEntity imMessageBodyEntity;

}
