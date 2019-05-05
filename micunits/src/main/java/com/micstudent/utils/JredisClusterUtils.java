package com.micstudent.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.util.List;

/**
 * @ClassName JredisUtils
 * @Description Jredis 帮助类
 * @Author pet-lsf
 * @Date 2019/4/30 11:20
 **/
@Component
@Slf4j
public class JredisClusterUtils {

    private static final String REDIS_OK="ok";

    @Autowired
    private JedisCluster jedis;

    public String get(String key){
        try{
            return jedis.get(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    public byte[] get(byte[] key){
        try{
            return jedis.get(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    public boolean set(String key,String value){
        try{
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean set(byte[] key,byte[] value){
        try{
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean del(String ...keys){
        try{
            jedis.del(keys);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean del(byte[] keys){
        try{
            jedis.del(keys);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean exists(String key){
        try{
            return jedis.exists(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean exists(String ...keys){
        try{
            return jedis.exists(keys)>0;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean flushadb(){
        try{
            jedis.flushAll();
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public long expire(String key,int expireTime){
        try{
            return jedis.expire(key,expireTime);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }
    public long ttl(String key){
        try{
            return jedis.ttl(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return 0;
        }
    }
    public long pttl(String key){
        try{
            return jedis.pttl(key);
        }catch(Exception e) {
            log.error(e.getMessage());
            return 0;
        }
    }
    public long persist(String key){
        try{
            return  jedis.persist(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }
    public boolean setex(String key,String value,int expireTime){
        try{
            jedis.setex(key,expireTime,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }

    public boolean setnx(String key,String value){
        try{
            jedis.setnx(key,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean append(String key,String value){
        Jedis jedis=null;
        try{
            jedis.append(key,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public boolean mdel(String ...keys){
        try{
            jedis.del(keys);
            return  true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }
    }
    public List<String> mget(String ...keys){
        try{
            return jedis.mget(keys);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }
    }
    public long hset(String key,String filed,String value){
        try{
            return jedis.hset(key,filed,value);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }
    }


}
