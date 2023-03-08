package com.javaedge.im.codec.utils;

import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.codec.proto.MessageHeader;
import com.javaedge.im.codec.proto.Message;
import io.netty.buffer.ByteBuf;

/**
 * @author JavaEdge
 * 将ByteBuf转化为Message实体，根据私有协议转换
 *
 * 私有协议规则，
 * 请求头：
 * 4位 Command指令 消息的开始，
 * 4位 version
 * 4位 clientType
 * 4位 messageType 消息解析类型
 * 4位 appId
 * 4位 imei长度
 * imei
 * 4位 数据长度
 * data
 * 后续将解码方式加到数据头根据不同的解码方式解码，如pb，json，现在用json字符串
 */
public class ByteBufToMessageUtils {

    private static final int JSON_TYPE = 0x0;

    public static Message transition(ByteBuf in) {

        // 获取command
        int command = in.readInt();

        // 获取version
        int version = in.readInt();

        // 获取clientType
        int clientType = in.readInt();

        // 获取clientType
        int messageType = in.readInt();

        // 获取appId
        int appId = in.readInt();

        // 获取imeiLength
        int imeiLength = in.readInt();

        // 获取bodyLen
        int bodyLen = in.readInt();

        // 数据不够了
        if (in.readableBytes() < bodyLen + imeiLength) {
            // 重置读下标
            in.resetReaderIndex();
            return null;
        }

        byte[] imeiData = new byte[imeiLength];
        in.readBytes(imeiData);
        String imei = new String(imeiData);

        byte[] bodyData = new byte[bodyLen];
        in.readBytes(bodyData);


        Message message = new Message();
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setAppId(appId);
        messageHeader.setClientType(clientType);
        messageHeader.setCommand(command);
        messageHeader.setLength(bodyLen);
        messageHeader.setVersion(version);
        messageHeader.setMessageType(messageType);
        messageHeader.setImei(imei);

        message.setMessageHeader(messageHeader);

        if (messageType == JSON_TYPE) {
            // 将数据解析成 JSON 格式
            String body = new String(bodyData);
            JSONObject parse = (JSONObject) JSONObject.parse(body);
            message.setMessagePack(parse);
        }

        // 更新读下标
        in.markReaderIndex();
        return message;
    }

}
