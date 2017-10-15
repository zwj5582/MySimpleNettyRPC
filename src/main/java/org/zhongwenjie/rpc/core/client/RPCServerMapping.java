package org.zhongwenjie.rpc.core.client;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.zhongwenjie.rpc.core.RPCThreadPoolFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 单例
 * 客户端与服务器的交互
 * 1、完成与服务器的连接
 * 2、定义一个线程池，处理数据发送与接收
 */
public class RPCServerMapping {

    //方法返回到Java虚拟机的可用的处理器数量
    private static final int parallel=Runtime.getRuntime().availableProcessors()*2;

    private EventLoopGroup eventLoopGroup =new NioEventLoopGroup(parallel);

    private static final String DELIMITED=":";

    private RpcSerializeProtocol rpcSerializeProtocol=RpcSerializeProtocol.JDKSERIALIZE;

    private static ThreadPoolExecutor threadPoolExecutor= RPCThreadPoolFactory.INSTANCE().build();

    private MessageSendHandler messageSendHandler=null;

    private Lock lock=new ReentrantLock();

    private Condition signal=lock.newCondition();

    private static volatile RPCServerMapping mapping;

    private RPCServerMapping(){}

    public static RPCServerMapping INSTANCE(){
        if (mapping==null){
            synchronized (RPCServerMapping.class){
                if (mapping==null){
                    mapping=new RPCServerMapping();
                }
            }
        }
        return mapping;
    }

    /**
     * 连接服务器，并将请求任务提交线程池处理
     * @param serverAddress
     * @param rpcSerializeProtocol
     */
    public void connect(String serverAddress,RpcSerializeProtocol rpcSerializeProtocol){
        String[] addr = serverAddress.toLowerCase().split(DELIMITED);
        InetSocketAddress socketAddress=new InetSocketAddress(addr[0],Integer.valueOf(addr[1]));

        threadPoolExecutor.submit(new MessageSendInitializeTask(socketAddress,this,eventLoopGroup));
    }

    /**
     * 回调设置处理完成的MessageSendhandler,发送信号给阻塞等待结果的线程
     * @param messageSendHandler
     */
    public void setMessageSendHandler(MessageSendHandler messageSendHandler){
        try {
            lock.lock();
            this.messageSendHandler=messageSendHandler;
            signal.signalAll();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获得处理完成的MessageSendHandler,未处理完成则阻塞等待当前线程直到返回结果
     * @return
     * @throws InterruptedException
     */
    public MessageSendHandler getMessageSendHandler() throws InterruptedException {
        try {
            lock.lock();
            while (messageSendHandler==null)
                signal.await();
            return messageSendHandler;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 关闭连接，释放资源
     */
    public void stop(){
        messageSendHandler.close();
        threadPoolExecutor.shutdown();
        eventLoopGroup.shutdownGracefully();
    }

}
