package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.zhongwenjie.rpc.core.client.RpcSerializeProtocol;
import org.zhongwenjie.rpc.core.server.MessageRecviceExecutor;

/**
 * 注册并启动服务
 */
public class NettyRPCRegister implements InitializingBean , DisposableBean {

    private String ipAddr;

    private String protocol;

    /**
     * 停止服务
     * @throws Exception
     */
    public void destroy() throws Exception {
        MessageRecviceExecutor.INSTANCE().stop();
    }

    /**
     * 获得执行者MessageRecviceExecutor，开启服务
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        MessageRecviceExecutor.INSTANCE()
                .serverAddress(ipAddr)
                .serializeProtocol(RpcSerializeProtocol.valueOf(protocol))
                .start();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
