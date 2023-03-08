package com.javaedge.im.common.route.algorithm.consistenthash;

import com.javaedge.im.common.enums.UserErrorCode;
import com.javaedge.im.common.exception.ApplicationException;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author JavaEdge
 */
public class TreeMapConsistentHash extends AbstractConsistentHash {

    private final TreeMap<Long, String> treeMap = new TreeMap<>();

    /**
     * 虚拟节点数量
     */
    private static final int NODE_SIZE = 2;

    @Override
    protected void add(long key, String value) {
        for (int i = 0; i < NODE_SIZE; i++) {
            // 这几个 key 的 value 都一样
            treeMap.put(super.hash("node" + key + i), value);
        }
        treeMap.put(key, value);
    }

    @Override
    protected String getFirstNodeValue(String value) {
        Long hash = super.hash(value);
        SortedMap<Long, String> last = treeMap.tailMap(hash);
        if (!last.isEmpty()) {
            return last.get(last.firstKey());
        }

        if (treeMap.size() == 0) {
            throw new ApplicationException(UserErrorCode.SERVER_NOT_AVAILABLE);
        }

        return treeMap.firstEntry().getValue();
    }

    @Override
    protected void processBefore() {
        treeMap.clear();
    }
}
