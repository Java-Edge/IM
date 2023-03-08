package com.javaedge.im.tcp.register;

import com.javaedge.im.codec.config.BootstrapConfig;
import com.javaedge.im.common.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author JavaEdge
 */
public class RegistryZK implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(RegistryZK.class);

    private final ZKit zKit;

    private final String ip;

    private final BootstrapConfig.TcpConfig tcpConfig;

    public RegistryZK(ZKit zKit, String ip, BootstrapConfig.TcpConfig tcpConfig) {
        this.zKit = zKit;
        this.ip = ip;
        this.tcpConfig = tcpConfig;
    }

    @Override
    public void run() {
        zKit.createRootNode();

        String tcpPath = Constants.ImCoreZkRoot
                + Constants.ImCoreZkRootTcp
                + "/" + ip
                + ":" + tcpConfig.getTcpPort();
        zKit.createNode(tcpPath);
        log.info("Registry zookeeper tcpPath success, msg=[{}]", tcpPath);

        String webPath =
                Constants.ImCoreZkRoot + Constants.ImCoreZkRootWeb + "/" + ip + ":" + tcpConfig.getWebSocketPort();
        zKit.createNode(webPath);
        log.info("Registry zookeeper webPath success, msg=[{}]", tcpPath);

    }
}
