package com.javaedge.im.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImUrlRouteWayEnum {

    /**
     * 随机
     */
    RANDOM(1, "com.javaedge.im.common.route.algorithm.random.RandomHandle"),


    /**
     * 轮训
     */
    LOOP(2, "com.javaedge.im.common.route.algorithm.loop.LoopHandle"),

    /**
     * 一致性哈希
     */
    CONSISTENT_HASH(3, "com.javaedge.im.common.route.algorithm.consistenthash.ConsistentHashHandle"),
    ;

    private final int code;

    private final String clazz;

    /**
     * 不能用 默认的 enumType b= enumType.values()[i]
     * 因为本枚举是类形式封装
     */
    public static ImUrlRouteWayEnum getHandler(int ordinal) {
        for (int i = 0; i < ImUrlRouteWayEnum.values().length; i++) {
            if (ImUrlRouteWayEnum.values()[i].getCode() == ordinal) {
                return ImUrlRouteWayEnum.values()[i];
            }
        }
        return null;
    }
}
