package com.hyj.threadtest;

/**
 * Created by houyunjuan on 2018/7/16.
 */
public class JoinThread extends Thread {
    private Thread thread;
    public JoinThread(Thread previousThread) {
        this.thread = previousThread;
    }

    @Override
    public void run() {
        try {
            thread.join();
            System.out.println(thread.getName() + " terminated.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
