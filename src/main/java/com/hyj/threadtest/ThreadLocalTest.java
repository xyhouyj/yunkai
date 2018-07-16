package com.hyj.threadtest;

/**
 * Created by houyunjuan on 2018/7/16.
 */
public class ThreadLocalTest {

    ThreadLocal<Long> longLocal = new ThreadLocal<Long>(){
        //重写改方法 可以设置初始值
        protected Long initialValue(){
            return Thread.currentThread().getId();
        }
    };
    ThreadLocal<String> stringLocal = new ThreadLocal<String>(){
        protected String initialValue(){
            return Thread.currentThread().getName();
        }
    };

    public void set(){
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong(){
        return longLocal.get();
    }
    public String getString(){
        return stringLocal.get();
    }

    public void remove(){
        longLocal.remove();
        stringLocal.remove();
    }
    public static void main(String[] args) throws InterruptedException {
        final ThreadLocalTest test = new ThreadLocalTest();
//        test.set();
        System.out.println(test.getLong());
        System.out.println(test.getString());

        Thread th = new Thread(){
            public  void run(){
//                test.set();
                System.out.println(test.getLong() +"---------1");
                System.out.println(test.getString()+"---------1");
            };
        };
        th.start();
        th.join();

        System.out.println(test.getLong());
        System.out.println(test.getString());
    }
}
