package com.javaedge.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.common.constant.Constants;
import com.javaedge.im.common.enums.ImConnectStatusEnum;
import com.javaedge.im.common.model.UserSession;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author JavaEdge
 */
@Component
public class UserSessionUtils {

    public Object get;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    //1.获取用户所有的session

    public List<UserSession> getUserSession(Integer appId, String userId) {
        String userSessionKey = appId + Constants.RedisConstants.UserSessionConstants + userId;

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries(userSessionKey);
        List<UserSession> list = new ArrayList<>();
        Collection<Object> values = entries.values();
        for (Object value : values) {
            String str = (String) value;
            UserSession session = JSONObject.parseObject(str, UserSession.class);
            if (Objects.equals(session.getConnectState(), ImConnectStatusEnum.ONLINE_STATUS.getCode())) {
                list.add(session);
            }
        }
        return list;
    }

    //2.获取用户除了本端的session

    //1.获取用户所有的session

    public UserSession getUserSession(Integer appId, String userId, Integer clientType, String imei) {
        String userSessionKey = appId + Constants.RedisConstants.UserSessionConstants + userId;
        String hashKey = clientType + ":" + imei;
        Object o = stringRedisTemplate.opsForHash().get(userSessionKey, hashKey);
        assert o != null;
        return JSONObject.parseObject(o.toString(), UserSession.class);
    }
}
