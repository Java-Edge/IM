package com.javaedge.im.service.user.model.req;

import com.javaedge.im.common.model.RequestBase;
import com.javaedge.im.service.user.dao.ImUserDataEntity;
import lombok.Data;

import java.util.List;


@Data
public class ImportUserReq extends RequestBase {

    private List<ImUserDataEntity> userData;


}
