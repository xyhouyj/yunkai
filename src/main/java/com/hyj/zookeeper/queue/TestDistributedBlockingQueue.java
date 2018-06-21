package com.hyj.zookeeper.queue;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by houyunjuan on 2018/6/21.
 */
public class TestDistributedBlockingQueue {

    public static void main(String[] args) {


        ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
        int delayTime = 30;

        ZkClient zkClient = new ZkClient("127.0.0.1:2181", 5000, 5000, new SerializableSerializer());
        boolean result = zkClient.exists("/Queue_block");
        if(!result){
            zkClient.createPersistent("/Queue_block");
        }
        final DistributedBlockingQueue<User> queue = new DistributedBlockingQueue<User>(zkClient,"/Queue_block");

        final User user1 = new User();
        user1.setId("1");
        user1.setName("xiao wang");

        final User user2 = new User();
        user2.setId("2");
        user2.setName("xiao wang");

        try {

            delayExector.schedule(new Runnable() {

                public void run() {
                    try {
                        queue.offer(user1);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, delayTime , TimeUnit.SECONDS);

            delayExector.schedule(new Runnable() {
                @Override
                public void run() {
                    try {
                        queue.offer(user2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            },delayTime+30,TimeUnit.SECONDS);
            System.out.println("ready poll!");

            User u1 = (User) queue.poll();
            User u2 = (User) queue.poll();
            System.out.println(u1.toString());
            System.out.println(u2.toString());
            if (user1.getId().equals(u1.getId()) && user2.getId().equals(u2.getId())){
                System.out.println("Success!");
            }
            if(user1.getId().equals(u1.getId())){
                System.out.println("Success");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            delayExector.shutdown();
            try {
                delayExector.awaitTermination(2, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
            }

        }

    }
}
