package com.hyj.zookeeper.com.hyj.zookeeper.com.hyj.zookeeper.nginx.client;

import java.util.List;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public abstract  class AbstractBalanceProvider<T> implements BalanceProvider<T>{

   protected  abstract T balanceAlgorithm(List<T> items);//负载均衡算法

   protected  abstract  List<T> getBalanceItems();//获取资源列表

   @Override
    public T getBalanceItem(){
        return balanceAlgorithm(getBalanceItems());
    }

}
