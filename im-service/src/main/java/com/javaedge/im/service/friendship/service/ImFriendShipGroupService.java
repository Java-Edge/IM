package com.javaedge.im.service.friendship.service;

import com.javaedge.im.common.ResponseVO;
import com.javaedge.im.service.friendship.dao.ImFriendShipGroupEntity;
import com.javaedge.im.service.friendship.model.req.AddFriendShipGroupReq;
import com.javaedge.im.service.friendship.model.req.DeleteFriendShipGroupReq;

/**
 * @author JavaEdge
 * @description:
 **/
public interface ImFriendShipGroupService {

    public ResponseVO addGroup(AddFriendShipGroupReq req);

    public ResponseVO deleteGroup(DeleteFriendShipGroupReq req);

    public ResponseVO<ImFriendShipGroupEntity> getGroup(String fromId, String groupName, Integer appId);

    public Long updateSeq(String fromId, String groupName, Integer appId);
}
