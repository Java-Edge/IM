package com.javaedge.im.service.group.model.req;

import com.javaedge.im.common.model.RequestBase;
import lombok.Data;

import java.util.List;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class GetRoleInGroupReq extends RequestBase {

    private String groupId;

    private List<String> memberId;
}
