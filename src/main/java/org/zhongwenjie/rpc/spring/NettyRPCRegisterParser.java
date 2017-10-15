package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class NettyRPCRegisterParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String id = element.getAttribute("id");
        String ipAddr = element.getAttribute("ipAddr");
        String protocol = element.getAttribute("protocol");

        RootBeanDefinition definition=new RootBeanDefinition();
        definition.setBeanClass(NettyRPCRegister.class);
        definition.setLazyInit(false);

        definition.getPropertyValues().addPropertyValue("ipAddr",ipAddr);
        definition.getPropertyValues().addPropertyValue("protocol",protocol);

        parserContext.getRegistry().registerBeanDefinition(id,definition);

        return definition;

    }

}
