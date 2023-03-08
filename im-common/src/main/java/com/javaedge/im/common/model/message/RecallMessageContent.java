package com.javaedge.im.common.model.message;

import com.javaedge.im.common.model.ClientInfo;
import lombok.Data;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class RecallMessageContent extends ClientInfo {

    private Long messageKey;

    private String fromId;

    private String toId;

    private Long messageTime;

    private Long messageSequence;

    private Integer conversationType;


//    {
//        "messageKey":419455774914383872,
//            "fromId":"javaedge",
//            "toId":"javaedge4",
//            "messageTime":"1665026849851",
//            "messageSequence":2,
//            "appId": 10000,
//            "clientType": 1,
//            "imei": "web",
//    "conversationType":0
//    }
}
