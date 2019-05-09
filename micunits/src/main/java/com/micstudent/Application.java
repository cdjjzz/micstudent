package com.micstudent;

import com.micstudent.utils.redis.JredisConfig;
import com.micstudent.utils.redis.JredisUtils;
import com.micstudent.utils.redis.RedisDistributionLock;
import com.micstudent.utils.redis.SpringRedisUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import redis.clients.jedis.JedisPool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private SpringRedisUtils springRedisUtils;

    @Autowired
    private RedisDistributionLock redisLock;

    private static final String LOCK_NO = "redis_distribution_lock_no_";

    private static int i = 0;

    public static void main(String args[]){
        SpringApplication.run(Application.class);
    }

    public void afterPropertiesSet() throws Exception {
        //jredisUtils.set("apple","www.apple.com",0);
        //jredisClusterUtils.set("qq12222","www.qq.com");
        //springRedisUtils.hset("china","sc","四川");
        for (int i=0;i<1000;i++){
            ExecutorService service = Executors.newFixedThreadPool(20);
            service.execute(new Runnable() {
                @Override
                public void run() {
                    task();
                }
            });
        }

    }

    private void task() {
        //加锁时间
        Long lockTime;
        if ((lockTime = redisLock.lock((LOCK_NO+1)+""))!=null){
            //开始执行任务
            System.out.println(Thread.currentThread().getName() + "任务执行中"+(i++));
            //任务执行完毕 关闭锁
            redisLock.unlock((LOCK_NO+1)+"", lockTime);
        }
    }
}
