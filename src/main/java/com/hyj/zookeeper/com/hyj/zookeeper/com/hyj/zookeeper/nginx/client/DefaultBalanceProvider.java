package com.hyj.zookeeper.com.hyj.zookeeper.com.hyj.zookeeper.nginx.client;

import com.hyj.zookeeper.com.hyj.zookeeper.nginx.server.ServerData;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public class DefaultBalanceProvider extends AbstractBalanceProvider<ServerData>{

    private final  String zkServer;

    private final String serverPath;
    private final ZkClient zkClient;
    private static final Integer SESSION_TIME_OUT = 10000;
    private static final Integer CONNECT_TIME_OUT = 10000;
    public DefaultBalanceProvider(String zkServer, String serverPath) {
        this.zkServer = zkServer;
        this.serverPath = serverPath;
        this.zkClient = new ZkClient(this.zkServer,SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
    }

    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
        return null;
    }
    //从zookeeper中拿到所有的工作服务器的列表
    @Override
    protected List<ServerData> getBalanceItems() {
        List<ServerData> sdList = new ArrayList<ServerData>();
        zkClient.getChildren(this.serverPath);
        return null;
    }
}
