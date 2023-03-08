package com.javaedge.im.common.model;

import lombok.Data;

/**
 * 多端登录的传输对象
 * @author JavaEdge
 */
@Data
public class UserClientDto {

    private Integer appId;

    private Integer clientType;

    private String userId;

    private String imei;

}
