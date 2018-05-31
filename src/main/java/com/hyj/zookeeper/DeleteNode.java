package com.hyj.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class DeleteNode implements Watcher{
	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new DeleteNode());
		System.out.println(zookeeper.getState());
		//客户端与服务端建立异步连接
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		System.out.println("收到事件：" +event);
		//在事件处理函数中进行
		if(event.getState() == KeeperState.SyncConnected){
			//判断当前客户端状态  如果是已连接
			try {
				doSomething();
			} catch (KeeperException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void doSomething() throws KeeperException, InterruptedException {
		zookeeper.delete("/node_6", -1);
		zookeeper.exists("/node_8", true);//第一个参数是节点 第二个参数 是否注册事件监听器
		//如果注册 
		//将会收到event.getType ==EventType.NodeCreated   NodeDataChanged  NodeDelete等节点事件
	}
	//异步调用 VoidCallback
}
