package com.hyj.zookeeper.nginx.server;

import org.I0Itec.zkclient.ZkClient;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public class ZookeeperRegistContext {

    private String path;

    private ZkClient zkClient;

    private Object data;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    public ZookeeperRegistContext(String path, ZkClient zkClient, Object data) {
        this.path = path;
        this.zkClient = zkClient;
        this.data = data;
    }
}
