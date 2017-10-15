package org.zhongwenjie.rpc.core.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.zhongwenjie.rpc.model.MessageRequest;
import org.zhongwenjie.rpc.model.MessageResponse;

import java.util.Map;

/**
 * 处理客户端发来的请求数据
 */
public class MessageRecviceHandler extends ChannelInboundHandlerAdapter {

    private Channel channel;

    //接口全类名与实现类映射Map
    private Map<String,Object> handler;

    /**
     * 构造函数，赋值handler
     * @param handler
     */
    public MessageRecviceHandler(Map<String,Object> handler){
        this.handler=handler;
    }

    /**
     * channel注册后，获得channel
     * @param ctx
     * @throws Exception
     */
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel=ctx.channel();
    }

    /**
     * 接收到客户端数据，转发给实际执行者MessageRecviceExecutor
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageRequest request=(MessageRequest)msg;

        System.out.println("接收到客户端请求数据： "+request.toString());

        MessageResponse response=new MessageResponse();
        response.setMessageId(request.getMessageId());
        MessageRecviceExecutor.submit(new MessageRecviceTask(handler,request,response,ctx));
    }

    /**
     * channelRead完成，打印"客户端请求数据已读取"
     * @param ctx
     * @throws Exception
     */
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端请求数据已读取");
    }

    /**
     * 捕捉到异常，关闭该channel
     * @param ctx
     * @param cause
     * @throws Exception
     */
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
