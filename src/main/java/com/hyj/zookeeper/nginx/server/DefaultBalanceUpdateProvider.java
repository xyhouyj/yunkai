package com.hyj.zookeeper.nginx.server;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkBadVersionException;
import org.apache.zookeeper.data.Stat;

/**
 * Created by houyunjuan on 2018/6/15.
 */
public class DefaultBalanceUpdateProvider implements  BalanceUpdateProvider{

    private String serverPath;//workServer在zookeeper中创建的临时节点路径

    private ZkClient zkClient;

    public  DefaultBalanceUpdateProvider(String serverPath,ZkClient zkClient){
        this.serverPath = serverPath;
        this.zkClient = zkClient;
    }
    //从zoo中读取workServer的基本信息  拿到其中的负载计数器 对计数器增加step 并回写到zookeepre中
    @Override
    public boolean addBalance(Integer step) {

        Stat stat = new Stat();
        ServerData sd;
        while (true){
            try{
                //读取信息
                sd = zkClient.readData(this.serverPath,stat);
                //增加
                sd.setBalance(sd.getBalance() + step);
                zkClient.writeData(this.serverPath,sd,stat.getVersion());
                return true;
            }catch (ZkBadVersionException e){
            }catch (Exception e){
                return false;
            }
        }
    }

    @Override
    public boolean reduceBalance(Integer step) {
        Stat stat = new Stat();
        ServerData sd;

        while (true) {
            try {
                sd = zkClient.readData(this.serverPath, stat);
                final Integer currBalance = sd.getBalance();
                //进行减操作
                sd.setBalance(currBalance > step?currBalance-step:0);
                zkClient.writeData(this.serverPath, sd, stat.getVersion());
                return true;
            } catch (ZkBadVersionException e) {
                // ignore
            } catch (Exception e) {
                return false;
            }
        }
    }
}
