package com.hyj.zookeeper.nginx.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public class DefaultRegistProvide implements RegistProvider{
    //注册  在zookeeper中创建一个临时节点  并写入基本信息
    @Override
    public void regist(Object context) throws Exception {
        //1  path  把自己注册在哪里
        //2 zkCleint
        //3 写入的信息
        ZookeeperRegistContext registContext = (ZookeeperRegistContext) context;
        String path = registContext.getPath();
        ZkClient zkClient = registContext.getZkClient();
       try{
           zkClient.createEphemeral(path,registContext.getData());
       }catch (ZkNoNodeException e){
           String parentDir = path.substring(0,path.lastIndexOf("/"));
           zkClient.createPersistent(parentDir,true);
           regist(context);
       }

    }

    @Override
    public void unRegist(Object context) throws Exception {

    }
}
