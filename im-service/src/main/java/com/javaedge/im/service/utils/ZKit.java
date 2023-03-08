package com.javaedge.im.service.utils;

import com.javaedge.im.common.constant.Constants;
import org.I0Itec.zkclient.ZkClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Zookeeper 工具
 *
 * @author JavaEdge
 **/
@Component
public class ZKit {

    private static final Logger logger = LoggerFactory.getLogger(ZKit.class);

    @Resource
    private ZkClient zkClient;

    public List<String> getAllTcpNode() {
        return zkClient.getChildren(Constants.ImCoreZkRoot + Constants.ImCoreZkRootTcp);
    }

    public List<String> getAllWebNode() {
        return zkClient.getChildren(Constants.ImCoreZkRoot + Constants.ImCoreZkRootWeb);
    }
}
