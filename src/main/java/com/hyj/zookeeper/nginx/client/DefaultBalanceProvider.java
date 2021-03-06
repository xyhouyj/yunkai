package com.hyj.zookeeper.nginx.client;

import com.hyj.zookeeper.nginx.server.ServerData;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by houyunjuan on 2018/6/15.
 *
 * 读取zookeeper server节点下的节点信息
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
    //负载均衡算法  依据每一个items的负载均衡信息 将items按照由小到大排序 取出最小的返回
    @Override
    protected ServerData balanceAlgorithm(List<ServerData> items) {
        if (null != items && !items.isEmpty()){
            Collections.sort(items);
            return items.get(0);
        }else{
            return null;
        }

    }
    //从zookeeper中拿到所有的工作服务器的列表
    @Override
    protected List<ServerData> getBalanceItems() {
        List<ServerData> sdList = new ArrayList<ServerData>();
        //拿到所有工作服务器的列表
        List<String> children= zkClient.getChildren(this.serverPath);
        for (String child:children
             ) {
           ServerData serverData = zkClient.readData(this.serverPath +"/" +child);
           sdList.add(serverData);
        }
        return sdList;
    }
}
