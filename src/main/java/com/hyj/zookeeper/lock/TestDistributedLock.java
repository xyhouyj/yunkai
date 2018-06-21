package com.hyj.zookeeper.lock;

import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;

import java.util.concurrent.TimeUnit;

/**
 * Created by houyunjuan on 2018/6/21.
 */
public class TestDistributedLock {
    public static void main(String[] args) {

        final ZkClientExt zkClientExt1 = new ZkClientExt("127.0.0.1:2181", 5000, 5000, new BytesPushThroughSerializer());
//        boolean result = zkClientExt1.exists("/Mutex");
//        if (!result){
//            zkClientExt1.createEphemeralSequential("/Mutex",null);
//        }
        final SimpleDistributedLockMutex mutex1 = new SimpleDistributedLockMutex(zkClientExt1, "/Mutex_two");

        final ZkClientExt zkClientExt2 = new ZkClientExt("127.0.0.1:2181", 5000, 5000, new BytesPushThroughSerializer());
        final SimpleDistributedLockMutex mutex2 = new SimpleDistributedLockMutex(zkClientExt2, "/Mutex_two");

        try {
//            mutex1.acquire();
            mutex1.acquire(20000, TimeUnit.MICROSECONDS);
            System.out.println("Client1 locked");
            long timeStart = System.currentTimeMillis();
            Thread client2Thd = new Thread(new Runnable() {

                public void run() {
                    try {
                        mutex2.acquire(20000, TimeUnit.MICROSECONDS);
                        System.out.println(System.currentTimeMillis() - timeStart);
                        System.out.println("Client2 locked");
                        mutex2.release();
                        System.out.println("Client2 released lock");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client2Thd.start();
            Thread.sleep(5000);
            mutex1.release();
            System.out.println("Client1 released lock");

            client2Thd.join();

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}
