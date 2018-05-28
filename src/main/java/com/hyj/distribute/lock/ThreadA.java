package com.hyj.distribute.lock;

/**
 * Created by houyunjuan on 2018/5/28.
 */
public class ThreadA  extends Thread {
    private RedisService service;

    public ThreadA(RedisService service) {
        this.service = service;
    }

    @Override
    public void run() {
        service.seckill();
    }
}
