package org.zhongwenjie.rpc.model;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MessageCallBack {

    private MessageRequest request;

    private MessageResponse response;

    private Lock lock = new ReentrantLock();

    private Condition waitfinish = lock.newCondition();

    public MessageRequest getRequest() {
        return request;
    }

    public void setRequest(MessageRequest request) {
        this.request = request;
    }

    public MessageResponse getResponse() {
        return response;
    }

    public void setResponse(MessageResponse response) {
        this.response = response;
    }

    /**
     *  1、获取远程调用的结果
     *  2、远程未返回结果将阻塞，直到远程结果返回或超时
     */
    public Object resultFromRemote() throws InterruptedException {
        try {
            lock.lock();
            //设定超时时间，rpc服务器太久没有相应的话，就默认返回空
            waitfinish.await(10*1000L, TimeUnit.MILLISECONDS);
            if (response!=null)
                return response.getResult();
            else
                return null;
        }finally {
            lock.unlock();
        }
    }

    /**
     *  设置返回的结果
     */
    public void setResponseAndSignal(MessageResponse response){
        try {
            lock.lock();
            this.response=response;
            waitfinish.signalAll();
        }finally {
            lock.unlock();
        }
    }


}
