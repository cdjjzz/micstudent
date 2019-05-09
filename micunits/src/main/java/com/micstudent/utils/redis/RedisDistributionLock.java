package com.micstudent.utils.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisDistributionLock
 * @Description TODO redis 分布式锁(redis 集群环境)
 * @Author pet-lsf
 * @Date 2019/5/8 16:28
 **/
@Slf4j
@Component
public class RedisDistributionLock {

     @Autowired
     private StringRedisTemplate stringRedisTemplate;

     private static final long LOCK_TIMEOUT = 100;

     public synchronized  long lock(String lockKey){
         System.out.println(Thread.currentThread().getName()+"开始加锁");
         //循环获取锁
         while(true){
                Long lock_time=currtTimeForRedis()+LOCK_TIMEOUT+1;
                if(stringRedisTemplate.execute(new RedisCallback<Boolean>() {
                    //加锁操作
                    @Override
                    public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                        //定义序列化方式
                        RedisSerializer<String> serializer = stringRedisTemplate.getStringSerializer();
                        byte[] value = serializer.serialize(lock_time.toString());
                        boolean flag = connection.setNX(lockKey.getBytes(), value);
                        return flag;
                    }
                })
                ){//加锁成功
                    //如果加锁成功
                    System.out.println(Thread.currentThread().getName() +"加锁成功 ++++ 111111");
                    //设置超时时间，释放内存
                    stringRedisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);
                    return lock_time;

                }else{//加锁失败或者锁已经失效
                    String result = stringRedisTemplate.opsForValue().get(lockKey);
                    Long currt_lock_timeout_str = result==null?null:Long.parseLong(result);
                    if (currt_lock_timeout_str != null && currt_lock_timeout_str < System.currentTimeMillis()){
                        //获取上一个锁到期时间，并设置现在的锁到期时间
                        Long old_lock_timeout_Str = Long.
                                valueOf(stringRedisTemplate.opsForValue().getAndSet(lockKey, stringRedisTemplate.toString()));
                        if (old_lock_timeout_Str != null && old_lock_timeout_Str.equals(currt_lock_timeout_str)){
                            //多线程运行时，多个线程签好都到了这里，但只有一个线程的设置值和当前值相同，它才有权利获取锁
                            System.out.println(Thread.currentThread().getName() + "加锁成功 ++++ 22222");
                            //设置超时间，释放内存
                            stringRedisTemplate.expire(lockKey, LOCK_TIMEOUT, TimeUnit.MILLISECONDS);

                            //返回加锁时间
                            return lock_time;
                        }
                    }

                }
         }

     }
     public synchronized  void unlock(String lockKey,long lockValue){
         System.out.println(Thread.currentThread().getName() + "执行解锁==========");//正常直接删除 如果异常关闭判断加锁会判断过期时间
         //获取redis中设置的时间
         String result = stringRedisTemplate.opsForValue().get(lockKey);
         Long currt_lock_timeout_str = result ==null?null:Long.valueOf(result);

         //如果是加锁者，则删除锁， 如果不是，则等待自动过期，重新竞争加锁
         if (currt_lock_timeout_str !=null && currt_lock_timeout_str == lockValue){
             stringRedisTemplate.delete(lockKey);
             System.out.println(Thread.currentThread().getName() + "解锁成功------------------");
         }
     }
    /**
     * 多服务器集群，使用下面的方法，代替System.currentTimeMillis()，
     * 获取redis时间，避免多服务的时间不一致问题！！！
     * @return
     */
    public long currtTimeForRedis(){
        return stringRedisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.time();
            }
        });
    }

}
