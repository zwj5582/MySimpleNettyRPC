package org.zhongwenjie.rpc.core.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.zhongwenjie.rpc.core.client.RpcSerializeProtocol;

import java.util.Map;

/**
 * 向管道pipeline注册所有handler
 */
public class MessageRecviceChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final Integer MESSAGE_LENGTH=4;

    //接口全类名与实现类映射Map
    private Map<String,Object> handler;

    //序列化协议
    private RpcSerializeProtocol serializeProtocol;

    /**
     * 构造函数
     * @param handler
     * @param serializeProtocol
     */
    public MessageRecviceChannelInitializer(Map<String,Object> handler,RpcSerializeProtocol serializeProtocol){
        this.handler=handler;
        this.serializeProtocol=serializeProtocol;
    }

    /**
     * 向管道pipeline注册所有handler
     * @param socketChannel
     * @throws Exception
     */
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,MESSAGE_LENGTH,0,MESSAGE_LENGTH ))
                .addLast(new LengthFieldPrepender(MESSAGE_LENGTH))
                .addLast(new ObjectEncoder())
                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                .addLast(new MessageRecviceHandler(handler));
    }
}
