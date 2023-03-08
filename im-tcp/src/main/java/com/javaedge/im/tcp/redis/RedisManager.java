package com.javaedge.im.tcp.redis;

import com.javaedge.im.tcp.reciver.UserLoginMessageListener;
import com.javaedge.im.codec.config.BootstrapConfig;
import org.redisson.api.RedissonClient;

/**
 * @author JavaEdge
 */
public class RedisManager {

    private static RedissonClient redissonClient;

    private static Integer loginModel;

    public static void init(BootstrapConfig config) {
        loginModel = config.getLim().getLoginModel();
        SingleClientStrategy singleClientStrategy = new SingleClientStrategy();
        redissonClient = singleClientStrategy.getRedissonClient(config.getLim().getRedis());
        UserLoginMessageListener userLoginMessageListener = new UserLoginMessageListener(loginModel);
        userLoginMessageListener.listenerUserLogin();
    }

    public static RedissonClient getRedissonClient() {
        return redissonClient;
    }

}
