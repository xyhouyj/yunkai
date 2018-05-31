package com.hyj.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
/**
 * 异步创建节点
 * @author houyunjuan
 *
 */
public class GetChildrenAsync implements Watcher{
	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new GetChildrenAsync());
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
//		zookeeper.get
		zookeeper.getChildren("/", true, new IChildren2Back(), null);
	}
	
	static class IChildren2Back implements AsyncCallback.Children2Callback{

		public void processResult(int rc, String path, Object ctx,
				List<String> children, Stat stat) {
			// TODO Auto-generated method stub
						StringBuilder sb = new StringBuilder();
						sb.append("rc=" +rc).append("\n");
						sb.append("path=" +path).append("\n");
						sb.append("ctx=" +ctx).append("\n");
						sb.append("children=" +children).append("\n");
						sb.append("stat=" +stat).append("\n");
						System.out.println(sb.toString());
			
		}
		
	}
	
	static class IStringCallback implements AsyncCallback.StringCallback{
		//rc  反悔吗 成功 0
		//ctx 异步调用的上下文 create里传入的值
		public void processResult(int rc, String path, Object ctx, String name) {
			// TODO Auto-generated method stub
			StringBuilder sb = new StringBuilder();
			sb.append("rc=" +rc).append("\n");
			sb.append("path=" +path).append("\n");
			sb.append("ctx=" +ctx).append("\n");
			sb.append("name=" +name).append("\n");
			System.out.println(sb.toString());
		}
		
	}
}
