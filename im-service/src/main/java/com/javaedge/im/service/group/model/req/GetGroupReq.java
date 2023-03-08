package com.javaedge.im.service.group.model.req;

import com.javaedge.im.common.model.RequestBase;
import lombok.Data;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class GetGroupReq extends RequestBase {

    private String groupId;

}
