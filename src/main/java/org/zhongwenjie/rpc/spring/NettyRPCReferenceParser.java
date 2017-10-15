package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class NettyRPCReferenceParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName=element.getAttribute("interfaceName");
        String id=element.getAttribute("id");
        String ipAddr=element.getAttribute("ipAddr");
        String procotolType = element.getAttribute("protocol");

        RootBeanDefinition definition=new RootBeanDefinition();
        definition.setBeanClass(NettyRPCReference.class);
        definition.setLazyInit(false);

        definition.getPropertyValues().addPropertyValue("interfaceName",interfaceName);
        definition.getPropertyValues().addPropertyValue("serverAddress",ipAddr);
        definition.getPropertyValues().addPropertyValue("serializeProtocol",procotolType);

        parserContext.getRegistry().registerBeanDefinition(id,definition);

        return definition;
    }
}
