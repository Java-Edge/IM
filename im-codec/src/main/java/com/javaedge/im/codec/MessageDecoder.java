package com.javaedge.im.codec;

import com.javaedge.im.codec.proto.Message;
import com.javaedge.im.codec.utils.ByteBufToMessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * 消息解码类
 *
 * @author JavaEdge
 */
public class MessageDecoder extends ByteToMessageDecoder {

    private static final int PROTO_LENGTH = 28;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if (in.readableBytes() < PROTO_LENGTH) {
            return;
        }

        Message message = ByteBufToMessageUtils.transition(in);
        if (message == null) {
            return;
        }

        out.add(message);
    }
}
