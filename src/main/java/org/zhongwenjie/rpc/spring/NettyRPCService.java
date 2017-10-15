package org.zhongwenjie.rpc.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.zhongwenjie.rpc.core.server.MessageRecviceExecutor;

/**
 * 向服务注册接口
 */
public class NettyRPCService implements ApplicationContextAware , InitializingBean {

    private String interfaceName;

    private String ref;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    /**
     * 向服务注册接口
     * 1、获取接口和接口实现类
     * 2、加入接口名与实现类映射Map,完成注册
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
        Object bean = applicationContext.getBean(ref);
        MessageRecviceExecutor.INSTANCE().getHandler().put(interfaceName,bean);
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
