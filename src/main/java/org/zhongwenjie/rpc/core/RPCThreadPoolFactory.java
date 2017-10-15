package org.zhongwenjie.rpc.core;

import java.util.concurrent.*;

/**
 * 线程池工厂
 * 独立出线程池主要是为了应对复杂耗I/O操作的业务，不阻塞netty的handler线程而引入
 */
public class RPCThreadPoolFactory {

    private static final Integer DEFAULT_THREAD_NUM=16;

    private static final ThreadFactory DEFAULT_THREAD_FACTORY=new NamedThreadFactory();

    private static final BlockingQueue<Runnable> DEFAULT_QUEUE=new SynchronousQueue<>();

    private static final RejectedExecutionHandler DEFAULT_HANDLER=new ThreadPoolExecutor.AbortPolicy();

    private Integer threadNum=DEFAULT_THREAD_NUM;

    private ThreadFactory threadFactory=DEFAULT_THREAD_FACTORY;

    private BlockingQueue<Runnable> workQueue=DEFAULT_QUEUE;

    private RejectedExecutionHandler handler=DEFAULT_HANDLER;

    private RPCThreadPoolFactory(){}

    public static RPCThreadPoolFactory INSTANCE(){
        return new RPCThreadPoolFactory();
    }

    public RPCThreadPoolFactory threadNum(Integer threadNum){
        this.threadNum=threadNum;
        return this;
    }

    public RPCThreadPoolFactory threadFactory(ThreadFactory threadFactory){
        this.threadFactory=threadFactory;
        return this;
    }

    public RPCThreadPoolFactory workQueue(BlockingQueue<Runnable> workQueue){
        this.workQueue=workQueue;
        return this;
    }

    public RPCThreadPoolFactory handler(RejectedExecutionHandler handler){
        this.handler=handler;
        return this;
    }

    public ThreadPoolExecutor build(){
        return new ThreadPoolExecutor(threadNum,threadNum,0,TimeUnit.MILLISECONDS,workQueue,threadFactory,handler);
    }

}
