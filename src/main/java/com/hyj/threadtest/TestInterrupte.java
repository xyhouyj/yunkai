package com.hyj.threadtest;

/**
 * Created by houyunjuan on 2018/7/16.
 */
public class TestInterrupte {
    public static void main(String[] args) {
        //sleepThread睡眠1000ms
        final Thread sleepThread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);//抛出异常后 清除标志位
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        };

        //busyThread一直执行死循环
        Thread busyThread = new Thread() {
            @Override
            public void run() {
                while (true) ;
            }
        };
        //启动两个线程
        sleepThread.start();
        busyThread.start();
        //在main方法线程里面  对两个线程调用interrupt方法
        sleepThread.interrupt();
        busyThread.interrupt();
        while (sleepThread.isInterrupted()) ;//一旦返回false才执行下面的
        System.out.println("sleepThread isInterrupted: " + sleepThread.isInterrupted());//false
        System.out.println("busyThread isInterrupted: " + busyThread.isInterrupted());//true
    }


}
