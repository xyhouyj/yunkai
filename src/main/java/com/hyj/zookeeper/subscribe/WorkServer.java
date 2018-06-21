package com.hyj.zookeeper.subscribe;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 工作服务器
 */
public class WorkServer {

    private ZkClient zkClient;

    private String configPath;

    private String serverPath;

    private ServerData serverData;

    private ServerConfig serverConfig;

    private IZkDataListener dataListener;//数据监听器

    /**
     *
     * @param configPath  zookeeper中config节点的路径
     * @param serverPath  servers节点的路径
     * @param serverData  当前工作服务器的基本信息
     * @param zkClient    底层用于与zookeeper集群通讯的组件
     * @param initConfig  工作服务器的初始配置
     */
    public WorkServer(String configPath, String serverPath, ServerData serverData, ZkClient zkClient,ServerConfig initConfig){
            this.configPath = configPath;
            this.serverPath = serverPath;
            this.serverData = serverData;
            this.zkClient = zkClient;
            this.serverConfig = initConfig;
            //该监听器 监听zookeeper的config节点的数据改变 在datachange里面做操作
        //config节点存放的是workServer的配置信息  配置信息由manageServer将一个serverConfig对象转成json对象存入的
        //在handleDataChange里面 通过data拿到当前节点的最新数据内容 最新的配置信息  反序列化为serverConfig对象 更新到自己的里面
            this.dataListener = new IZkDataListener() {
                @Override
                public void handleDataChange(String dataPath, Object data) throws Exception {
                    String retJson = new String((byte[]) data);
                    ServerConfig serverConfigLocal = JSON.parseObject(retJson,ServerConfig.class);
                    //调用此方法 执行更新
                    updateConfig(serverConfigLocal);
                    System.out.println("new workServer config is :" + serverConfigLocal.toString());
                }

                @Override
                public void handleDataDeleted(String s) throws Exception {

                }
            };
    }

    public void start(){
        System.out.println("workServer start");
        initRunning();
    }

    /**
     * 服务器停止的时候  取消对config节点的监听
     */
    public  void stop(){
        System.out.println("workServer stop...");
        zkClient.unsubscribeDataChanges(configPath,dataListener);
    }



    //初始化函数   注册自己 订阅config的改变
    public void initRunning(){
        //向zookeeper注册自己
        registMe();
        //订阅config节点的改名
        zkClient.subscribeDataChanges(configPath,dataListener);
    }

    /**
     * 启动时候 注册自己的  其实就是在servers节点中创建一个临时节点
     */
    private void registMe(){
        //节点名称采用 address属性
        String mePath = serverPath.concat("/").concat(serverData.getAddress().toString());
        //执行节点创建 数据内容存入serverData 需要序列号为json
        try {
            zkClient.createEphemeral(mePath, JSON.toJSONString(serverData).getBytes());
        }catch (ZkNoNodeException e){
            //节点没找到 serverPath没有创建
            zkClient.createPersistent(serverPath,true);
            registMe();
        }

    }



    //监听到config节点数据发生改变时候 读取config节点的配置信息 来更新自己的配置信息
    private void updateConfig(ServerConfig serverConfig){
        this.serverConfig = serverConfig;


    }

}
