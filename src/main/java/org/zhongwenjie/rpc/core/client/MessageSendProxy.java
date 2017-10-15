package org.zhongwenjie.rpc.core.client;

import org.zhongwenjie.rpc.model.MessageCallBack;
import org.zhongwenjie.rpc.model.MessageRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 客户端接口代理类
 * @param <T>
 */
public class MessageSendProxy<T> implements InvocationHandler {

    private Class<T> clazz;

    public MessageSendProxy(Class<T> clazz){
        this.clazz=clazz;
    }

    /**
     * 代理类，代理method向服务器发起请求，等待服务器返回结果
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MessageRequest request=new MessageRequest();
        request.setMessageId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParametersVal(args);
        request.setParametersType(method.getParameterTypes());

        MessageSendHandler handler = RPCServerMapping.INSTANCE().getMessageSendHandler();
        MessageCallBack callBack = handler.sendMessage(request);
        return callBack.resultFromRemote();
    }
}
