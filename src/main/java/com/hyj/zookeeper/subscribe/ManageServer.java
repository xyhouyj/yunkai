package com.hyj.zookeeper.subscribe;

import com.alibaba.fastjson.JSON;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;

import java.util.List;

public class ManageServer {

    private String serverPath;

    private String commandPath;

    private String configPath;

    private ServerConfig config;

    private ZkClient zkClient;
    //监听zookeeper servers节点子节点变化的监听器
    private IZkChildListener childListener;
    //监听zookeeper command节点 数据内容变化的监听器
    private IZkDataListener dataListener;
    //工作服务器列表
    List<String> workserverList;
    /**
     *
     * @param serverPath  监控servers服务器的列表变化  需要外部传入
     * @param commandPath  通过command节点执行命令
     * @param configPath
     * @param zkClient
     * @param config  workserve的默认配置
     */
    public ManageServer(String serverPath, String commandPath, String configPath, ZkClient zkClient,ServerConfig config){
        this.serverPath = serverPath;
        this.commandPath = commandPath;
        this.configPath = configPath;
        this.zkClient = zkClient;
        this.config = config;
        //监听servers下面的
        this.childListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                workserverList = currentChilds;
                System.out.println("work server list changed, new list is");
                execList();
            }
        };
        this.dataListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String dataPath, Object data) throws Exception {
                String cmd = new String((byte[]) data);
                System.out.println("cmd : " + cmd);
                exeCmd(cmd);
            }

            @Override
            public void handleDataDeleted(String s) throws Exception {

            }
        }
    }

    private void execList(){
        System.out.println(workserverList.toString());
    }
    //启动的时候  执行两个事件订阅
    private  void  initRunning(){
        zkClient.subscribeDataChanges(commandPath,dataListener);
        zkClient.unsubscribeChildChanges(serverPath,childListener);

    }

    public void start(){
        initRunning();
    }
    //停止的时候 取消两个事件的订阅
    public void stop(){
        zkClient.unsubscribeDataChanges(commandPath,dataListener);
        zkClient.unsubscribeChildChanges(serverPath,childListener);

    }
    //1 list  列出server的所有节点  2 create 创建config节点 3 modify  修改config节点的内容
    public void exeCmd(String cmdType){
            if("list".equals(cmdType)){
                execList();
            }else if("create".equals(cmdType)){
                execCreate();
            }else if("modify".equals(cmdType)){
                execModify();
            }else{
                System.out.println("error Command");
            }
    }

    private void execCreate(){
        if(!zkClient.exists(configPath)){
            try{
                zkClient.createPersistent(configPath, JSON.toJSONString(config).getBytes());
            }catch (ZkNodeExistsException e){//节点已经存在 直接写入数据内容
                zkClient.writeData(configPath,JSON.toJSONString(config).getBytes());
            }catch (ZkNoNodeException e){
                //节点不存在
                String parentDir = configPath.substring(0,configPath.lastIndexOf("/"));
                zkClient.createPersistent(parentDir,true);
                execCreate();
            }
        }
    }

    private void execModify(){
        config.setDbUser(config.getDbUser()+"_modify");
        try{
            zkClient.writeData(configPath,JSON.toJSONString(config).getBytes());
        }catch (ZkNoNodeException e){
            execCreate();
        }

    }
}
