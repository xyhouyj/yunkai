package com.hyj.zookeeper.com.hyj.zookeeper.com.hyj.zookeeper.nginx.client;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public interface BalanceProvider<T> {

    public T getBalanceItem();
}
