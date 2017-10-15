package org.zhongwenjie.rpc.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.zhongwenjie.rpc.core.NamedThreadFactory;
import org.zhongwenjie.rpc.core.RPCThreadPoolFactory;
import org.zhongwenjie.rpc.core.client.RpcSerializeProtocol;

import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 单例
 * 定义了一个线程池
 * 1、绑定端口，启动服务
 * 2、提交线程池处理请求的任务
 */
public class MessageRecviceExecutor {

    private static final String DELIMTED=":";

    private ConcurrentHashMap<String,Object> handler=new ConcurrentHashMap<>();

    private static int threadNum=10;

    private static int workQueueNum=-1;

    private String serverAddress;

    private RpcSerializeProtocol serializeProtocol=RpcSerializeProtocol.JDKSERIALIZE;

    private int parallel = Runtime.getRuntime().availableProcessors()*2;

    private EventLoopGroup boss=new NioEventLoopGroup();

    private NamedThreadFactory threadFactory=new NamedThreadFactory("MyNettyRPC ThreadFactory");

    private EventLoopGroup worker=new NioEventLoopGroup(parallel,threadFactory, SelectorProvider.provider());

    private static volatile ThreadPoolExecutor threadPoolExecutor=null;

    private MessageRecviceExecutor(){}

    private static class MessageRecviceExecutorHolder{
        static final MessageRecviceExecutor INSTANCE=new MessageRecviceExecutor();
    }

    public static MessageRecviceExecutor INSTANCE(){
        return MessageRecviceExecutorHolder.INSTANCE;
    }

    /**
     * 初始化线程池并把请求处理任务提交给线程池
     * @param task
     */
    public static void submit(Runnable task){
        if (threadPoolExecutor==null){
            synchronized (MessageRecviceExecutor.class){
                if (threadPoolExecutor==null)
                    threadPoolExecutor= RPCThreadPoolFactory.INSTANCE()
                                .threadNum(threadNum)
                                .workQueue(new SynchronousQueue<>())
                                .build();
            }
        }
        threadPoolExecutor.submit(task);
    }

    /**
     * 启动服务
     * boss主线程接收客户端连接
     * worker线程接收客户端请求并转发请求给线程池处理
     */
    public void start(){
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new MessageRecviceChannelInitializer(handler,serializeProtocol));
            String[] addr = serverAddress.split(DELIMTED);
            String host=addr[0];
            int port=Integer.valueOf(addr[1]);
            ChannelFuture future = bootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 停止服务
     */
    public void stop(){
        boss.shutdownGracefully();
        worker.shutdownGracefully();
    }

    public ConcurrentHashMap<String, Object> getHandler() {
        return handler;
    }

    public void setHandler(ConcurrentHashMap<String, Object> handler) {
        this.handler = handler;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public MessageRecviceExecutor serverAddress(String serverAddress) {
        this.serverAddress = serverAddress;
        return MessageRecviceExecutorHolder.INSTANCE;
    }

    public MessageRecviceExecutor serializeProtocol(RpcSerializeProtocol serializeProtocol){
        this.serializeProtocol=serializeProtocol;
        return MessageRecviceExecutorHolder.INSTANCE;
    }
}
