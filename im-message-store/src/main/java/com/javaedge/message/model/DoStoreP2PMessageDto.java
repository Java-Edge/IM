package com.javaedge.message.model;

import com.javaedge.im.common.model.message.MessageContent;
import com.javaedge.message.dao.ImMessageBodyEntity;
import lombok.Data;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class DoStoreP2PMessageDto {

    private MessageContent messageContent;

    private ImMessageBodyEntity imMessageBodyEntity;

}
