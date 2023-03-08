package com.javaedge.im.tcp.reciver;

import com.alibaba.fastjson.JSONObject;
import com.javaedge.im.codec.proto.MessagePack;
import com.javaedge.im.common.ClientType;
import com.javaedge.im.common.constant.Constants;
import com.javaedge.im.common.enums.DeviceMultiLoginEnum;
import com.javaedge.im.common.enums.command.SystemCommand;
import com.javaedge.im.common.model.UserClientDto;
import com.javaedge.im.tcp.redis.RedisManager;
import com.javaedge.im.tcp.utils.SessionSocketHolder;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.redisson.api.RTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author JavaEdge
 */
public class UserLoginMessageListener {

    private final static Logger logger = LoggerFactory.getLogger(UserLoginMessageListener.class);

    private final Integer loginModel;

    public UserLoginMessageListener(Integer loginModel) {
        this.loginModel = loginModel;
    }

    public void listenerUserLogin() {
        RTopic topic = RedisManager.getRedissonClient().getTopic(Constants.RedisConstants.UserLoginChannel);
        topic.addListener(String.class, (charSequence, msg) -> {
            logger.info("收到用户上线通知：" + msg);
            UserClientDto dto = JSONObject.parseObject(msg, UserClientDto.class);
            List<NioSocketChannel> nioSocketChannels = SessionSocketHolder.get(dto.getAppId(), dto.getUserId());

            for (NioSocketChannel nioSocketChannel : nioSocketChannels) {
                if (loginModel == DeviceMultiLoginEnum.ONE.getLoginMode()) {
                    Integer clientType = (Integer) nioSocketChannel.attr(AttributeKey.valueOf(Constants.ClientType)).get();
                    String imei = (String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.Imei)).get();

                    writePack(dto, nioSocketChannel, clientType, imei);

                } else if (loginModel == DeviceMultiLoginEnum.TWO.getLoginMode()) {
                    if (dto.getClientType() == ClientType.WEB.getCode()) {
                        continue;
                    }
                    Integer clientType = (Integer) nioSocketChannel.attr(AttributeKey.valueOf(Constants.ClientType)).get();

                    if (clientType == ClientType.WEB.getCode()) {
                        continue;
                    }
                    String imei = (String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.Imei)).get();
                    writePack(dto, nioSocketChannel, clientType, imei);

                } else if (loginModel == DeviceMultiLoginEnum.THREE.getLoginMode()) {

                    Integer clientType = (Integer) nioSocketChannel.attr(AttributeKey.valueOf(Constants.ClientType)).get();
                    String imei = (String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.Imei)).get();
                    if (dto.getClientType() == ClientType.WEB.getCode()) {
                        continue;
                    }

                    Boolean isSameClient = false;
                    if ((clientType == ClientType.IOS.getCode() ||
                            clientType == ClientType.ANDROID.getCode()) &&
                            (dto.getClientType() == ClientType.IOS.getCode() ||
                                    dto.getClientType() == ClientType.ANDROID.getCode())) {
                        isSameClient = true;
                    }

                    if ((clientType == ClientType.MAC.getCode() ||
                            clientType == ClientType.WINDOWS.getCode()) &&
                            (dto.getClientType() == ClientType.MAC.getCode() ||
                                    dto.getClientType() == ClientType.WINDOWS.getCode())) {
                        isSameClient = true;
                    }

                    if (isSameClient && !(clientType + ":" + imei).equals(dto.getClientType() + ":" + dto.getImei())) {
                        MessagePack<Object> pack = new MessagePack<>();
                        pack.setToId((String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.UserId)).get());
                        pack.setUserId((String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.UserId)).get());
                        pack.setCommand(SystemCommand.MUTUALLOGIN.getCommand());
                        nioSocketChannel.writeAndFlush(pack);
                    }
                }
            }
        });
    }

    /**
     * 禁止直接调用 channel.close 剔除客户端，会导致还在传输的数据包不就丢了嘛！
     * 服务端只有心跳超时或者用户主动登出 case，才能直接调用 channel.close
     */
    private void writePack(UserClientDto dto, NioSocketChannel nioSocketChannel, Integer clientType, String imei) {
        if (!(clientType + ":" + imei).equals(dto.getClientType() + ":" + dto.getImei())) {
            MessagePack<Object> pack = new MessagePack<>();
            pack.setToId((String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.UserId)).get());
            pack.setUserId((String) nioSocketChannel.attr(AttributeKey.valueOf(Constants.UserId)).get());
            pack.setCommand(SystemCommand.MUTUALLOGIN.getCommand());
            nioSocketChannel.writeAndFlush(pack);
        }
    }
}
