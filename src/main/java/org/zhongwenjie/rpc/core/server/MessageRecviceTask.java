package org.zhongwenjie.rpc.core.server;

import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.zhongwenjie.rpc.model.MessageRequest;
import org.zhongwenjie.rpc.model.MessageResponse;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * 异步处理实际的业务逻辑，并返回客户端
 */
public class MessageRecviceTask implements Runnable {

    //接口全类名与实现类映射Map
    private Map<String,Object> handler;

    private MessageRequest request;

    private MessageResponse response;

    private ChannelHandlerContext ctx;

    public MessageRecviceTask(Map<String,Object> handler, MessageRequest request, MessageResponse response, ChannelHandlerContext ctx){
        this.handler=handler;
        this.request=request;
        this.response=response;
        this.ctx=ctx;
    }

    /**
     * 异步处理实际的业务逻辑，并返回客户端
     */
    public void run() {
        try {
            Object result = reflect(request);
            response.setResult(result);
        }catch (Throwable e){
            response.setError(e.toString());
            e.printStackTrace();
        }
        ctx.writeAndFlush(response);

    }

    /**
     * 通过反射调用接口实现类的方法并返回
     * @param request
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private Object reflect(MessageRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String className = request.getClassName();
        Object classImpl = handler.get(className);
        String methodName = request.getMethodName();
        Class<?>[] parametersType = request.getParametersType();
        Object[] parametersVal = request.getParametersVal();
        return MethodUtils.invokeMethod(classImpl,methodName,parametersVal,parametersType);
    }

}
