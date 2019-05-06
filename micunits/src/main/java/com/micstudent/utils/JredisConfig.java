package com.micstudent.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName JredisConfig
 * @Description Jredis 读取配置文件注册为bean
 * @Author pet-lsf
 * @Date 2019/4/30 11:24
 **/
@PropertySource("classpath:redis.properties")
@Slf4j
@Data
@Configuration
public class JredisConfig{

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.port1}")
    private int port1;

    @Value("${spring.redis.port2}")
    private int port2;

    @Value("${spring.redis.port3}")
    private int port3;

    @Value("${spring.redis.port4}")
    private int port4;

    @Value("${spring.redis.port5}")
    private int port5;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private int maxWait;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdel;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.block-when-exhausted}")
    private boolean exhausted;

    @Bean
    public JedisPool redisPoolFactory()  throws Exception{
        log.info("JedisPool注入成功！！");
        log.info("redis地址：" + host + ":" + port);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(exhausted);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        JedisPool jedisPool =null;
        try{
            jedisPool=new JedisPool(jedisPoolConfig, host, port, timeout);
        }catch(Exception e){
            jedisPool=new JedisPool(jedisPoolConfig, host, port, timeout, password);
        }
        return jedisPool;
    }
    @Bean
    public JedisCluster redisCluster() throws Exception{
        Set<HostAndPort> set=new HashSet<>();
        set.add(new HostAndPort(host,port));
        set.add(new HostAndPort(host,port1));
        set.add(new HostAndPort(host,port2));
        set.add(new HostAndPort(host,port3));
        set.add(new HostAndPort(host,port4));
        set.add(new HostAndPort(host,port5));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(exhausted);
        // 是否启用pool的jmx管理功能, 默认true
        jedisPoolConfig.setJmxEnabled(true);
        JedisCluster jedisCluster=new JedisCluster(set,jedisPoolConfig);
        return jedisCluster;
    }

}
