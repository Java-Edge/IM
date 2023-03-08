package com.javaedge.im.service.friendship.service;

import com.javaedge.im.common.ResponseVO;
import com.javaedge.im.service.friendship.model.req.AddFriendShipGroupMemberReq;
import com.javaedge.im.service.friendship.model.req.DeleteFriendShipGroupMemberReq;

/**
 * @author JavaEdge
 * @description:
 **/
public interface ImFriendShipGroupMemberService {

    public ResponseVO addGroupMember(AddFriendShipGroupMemberReq req);

    public ResponseVO delGroupMember(DeleteFriendShipGroupMemberReq req);

    public int doAddGroupMember(Long groupId, String toId);

    public int clearGroupMember(Long groupId);
}
