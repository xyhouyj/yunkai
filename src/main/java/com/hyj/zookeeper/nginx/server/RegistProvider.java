package com.hyj.zookeeper.nginx.server;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public interface RegistProvider {

    public void regist(Object context) throws Exception;

    public void unRegist(Object context) throws Exception;
}
