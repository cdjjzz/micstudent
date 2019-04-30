package com.micstudent.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @ClassName JredisUtils
 * @Description Jredis 帮助类
 * @Author pet-lsf
 * @Date 2019/4/30 11:20
 **/
@Component
@Slf4j
public class JredisUtils {

    private static final String REDIS_OK="ok";

    @Autowired
    private JedisPool jedisPool;

    public void relasePool(Jedis jedis){
        try{
            jedis.close();
        }catch(Exception e){

        }
    }
    public Jedis getJredis(int indexdb){
        try{
            Jedis jedis=jedisPool.getResource();
            jedis.select(indexdb);
            return jedis;
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    public String get(String key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.get(key);
        }catch(Exception e){
            relasePool(jedis);
            log.error(e.getMessage());
            return null;
        }
    }
    public byte[] get(byte[] key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.get(key);
        }catch(Exception e){
            relasePool(jedis);
            log.error(e.getMessage());
            return null;
        }
    }
    public boolean set(String key,String value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            relasePool(jedis);
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean set(byte[] key,byte[] value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            relasePool(jedis);
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean del(int indexdb,String ...keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            jedis.del(keys);
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean del(int indexdb,byte[] keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            jedis.del(keys);
            return true;
        }catch(Exception e){
            return false;
        }
    }

}
