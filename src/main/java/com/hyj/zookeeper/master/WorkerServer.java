package com.hyj.zookeeper.master;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;

public class WorkerServer {
	
	private volatile boolean running = false;//记录服务器的状态
	
	private ZkClient zkClient;
	// Master节点对应zookeeper中的节点路径
	private static final String MASTER_PATH = "/master";
	// 监听Master节点删除事件
	private IZkDataListener dataListener;
	// 记录当前节点的基本信息
	private RunningDate serverData;
	// 记录集群中Master节点的基本信息
	private RunningDate masterData;
	
	private ScheduledExecutorService delayExecutorService = Executors.newScheduledThreadPool(1);
	 private int delayTime = 5;//延迟事件为5秒
	
	public WorkerServer(RunningDate data){
		this.serverData = data;
		this.dataListener = new IZkDataListener() {
			
			public void handleDataDeleted(String arg0) throws Exception {
				// TODO Auto-generated method stub
			//监测到master节点删除 争抢master
				/*if (masterData!=null && masterData.getCname().equals(serverData.getCname())) {
					//如果master的name 和server 的name相同 说明当前服务器就是上一轮的master
					//此处的判断是对网络抖动造成master下线的一个优化  避免由于再次选出的master 发生变化 造成的资源转移
					takeMaster();
				}else {
					//延迟5秒钟 争抢master
					delayExecutorService.schedule(new Runnable() {
						
						public void run() {
							// TODO Auto-generated method stub
							takeMaster();
						}
					}, delayTime, TimeUnit.SECONDS);
				}*/
				takeMaster();
			}
			
			public void handleDataChange(String arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				
			}
		};
	}
	//服务器启动函数
	public void start() throws Exception{
		//如果已经启动啦
		if (running) {
			throw new Exception("server has startup...");
		}
		running = true;
		//订阅master的删除事件
		zkClient.subscribeDataChanges(MASTER_PATH, dataListener);
		//争抢master权力
		takeMaster();
	}
	//停止服务器
	public void stop() throws Exception{
		if(!running){
			throw new Exception("server has stoped");
		}
		running = false;
		//取消master节点的订阅
		zkClient.unsubscribeDataChanges(MASTER_PATH, dataListener);
		releaseMaster();
	}
	//争抢master权力
	private void takeMaster(){
		//监测服务器状态
		if(!running){
			return;
		}
		//尝试创建
		try {
			zkClient.create(MASTER_PATH, serverData, CreateMode.EPHEMERAL);
			masterData = serverData;
			System.out.println("master is" + serverData.toString());
			//每隔5秒钟 释放一次master  方便看到选举的效果
			delayExecutorService.schedule(new Runnable() {
				
				public void run() {
					if (checkMaster()) {
						releaseMaster();
					}
					
				}
			}, 5, TimeUnit.SECONDS);
		} catch (ZkNodeExistsException e) {
			//节点已经存在
			RunningDate runningDate = zkClient.readData(MASTER_PATH,true);//读取数据
			if (runningDate == null) {//如果没有读取到 说明宕机啦
				takeMaster();
			}else {
				masterData = runningDate;
			}
		}
	}
	//释放master
	private void releaseMaster(){
		//判断自己是否是master  是 就删除自己创建的节点
		if(checkMaster()){
			zkClient.delete(MASTER_PATH);
		}
	}
	//监测自己是否是master
	//到zookeeper中读取master信息 跟自己的节点信息进行对比 如果一样 说明自己是masert
	private boolean checkMaster(){
		try {
			RunningDate eventData = zkClient.readData(MASTER_PATH);
			masterData = eventData;
			if (masterData.getCname().equals(serverData.getCname())) {
				//名称一致  是
				return true;
			}
			return false;
			
		} catch (ZkNoNodeException e) {
			return false;//节点不存在
		}catch (ZkInterruptedException e) {
			return checkMaster();
		}catch (ZkException e) {
			// TODO: handle exception
			return false;
		}
	}
	public ZkClient getZkClient() {
		return zkClient;
	}
	public void setZkClient(ZkClient zkClient) {
		this.zkClient = zkClient;
	}
	public IZkDataListener getDataListener() {
		return dataListener;
	}
	public void setDataListener(IZkDataListener dataListener) {
		this.dataListener = dataListener;
	}
	public RunningDate getServerData() {
		return serverData;
	}
	public void setServerData(RunningDate serverData) {
		this.serverData = serverData;
	}
	public RunningDate getMasterData() {
		return masterData;
	}
	public void setMasterData(RunningDate masterData) {
		this.masterData = masterData;
	}
	public ScheduledExecutorService getDelayExecutorService() {
		return delayExecutorService;
	}
	public void setDelayExecutorService(
			ScheduledExecutorService delayExecutorService) {
		this.delayExecutorService = delayExecutorService;
	}
	public int getDelayTime() {
		return delayTime;
	}
	public void setDelayTime(int delayTime) {
		this.delayTime = delayTime;
	}
	
	
}
