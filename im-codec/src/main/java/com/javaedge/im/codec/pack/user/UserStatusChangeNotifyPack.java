package com.javaedge.im.codec.pack.user;

import com.javaedge.im.common.model.UserSession;
import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author JavaEdge
 * @version: 1.0
 */
@Data
public class UserStatusChangeNotifyPack {

    private Integer appId;

    private String userId;

    private Integer status;

    private List<UserSession> client;

}
