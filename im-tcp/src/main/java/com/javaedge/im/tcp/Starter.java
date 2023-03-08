package com.javaedge.im.tcp;

import com.javaedge.im.tcp.reciver.MessageReceiver;
import com.javaedge.im.tcp.redis.RedisManager;
import com.javaedge.im.tcp.register.RegistryZK;
import com.javaedge.im.tcp.register.ZKit;
import com.javaedge.im.tcp.server.LimServer;
import com.javaedge.im.tcp.server.LimWebSocketServer;
import com.javaedge.im.tcp.utils.MqFactory;
import com.javaedge.im.codec.config.BootstrapConfig;
import org.I0Itec.zkclient.ZkClient;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
/**
 * @author JavaEdge
 */
public class Starter {

    public static void main(String[] args) {
        if (args.length > 0) {
            start(args[0]);
        }
    }

    private static void start(String path) {
        try {
            BootstrapConfig bootstrapConfig = getMyConfig(path);
            new LimServer(bootstrapConfig.getLim()).start();
            new LimWebSocketServer(bootstrapConfig.getLim()).start();

            RedisManager.init(bootstrapConfig);
            MqFactory.init(bootstrapConfig.getLim().getRabbitmq());
            MessageReceiver.init(bootstrapConfig.getLim().getBrokerId() + "");
            registerZK(bootstrapConfig);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(500);
        }
    }

    public static void registerZK(BootstrapConfig config) throws UnknownHostException {
        String hostAddress = InetAddress.getLocalHost().getHostAddress();
        ZkClient zkClient = new ZkClient(config.getLim().getZkConfig().getZkAddr(),
                config.getLim().getZkConfig().getZkConnectTimeOut());
        ZKit zKit = new ZKit(zkClient);
        RegistryZK registryZK = new RegistryZK(zKit, hostAddress, config.getLim());
        Thread thread = new Thread(registryZK);
        thread.start();

    }

    private static BootstrapConfig getMyConfig(String path) throws FileNotFoundException {
        Yaml yaml = new Yaml();
        InputStream inputStream = new FileInputStream(path);
        // 解析配置文件
        return yaml.loadAs(inputStream, BootstrapConfig.class);
    }
}
