package com.javaedge.im.service.user.model.req;

import com.javaedge.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Data
public class SubscribeUserOnlineStatusReq extends RequestBase {

    private List<String> subUserId;

    private Long subTime;


}
