package com.javaedge.im.tcp.server;

import com.javaedge.im.tcp.handler.HeartBeatHandler;
import com.javaedge.im.tcp.handler.NettyServerHandler;
import com.javaedge.im.codec.MessageDecoder;
import com.javaedge.im.codec.MessageEncoder;
import com.javaedge.im.codec.config.BootstrapConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JavaEdge
 */
public class LimServer {

    private final static Logger logger = LoggerFactory.getLogger(LimServer.class);

    BootstrapConfig.TcpConfig config;

    EventLoopGroup bossGroup;

    EventLoopGroup workGroup;

    ServerBootstrap server;

    public LimServer(BootstrapConfig.TcpConfig config) {
        this.config = config;
        bossGroup = new NioEventLoopGroup(config.getBossThreadSize());
        workGroup = new NioEventLoopGroup(config.getWorkThreadSize());
        server = new ServerBootstrap();
        server.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                // 服务端可连接队列大小
                .option(ChannelOption.SO_BACKLOG, 10240)
                // 参数表示允许重复使用本地地址和端口
                .option(ChannelOption.SO_REUSEADDR, true)
                // 是否禁用Nagle算法 简单点说是否批量发送数据 true关闭 false开启。 开启的话可以减少一定的网络开销，但影响消息实时性
                .childOption(ChannelOption.TCP_NODELAY, true)
                // 保活开关2h没有数据服务端会发送心跳包
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new MessageDecoder());
                        ch.pipeline().addLast(new MessageEncoder());
                        // 心跳检测，调试时可先注释了 超时后，会调用到下一个 handler 的 userEventTrigger 方法
//                        ch.pipeline().addLast(new IdleStateHandler(0, 0, 1));
                        ch.pipeline().addLast(new HeartBeatHandler(config.getHeartBeatTime()));
                        ch.pipeline().addLast(new NettyServerHandler(config.getBrokerId(), config.getLogicUrl()));
                    }
                });
    }

    public void start() {
        this.server.bind(this.config.getTcpPort());
    }


}
