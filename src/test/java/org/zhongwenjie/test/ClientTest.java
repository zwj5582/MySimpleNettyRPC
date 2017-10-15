package org.zhongwenjie.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.zhongwenjie.rpc.services.SumService;

public class ClientTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext ctx=new ClassPathXmlApplicationContext("classpath:netty-rpc-client.xml");

        SumService service=(SumService)ctx.getBean("sumService");

        Integer sum = service.sum(90, 90);

        System.out.println(sum);

    }

}
