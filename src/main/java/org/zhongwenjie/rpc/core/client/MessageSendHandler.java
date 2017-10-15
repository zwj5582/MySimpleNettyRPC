package org.zhongwenjie.rpc.core.client;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.zhongwenjie.rpc.model.MessageCallBack;
import org.zhongwenjie.rpc.model.MessageRequest;
import org.zhongwenjie.rpc.model.MessageResponse;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端发送请求和接收结果的handler
 */
public class MessageSendHandler extends ChannelInboundHandlerAdapter {

    /**
     * 请求ID与MessageCallBack映射
     */
    private ConcurrentHashMap<String,MessageCallBack> mapMessageCallBack=new ConcurrentHashMap<>();

    private Channel channel;

    /**
     * channel注册后，得到channel的引用
     * @param ctx
     * @throws Exception
     */
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel=ctx.channel();
    }

    /**
     * 获取服务端response响应,并设置返回结果
     * @param ctx
     * @param msg
     * @throws Exception
     */
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageResponse response=(MessageResponse) msg;
        String messageId = response.getMessageId();
        MessageCallBack messageCallBack = mapMessageCallBack.get(messageId);
        if (messageCallBack!=null) {
            mapMessageCallBack.remove(messageCallBack);
            messageCallBack.setResponseAndSignal(response);
        }
    }

    /**
     * 被代理类调用发送请求
     * @param request
     * @return
     */
    public MessageCallBack sendMessage(MessageRequest request){
        MessageCallBack messageCallBack =new MessageCallBack();
        messageCallBack.setRequest(request);
        mapMessageCallBack.put(request.getMessageId(),messageCallBack);
        channel.writeAndFlush(request);
        return messageCallBack;
    }

    /**
     * 关闭连接
     */
    public void close(){
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


}
