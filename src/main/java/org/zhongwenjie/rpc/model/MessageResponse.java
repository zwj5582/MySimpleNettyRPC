package org.zhongwenjie.rpc.model;


import java.io.Serializable;

/**
 * 服务端响应的消息类
 */
public class MessageResponse implements Serializable {

    private String messageId;//message的唯一标识

    private String error;//服务器响应的错误消息

    private Object result;//返回的结果


    /**
     *
     * getter和setter方法
     *
     */

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }


    /**
     *
     * 重写toString方法
     *
     */


    @Override
    public String toString() {
        return "MessageResponse{" +
                "messageId='" + messageId + '\'' +
                ", error='" + error + '\'' +
                ", result=" + result +
                '}';
    }
}
