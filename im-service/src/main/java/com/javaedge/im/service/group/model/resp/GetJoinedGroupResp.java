package com.javaedge.im.service.group.model.resp;

import com.javaedge.im.service.group.dao.ImGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author JavaEdge
 * @description:
 **/
@Data
public class GetJoinedGroupResp {

    private Integer totalCount;

    private List<ImGroupEntity> groupList;

}
