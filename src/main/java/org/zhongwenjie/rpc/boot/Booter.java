package org.zhongwenjie.rpc.boot;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Booter {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("classpath:netty-rpc-server.xml");
    }

}
