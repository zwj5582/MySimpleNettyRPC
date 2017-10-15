package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class NettyRPCNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        registerBeanDefinitionParser("service",new NettyRPCServiceParser());
        registerBeanDefinitionParser("register",new NettyRPCRegisterParser());
        registerBeanDefinitionParser("reference",new NettyRPCReferenceParser());
    }
}
