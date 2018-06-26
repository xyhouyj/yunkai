package com.hyj.zookeeper.nginx.server;

import java.io.Serializable;

/**
 * Created by houyunjuan on 2018/6/15.
 * 服务端和客户端共用的一个类服务端会把自己的基本信息，包括负载信息，打包成ServerData并写入到zookeeper中，
 * 客户端在计算负载的时候需要到zookeeper中拿到ServerData，并取得服务端的地址和负载信息
 * 并且让serverData可根据balance比对
 */
public class ServerData implements Serializable,Comparable<ServerData>{


    private Integer balance;

    private String host;

    private Integer port;

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "ServerData{" +
                "balance=" + balance +
                ", host='" + host + '\'' +
                ", port=" + port +
                '}';
    }

    public int compareTo(ServerData o){
        return this.getBalance().compareTo(o.getBalance());
    }
}
