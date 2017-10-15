package org.zhongwenjie.rpc.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class NettyRPCServiceParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        String interfaceName = element.getAttribute("interfaceName");
        String ref = element.getAttribute("ref");

        RootBeanDefinition definition=new RootBeanDefinition();
        definition.setBeanClass(NettyRPCService.class);
        definition.setLazyInit(false);

        definition.getPropertyValues().addPropertyValue("interfaceName",interfaceName);
        definition.getPropertyValues().addPropertyValue("ref",ref);

        parserContext.getRegistry().registerBeanDefinition(interfaceName,definition);

        return definition;
    }
}
