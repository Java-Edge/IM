package com.javaedge.im.common.utils;

import com.javaedge.im.common.BaseErrorCode;
import com.javaedge.im.common.exception.ApplicationException;
import com.javaedge.im.common.route.RouteInfo;

public class RouteInfoParseUtil {

    public static RouteInfo parse(String info) {
        try {
            String[] serverInfo = info.split(":");
            return new RouteInfo(serverInfo[0], Integer.parseInt(serverInfo[1]));
        } catch (Exception e) {
            throw new ApplicationException(BaseErrorCode.PARAMETER_ERROR);
        }
    }
}
