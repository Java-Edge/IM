package com.javaedge.im.tcp.publish;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.codec.proto.Message;
import com.javaedge.im.codec.proto.MessageHeader;
import com.javaedge.im.common.constant.Constants;
import com.javaedge.im.common.enums.command.CommandType;
import com.javaedge.im.tcp.utils.MqFactory;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JavaEdge
 */
@Slf4j
public class MqMessageProducer {

    public static void sendMessage(Message message, Integer command) {
        Channel channel = null;
        String com = command.toString();
        String commandSub = com.substring(0, 1);
        CommandType commandType = CommandType.getCommandType(commandSub);
        String channelName = "";
        if (commandType == CommandType.MESSAGE) {
            channelName = Constants.RabbitConstants.Im2MessageService;
        } else if (commandType == CommandType.GROUP) {
            channelName = Constants.RabbitConstants.Im2GroupService;
        } else if (commandType == CommandType.FRIEND) {
            channelName = Constants.RabbitConstants.Im2FriendshipService;
        } else if (commandType == CommandType.USER) {
            channelName = Constants.RabbitConstants.Im2UserService;
        }

        try {
            channel = MqFactory.getChannel(channelName);

            JSONObject o = (JSONObject) JSON.toJSON(message.getMessagePack());
            o.put("command", command);
            o.put("clientType", message.getMessageHeader().getClientType());
            o.put("imei", message.getMessageHeader().getImei());
            o.put("appId", message.getMessageHeader().getAppId());
            channel.basicPublish(channelName, "",
                    null, o.toJSONString().getBytes());
        } catch (Exception e) {
            log.error("发送消息出现异常：{}", e.getMessage());
        }
    }

    public static void sendMessage(Object message, MessageHeader header, Integer command) {
        Channel channel = null;
        String com = command.toString();
        String commandSub = com.substring(0, 1);
        CommandType commandType = CommandType.getCommandType(commandSub);
        String channelName = "";
        if (commandType == CommandType.MESSAGE) {
            channelName = Constants.RabbitConstants.Im2MessageService;
        } else if (commandType == CommandType.GROUP) {
            channelName = Constants.RabbitConstants.Im2GroupService;
        } else if (commandType == CommandType.FRIEND) {
            channelName = Constants.RabbitConstants.Im2FriendshipService;
        } else if (commandType == CommandType.USER) {
            channelName = Constants.RabbitConstants.Im2UserService;
        }

        try {
            channel = MqFactory.getChannel(channelName);

            JSONObject o = (JSONObject) JSON.toJSON(message);
            o.put("command", command);
            o.put("clientType", header.getClientType());
            o.put("imei", header.getImei());
            o.put("appId", header.getAppId());
            channel.basicPublish(channelName, "",
                    null, o.toJSONString().getBytes());
        } catch (Exception e) {
            log.error("发送消息出现异常：{}", e.getMessage());
        }
    }

}
