package com.hyj.distribute.lock;

/**
 * Created by houyunjuan on 2018/5/28.
 */
public class TestRedisLockDemo {
    public static void main(String[] args) {
        RedisService service = new RedisService();
        for (int i = 0; i < 50; i++) {
            ThreadA threadA = new ThreadA(service);
            threadA.start();
        }
    }

}
