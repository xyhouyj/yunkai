package com.hyj.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

public class CreateSession implements Watcher{
	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new CreateSession());
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
			doSomething();
		}
	}

	private void doSomething() {
		// TODO Auto-generated method stub
		
	}
}
