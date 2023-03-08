package com.javaedge.im.service.utils;

import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.codec.proto.MessagePack;
import com.javaedge.im.common.constant.Constants;
import com.javaedge.im.common.enums.command.Command;
import com.javaedge.im.common.model.ClientInfo;
import com.javaedge.im.common.model.UserSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 给用户发送消息
 *
 * @author JavaEdge
 */
@Service
@Slf4j
public class MsgProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    UserSessionUtils userSessionUtils;

    private String queueName = Constants.RabbitConstants.MessageService2Im;

    public boolean sendMessage(UserSession session, Object msg) {
        try {
            log.info("send message == " + msg);
            rabbitTemplate.convertAndSend(queueName, session.getBrokerId() + "", msg);
            return true;
        } catch (Exception e) {
            log.error("send error :" + e.getMessage());
            return false;
        }
    }

    //包装数据，调用sendMessage
    public boolean sendPack(String toId, Command command, Object msg, UserSession session) {
        MessagePack messagePack = new MessagePack();
        messagePack.setCommand(command.getCommand());
        messagePack.setToId(toId);
        messagePack.setClientType(session.getClientType());
        messagePack.setAppId(session.getAppId());
        messagePack.setImei(session.getImei());
        JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(msg));
        messagePack.setData(jsonObject);

        String body = JSONObject.toJSONString(messagePack);
        return sendMessage(session, body);
    }

    /**
     * 发送给所有端的方法
     */
    public List<ClientInfo> send2User(String toId, Command command, Object data, Integer appId) {
        List<UserSession> userSession = userSessionUtils.getUserSession(appId, toId);
        List<ClientInfo> list = new ArrayList<>();
        for (UserSession session : userSession) {
            boolean b = sendPack(toId, command, data, session);
            if (b) {
                list.add(new ClientInfo(session.getAppId(), session.getClientType(), session.getImei()));
            }
        }
        return list;
    }

    public void send2User(String toId, Integer clientType, String imei, Command command,
                          Object data, Integer appId) {
        if (clientType != null && StringUtils.isNotBlank(imei)) {
            // 说明是 APP 发起的请求（因为有 IMEI 号）,则只需发给其他设备端
            ClientInfo clientInfo = new ClientInfo(appId, clientType, imei);
            send2UserExceptClient(toId, command, data, clientInfo);
        } else {
            // 后台管理调用的请求，所以发到所有端
            send2User(toId, command, data, appId);
        }
    }

    /**
     * 发送给某个用户的指定客户端
     */
    public void send2User(String toId, Command command, Object data, ClientInfo clientInfo) {
        UserSession userSession = userSessionUtils.getUserSession(clientInfo.getAppId(), toId,
                clientInfo.getClientType(), clientInfo.getImei());
        sendPack(toId, command, data, userSession);
    }

    private boolean isMatch(UserSession sessionDto, ClientInfo clientInfo) {
        return Objects.equals(sessionDto.getAppId(), clientInfo.getAppId())
                && Objects.equals(sessionDto.getImei(), clientInfo.getImei())
                && Objects.equals(sessionDto.getClientType(), clientInfo.getClientType());
    }

    /**
     * 发送给除了某端的其他端
     */
    public void send2UserExceptClient(String toId, Command command, Object data, ClientInfo clientInfo) {
        List<UserSession> userSession = userSessionUtils.getUserSession(clientInfo.getAppId(), toId);
        for (UserSession session : userSession) {
            if (!isMatch(session, clientInfo)) {
                sendPack(toId, command, data, session);
            }
        }
    }
}
