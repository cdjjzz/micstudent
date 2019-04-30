package com.micstudent;

import com.micstudent.utils.JredisConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName Application
 * @Description TODO
 * @Author pet-lsf
 * @Date 2019/4/30 14:03
 **/
@SpringBootApplication
public class Application  implements InitializingBean {
    @Autowired
    private JredisConfig jredisConfig;
    @Autowired
    private JedisPool jedisPool;
    public static void main(String args[]){
        SpringApplication.run(Application.class);
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println(1);
        System.out.println(jredisConfig.getHost());
        System.out.println(jedisPool.getMaxBorrowWaitTimeMillis());
        System.out.println(jedisPool.getResource());
        System.out.println(jedisPool.getResource().set("google","www.google.com"));
    }
}
