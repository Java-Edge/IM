package com.javaedge.im.common.route.algorithm.consistenthash;

import com.javaedge.im.common.route.RouteHandle;

import java.util.List;

/**
 * TreeMap实现
 *
 * @author JavaEdge
 */
public class ConsistentHashHandle implements RouteHandle {

    /**
     * 可拓展（后续可变更底层数据结构）
     */
    private AbstractConsistentHash hash;

    public void setHash(AbstractConsistentHash hash) {
        this.hash = hash;
    }

    @Override
    public String routeServer(List<String> values, String key) {
        return hash.process(values,key);
    }
}
