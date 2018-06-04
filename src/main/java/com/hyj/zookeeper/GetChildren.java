package com.hyj.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;

public class GetChildren implements Watcher{
	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new GetChildren());
		System.out.println(zookeeper.getState());
		//客户端与服务端建立异步连接
		Thread.sleep(Integer.MAX_VALUE);
	}

	public void process(WatchedEvent event) {
		// TODO Auto-generated method stub
		System.out.println("收到事件：" +event);
		//在事件处理函数中进行
		if(event.getState() == KeeperState.SyncConnected){//当前事件的zookeeper的状态
			//判断当前客户端状态  如果是已连接
			//事件类型 及相关连的数据节点 event.getPath
			if (event.getType() == EventType.None && null == event.getPath()) {//客户端与服务端建立连接以后 doSomething只执行异常
				try {
					doSomething();
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else{
			if(event.getType() == EventType.NodeChildrenChanged){
				try {
					System.out.println(zookeeper.getChildren(event.getPath(), true));
				} catch (KeeperException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	}

	private void doSomething() throws KeeperException, InterruptedException {
		List<String> children2 = zookeeper.getChildren("/", true);//获取的过程 关注子节点的变化
		System.out.println(children2);
	}
}
