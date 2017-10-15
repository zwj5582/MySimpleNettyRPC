package org.zhongwenjie.rpc.core.client;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * 连接服务器，异步设置
 */
public class MessageSendInitializeTask implements Runnable {

    private InetSocketAddress address;

    private RPCServerMapping rpcServerMapping;

    private EventLoopGroup eventLoopGroup;

    public MessageSendInitializeTask(InetSocketAddress address, RPCServerMapping mapping,
                                     EventLoopGroup eventLoopGroup){
        this.address=address;
        this.rpcServerMapping=mapping;
        this.eventLoopGroup=eventLoopGroup;
    }

    @Override
    public void run() {
        Bootstrap bootstrap=new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new MessageSendInitialize());
        ChannelFuture channelFuture = bootstrap.connect(address);
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                if (channelFuture.isSuccess()){
                    rpcServerMapping.setMessageSendHandler(channelFuture.channel().pipeline().get(MessageSendHandler.class));
                }
            }
        });
    }
}
