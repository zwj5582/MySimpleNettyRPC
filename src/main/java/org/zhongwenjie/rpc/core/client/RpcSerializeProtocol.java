package org.zhongwenjie.rpc.core.client;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 协议类型枚举类
 */
public enum RpcSerializeProtocol {

    JDKSERIALIZE("jdknative"),KRYOSERIALIZE("kryo"), HESSIANSERIALIZE("hessian"), PROTOSTUFFSERIALIZE("protostuff");

    private String serializeProtocol;

    private RpcSerializeProtocol(String serializeProtocol){
        this.serializeProtocol=serializeProtocol;
    }

    public String toString(){
        ReflectionToStringBuilder.setDefaultStyle(ToStringStyle.SHORT_PREFIX_STYLE);
        return ReflectionToStringBuilder.toString(this);
    }

}
