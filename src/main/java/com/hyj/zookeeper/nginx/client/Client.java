package com.hyj.zookeeper.nginx.client;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public interface Client {


    public void connect() throws Exception;

    public void disConnect() throws Exception;
}
