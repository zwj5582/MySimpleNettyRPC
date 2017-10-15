package org.zhongwenjie.rpc.core.client;

import java.lang.reflect.Proxy;

/**
 * 单例
 * 客户端执行者
 * 1、连接服务器
 * 2、生成动态代理类
 */
public class MessageSendExecutor {

    private static class MessageSendExecutorHolder{
        private static MessageSendExecutor INSTANCE=new MessageSendExecutor();
    }

    private MessageSendExecutor(){}

    private String serverAddress;

    private RpcSerializeProtocol rpcSerializeProtocol;

    private RPCServerMapping mapping=RPCServerMapping.INSTANCE();

    public static MessageSendExecutor INSTANCE() {
        return MessageSendExecutorHolder.INSTANCE;
    }

    /**
     * 连接服务器
     * @param serverAddress
     * @param rpcSerializeProtocol
     */
    public void connectServer(String serverAddress,RpcSerializeProtocol rpcSerializeProtocol){
        RPCServerMapping.INSTANCE().connect(serverAddress,rpcSerializeProtocol);
    }

    /**
     * 生成动态代理类
     * @param rpcInterface
     * @param <T>
     * @return
     */
    public static <T> T execute (Class<T> rpcInterface){
        return (T) Proxy.newProxyInstance(rpcInterface.getClassLoader() ,
                new Class<?>[]{rpcInterface},new MessageSendProxy<T>(rpcInterface));
    }

    /**
     * 关闭连接
     */
    public void stop(){
        mapping.stop();
    }
}
