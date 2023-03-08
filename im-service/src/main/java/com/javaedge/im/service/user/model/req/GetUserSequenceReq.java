package com.javaedge.im.service.user.model.req;

import com.javaedge.im.common.model.RequestBase;
import lombok.Data;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Data
public class GetUserSequenceReq extends RequestBase {

    private String userId;

}
