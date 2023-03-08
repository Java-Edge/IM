package com.javaedge.im.service.user.service;

import com.javaedge.im.common.ResponseVO;
import com.javaedge.im.service.user.dao.ImUserDataEntity;
import com.javaedge.im.service.user.model.req.*;
import com.javaedge.im.service.user.model.resp.GetUserInfoResp;

/**
 * @author JavaEdge
 */
public interface ImUserService {

    ResponseVO importUser(ImportUserReq req);

    ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId , Integer appId);

    ResponseVO deleteUser(DeleteUserReq req);

    /**
     * 修改用户资料
     *
     * @param req
     * @return
     */
    ResponseVO modifyUserInfo(ModifyUserInfoReq req);

    ResponseVO login(LoginReq req);

    ResponseVO getUserSequence(GetUserSequenceReq req);

}
