package com.hyj.zookeeper.master;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

public class LeaserSelectorZkClient {
	//启动的服务个数
	private static final int CLIENT_QTY = 10;
	//zookeeper拂去其的地址
	private static final String ZOOKEEPER_SERVER = "127.0.0.1:2181";
	public static void main(String[] args) throws Exception {
		//保存所有的zkClient的列表
		List<ZkClient> clients = new ArrayList<ZkClient>();
		//保存所有的服务
		List<WorkerServer> workServers = new ArrayList<WorkerServer>();
		try {
			for (int i = 0; i < CLIENT_QTY; i++) {
				ZkClient client = new ZkClient(ZOOKEEPER_SERVER, 5000, 5000,new SerializableSerializer());
				clients.add(client);
				//创建serverData
				RunningDate runningData = new RunningDate();
				runningData.setCid(Long.valueOf(i));
				runningData.setCname("Client #" +i);
				//创建服务
				WorkerServer workerServer = new WorkerServer(runningData);
				workerServer.setZkClient(client);
				
				workServers.add(workerServer);
				workerServer.start();
			}
			System.out.println("敲回车键推出！\n");
			new BufferedReader(new InputStreamReader(System.in)).readLine();
		} finally{
			System.out.println("shutting down");
			for (WorkerServer workerServer : workServers) {
				try {
					workerServer.stop();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			for (ZkClient client : clients) {
				client.close();
			}
		}
	}
}
