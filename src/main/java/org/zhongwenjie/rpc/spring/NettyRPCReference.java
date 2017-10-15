package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.zhongwenjie.rpc.core.client.MessageSendExecutor;
import org.zhongwenjie.rpc.core.client.RpcSerializeProtocol;

/**
 * 1、连接服务器
 * 2、向spring容器中放入接口的代理类
 */
public class NettyRPCReference implements FactoryBean , InitializingBean , DisposableBean {

    private String serverAddress;

    private String interfaceName;

    private String serializeProtocol;

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public void setSerializeProtocol(String serializeProtocol) {
        this.serializeProtocol = serializeProtocol;
    }

    /**
     * 关闭连接
     * @throws Exception
     */
    public void destroy() throws Exception {
        MessageSendExecutor.INSTANCE().stop();
    }

    /**
     * 向spring容器中放入接口的代理类
     * @return
     * @throws Exception
     */
    public Object getObject() throws Exception {
        return MessageSendExecutor.execute(getObjectType());
    }

    /**
     * 返回需要接口类型
     * @return
     */
    public Class<?> getObjectType() {
        try {
            Class<?> loadClass = this.getClass().getClassLoader().loadClass(interfaceName);
            return loadClass;
        } catch (ClassNotFoundException e) {
            System.err.println("spring analyze fail!");
        }
        return null;
    }

    /**
     * 通知spring容器，放入的代理类为单例
     * @return
     */
    public boolean isSingleton() {
        return true;
    }

    /**
     * 连接服务器
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        MessageSendExecutor.INSTANCE().connectServer(serverAddress, RpcSerializeProtocol.valueOf(serializeProtocol));
    }


}
