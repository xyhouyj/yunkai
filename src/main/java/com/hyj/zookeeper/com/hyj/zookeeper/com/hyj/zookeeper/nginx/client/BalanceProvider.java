package com.hyj.zookeeper.com.hyj.zookeeper.com.hyj.zookeeper.nginx.client;

/**
 * Created by houyunjuan on 2018/6/15.
 * 提供负载均衡算法
 */
public interface BalanceProvider<T> {

    public T getBalanceItem();
}
