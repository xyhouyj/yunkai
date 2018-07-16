package com.hyj.threadtest;

/**
 * Created by houyunjuan on 2018/7/16.
 */
public class JoinTest {
    public static void main(String[] args) {
        Thread previousThread = Thread.currentThread();
        for (int i = 1; i <= 10; i++) {
            Thread curThread = new JoinThread(previousThread);
            curThread.start();
            previousThread = curThread;
        }
    }
}
