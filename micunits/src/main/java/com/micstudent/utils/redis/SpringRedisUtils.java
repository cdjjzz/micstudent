package com.micstudent.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName SpringRedisUtils
 * @Description 使用spring 提供的RedisTemplete
 * @Author pet-lsf
 * @Date 2019/5/8 13:42
 **/
@Slf4j
@Component
public class SpringRedisUtils {
    private static final String REDIS_OK="ok";

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String get(String key){
        try{
            return stringRedisTemplate.opsForValue().get(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }finally {
        }
    }
    public boolean set(String key,String value){
        try{
            stringRedisTemplate.opsForValue().set(key,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
        }
    }
    public boolean del(String ...keys){
        try{
            stringRedisTemplate.delete(Arrays.asList(keys));
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
        }
    }
    public boolean exists(String key){
        try{
            return stringRedisTemplate.hasKey(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
        }
    }
    public boolean flushadb(){
        try{
            stringRedisTemplate.execute(new RedisCallback() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                     connection.flushDb();
                     return  "ok";
                }
            });
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
        }
    }

    public boolean expire(String key,int expireTime,int indexdb){
        try{
            return stringRedisTemplate.expire(key,expireTime, TimeUnit.MINUTES);
        }catch(Exception e){
            log.error(e.getMessage());
            return  false;
        }finally {
        }
    }

    public long hset(String key,String filed,String value) {
        try {
            stringRedisTemplate.opsForHash().put(key, filed, value);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return -1;
        } finally {

        }
    }


}
