package com.micstudent;

import com.micstudent.utils.JredisClusterUtils;
import com.micstudent.utils.JredisConfig;
import com.micstudent.utils.JredisUtils;
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

    @Autowired
    private JredisUtils jredisUtils;

    @Autowired
    private JredisClusterUtils jredisClusterUtils;
    public static void main(String args[]){
        SpringApplication.run(Application.class);
    }

    public void afterPropertiesSet() throws Exception {
        jredisUtils.set("apple","www.apple.com",0);
        jredisClusterUtils.set("qq12222","www.qq.com");
    }
}
