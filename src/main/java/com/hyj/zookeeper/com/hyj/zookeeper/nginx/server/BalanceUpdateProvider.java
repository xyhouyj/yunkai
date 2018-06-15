package com.hyj.zookeeper.com.hyj.zookeeper.nginx.server;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public interface BalanceUpdateProvider {

    public boolean addBalance(Integer step);

    public boolean reduceBalance(Integer step);
}
