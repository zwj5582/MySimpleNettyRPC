package org.zhongwenjie.rpc.model;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 客户端请求的消息类
 */
public class MessageRequest implements Serializable {


    private String messageId;//message的唯一标识

    private String className;//接口名称

    private String methodName;//接口调用的方法

    private Class<?>[] parametersType;//方法参数类型

    private Object[] parametersVal;//方法参数


    public MessageRequest(){

    }

    /**
     *
     *getter和setter方法
     *
     */


    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Class<?>[] getParametersType() {
        return parametersType;
    }

    public void setParametersType(Class<?>[] parametersType) {
        this.parametersType = parametersType;
    }

    public Object[] getParametersVal() {
        return parametersVal;
    }

    public void setParametersVal(Object[] parametersVal) {
        this.parametersVal = parametersVal;
    }


    /**
     *
     * 重写toString方法
     *
     */


    @Override
    public String toString() {
        return "MessageRequest{" +
                "messageId='" + messageId + '\'' +
                ", className='" + className + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parametersType=" + Arrays.toString(parametersType) +
                ", parametersVal=" + Arrays.toString(parametersVal) +
                '}';
    }
}
