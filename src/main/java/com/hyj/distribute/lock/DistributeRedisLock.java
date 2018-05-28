package com.hyj.distribute.lock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.JedisException;

import java.util.List;
import java.util.UUID;
/**
 * 为什么采用分布式锁
 * 分布式与单机情况下最大的不同在于其不是多线程而是多进程。
 * 多线程由于可以共享堆内存，因此可以简单的采取内存作为标记存储位置。
 * 而进程之间甚至可能都不在同一台物理机上，因此需要将标记存储在一个所有进程都能看到的地方。
 */

/**
 * Created by houyunjuan on 2018/5/28.
 */
public class DistributeRedisLock {
    private final JedisPool jedisPool;
    public DistributeRedisLock(JedisPool jedisPool){
        this.jedisPool = jedisPool;
    }
/**
 * 原理：
 * Redis为单进程单线程模式，采用队列模式将并发访问变成串行访问，且多客户端对Redis的连接并不存在竞争关系。
 */
    /**
     *
     * @param lockName 锁的key值
     * @param acquireTimeout 获取超时时间
     * @param timeout  锁的超时时间
     * @return
     */
    public String lockWithTimeout(String lockName,long acquireTimeout,long timeout){
        Jedis conn = null;
        String  retIndentifier = null;
        try{
            conn = jedisPool.getResource();
            String identifier = UUID.randomUUID().toString();
            // 锁名，即key值
            String lockKey = "lock:" + lockName;
            // 超时时间，上锁后超过此时间则自动释放锁
            int lockExpire = (int)(timeout / 1000);
            // 获取锁的超时时间，超过这个时间则放弃获取锁
            long end = System.currentTimeMillis() + acquireTimeout;
            while(System.currentTimeMillis() < end){
                //当且仅当key不存在时，set一个key为val的字符串，返回1；若key存在，则什么都不做，返回0。
                if(conn.setnx(lockKey,identifier) == 1){
                    //为key设置一个超时时间，单位为second，超过这个时间锁会自动释放，避免死锁。
                    //如果获取锁之后  还没有来得及设置超时时间 出现死锁？？？？
                    conn.expire(lockKey,lockExpire);
                    // 返回value值，用于释放锁时间确认
                    retIndentifier = identifier;
                    return retIndentifier;
                }
                if (conn.ttl(lockKey) == -1){
                    conn.expire(lockKey,lockExpire);
                }
                try {
                    ThreadA.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }catch (JedisException e){
            e.printStackTrace();
        }finally {
            if(null != conn){
                conn.close();
            }
        }
        return  retIndentifier;
    }

    /**
     * 释放锁
     * @param lockName 锁的key
     * @param identifier    释放锁的标识
     * @return
     */
    public boolean releaseLock(String lockName, String identifier) {
        Jedis conn = null;
        String lockKey = "lock:" + lockName;
        boolean retFlag = false;
        try {
            conn = jedisPool.getResource();
            while (true) {
                // 监视lock，准备开始事务
                conn.watch(lockKey);
                // 通过前面返回的value值判断是不是该锁，若是该锁，则删除，释放锁
                if (identifier.equals(conn.get(lockKey))) {
                    Transaction transaction = conn.multi();
                    transaction.del(lockKey);
                    List<Object> results = transaction.exec();
                    if (results == null) {
                        continue;
                    }
                    retFlag = true;
                }
                conn.unwatch();
                break;
            }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
        return retFlag;
    }
/**
 * setnx(lockkey, 当前时间+过期超时时间) ，如果返回1，则获取锁成功；如果返回0则没有获取到锁，转向2。
 get(lockkey)获取值oldExpireTime ，并将这个value值与当前的系统时间进行比较，如果小于当前系统时间，则认为这个锁已经超时，可以允许别的请求重新获取，转向3。
 计算newExpireTime=当前时间+过期超时时间，然后getset(lockkey, newExpireTime) 会返回当前lockkey的值currentExpireTime。
 判断currentExpireTime与oldExpireTime 是否相等，如果相等，说明当前getset设置成功，获取到了锁。如果不相等，说明这个锁又被别的请求获取走了，那么当前请求可以直接返回失败，或者继续重试。
 在获取到锁之后，当前线程可以开始自己的业务处理，当处理完毕后，比较自己的处理时间和对于锁设置的超时时间，如果小于锁设置的超时时间，则直接执行delete释放锁；如果大于锁设置的超时时间，则不需要再锁进行处理。
 */
    /**
     * setnx  get  getSet  获取锁
     * @param key
     * @param expire
     * @return
     */
    public boolean lock2(String key,int expire){
        Jedis conn = null;
        String  retIndentifier = null;
        try {
            conn = jedisPool.getResource();
            long value = System.currentTimeMillis() + expire;
            long status = conn.setnx(key,String.valueOf(value));
            if(status == 1){
                //获取锁成功
                return true;
            }
            String keyValue = conn.get(key);
            long oldExpireTime = Long.parseLong(keyValue);
            if(oldExpireTime < System.currentTimeMillis()){
                //超时啦
                long newExpireTime = System.currentTimeMillis() + expire;
                Long currentExpireTime = Long.parseLong(conn.getSet(key,String.valueOf(newExpireTime)));
                if(currentExpireTime == oldExpireTime){
                    //获得锁
                    return true;
                }
            }
        }catch (JedisException e){
            e.printStackTrace();
        }
        return false;
    }

    public  void unlock2(String key){
        Jedis conn = null;
        try {
            conn = jedisPool.getResource();
          long oldExpireTime = Long.parseLong(conn.get(key));
          if(oldExpireTime > System.currentTimeMillis()){
              conn.del(key);
          }
        } catch (JedisException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }
}
