package com.micstudent.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

/**
 * @ClassName JredisUtils
 * @Description  TODO Jredis 帮助类
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
            if(jedis!=null)
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
            log.error(e.getMessage());
            return null;
        }finally {
            relasePool(jedis);
        }
    }
    public byte[] get(byte[] key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.get(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean set(String key,String value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean set(byte[] key,byte[] value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return REDIS_OK.equals(jedis.set(key,value));
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean del(int indexdb,String ...keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            jedis.del(keys);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean del(int indexdb,byte[] keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            jedis.del(keys);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean exists(String key){
        Jedis jedis=null;
        try{
            jedis=getJredis(0);
            return jedis.exists(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean exists(String ...keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(0);
            return jedis.exists(keys)>0;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean flushadb(int ...indexdb){
        Jedis jedis=null;
        try{
            if(indexdb!=null&&indexdb.length>0){
                for (int i = 0; i <indexdb.length ; i++) {
                    jedis=getJredis(0);
                    jedis.flushDB();
                    relasePool(jedis);
                }
            }else{
                jedis.flushAll();
            }
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally {
            relasePool(jedis);
        }
    }

    public long expire(String key,int expireTime,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.expire(key,expireTime);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }finally {
            relasePool(jedis);
        }
    }
    public long ttl(String key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.ttl(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return 0;
        }finally {
            relasePool(jedis);
        }
    }
    public long pttl(String key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.pttl(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return 0;
        }finally {
            relasePool(jedis);
        }
    }
    public long persist(String key,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return  jedis.persist(key);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }finally {
            relasePool(jedis);
        }
    }
    public boolean setex(String key,String value,int expireTime){
        Jedis jedis=null;
        try{
            jedis=getJredis(0);
            jedis.setex(key,expireTime,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally{
            relasePool(jedis);
        }
    }

    public boolean setnx(String key,String value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(0);
            jedis.setnx(key,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally{
            relasePool(jedis);
        }
    }
    public boolean append(String key,String value,int indexdb){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            jedis.append(key,value);
            return true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally{
            relasePool(jedis);
        }
    }
    public boolean mdel(int indexdb,String ...keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            Pipeline pipeline=jedis.pipelined();
            if(keys==null)return true;
            for (int i = 0; i <keys.length ; i++) {
                pipeline.del(keys[i]);
            }
            pipeline.sync();
            return  true;
        }catch(Exception e){
            log.error(e.getMessage());
            return false;
        }finally{
            relasePool(jedis);
        }
    }
    public List<String> mget(int indexdb, String ...keys){
        Jedis jedis=null;
        try{
            jedis=getJredis(indexdb);
            return jedis.mget(keys);
        }catch(Exception e){
            log.error(e.getMessage());
            return null;
        }finally{
            relasePool(jedis);
        }
    }
    public long hset(String key,String filed,String value){
        Jedis jedis=null;
        try{
            jedis=getJredis(0);
            return jedis.hset(key,filed,value);
        }catch(Exception e){
            log.error(e.getMessage());
            return -1;
        }finally{
            relasePool(jedis);
        }
    }


}
