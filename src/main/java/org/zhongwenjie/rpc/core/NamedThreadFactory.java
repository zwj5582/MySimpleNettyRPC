package org.zhongwenjie.rpc.core;

import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程工厂
 */
public class NamedThreadFactory implements ThreadFactory {

    private static final AtomicInteger threadPoolID = new AtomicInteger(1);

    private AtomicInteger nThreadID = new AtomicInteger(1);

    private final boolean daemoThread;

    private final String prefix;

    private final ThreadGroup threadGroup;

    public NamedThreadFactory(){
        this("MyRPCServer-threadPool-"+threadPoolID.getAndIncrement(),false);
    }

    public NamedThreadFactory(String prefix){
        this(prefix,false);
    }

    public NamedThreadFactory(String prefix,boolean daemo){
        this.prefix = StringUtils.isNotEmpty(prefix) ? prefix+"-thread-" : "";
        daemoThread=daemo;
        SecurityManager s = System.getSecurityManager();
        threadGroup = ( s !=null ) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread=new Thread(threadGroup,r,prefix+nThreadID.getAndIncrement());
        thread.setDaemon(daemoThread);
        return thread;
    }
}
