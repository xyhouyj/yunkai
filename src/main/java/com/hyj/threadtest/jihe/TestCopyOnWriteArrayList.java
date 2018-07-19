package com.hyj.threadtest.jihe;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by houyunjuan on 2018/7/18.
 */
public class TestCopyOnWriteArrayList {

    private static List<Integer> list = new ArrayList<Integer>();
//    private static Vector<Integer> list = new Vector<Integer>();
    public static void main(String[] args) throws InterruptedException {
        final int threadCount = 500;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);//等待500个执行完
        final Semaphore semaphore = new Semaphore(50);//允许50个执行
        long start = System.currentTimeMillis();
        ExecutorService service = Executors.newCachedThreadPool();
        for (int i = 0;i < threadCount;i++){
            final int threadNum = i;
            service.execute(new Runnable() {
                public void run() {
                    try {
                        semaphore.acquire();
                        operation(threadNum);
                        semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        countDownLatch.countDown();
                    }
                }
            });

        }
        countDownLatch.await();
        System.out.println(list.size());
        System.out.println("finish");
        service.shutdown();
        System.out.println("times is ===" + (System.currentTimeMillis() - start));

    }

    public static  void operation(int i) throws InterruptedException {
        Thread.sleep(100);
        list.add(i);
        System.out.println("执行===" +i);
    }
}
