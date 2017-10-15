package org.zhongwenjie.rpc.core.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 向管道pipeline注册所有handler
 */
public class MessageSendInitialize extends ChannelInitializer<SocketChannel> {

    private final static Integer MESSAGE_LENGTH=4;

    /**
     * 向管道pipeline注册所有handler
     * @param socketChannel
     * @throws Exception
     */
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,MESSAGE_LENGTH,0,MESSAGE_LENGTH))
                .addLast(new LengthFieldPrepender(MESSAGE_LENGTH))
                .addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())))
                .addLast(new ObjectEncoder())
                .addLast(new MessageSendHandler());
    }
}
