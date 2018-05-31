package com.hyj.zookeeper;

import java.io.IOException;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

public class UpdateNode implements Watcher{
	private static ZooKeeper zookeeper;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		zookeeper = new ZooKeeper("127.0.0.1:2181", 5000, new UpdateNode());
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
		//修改数据
		//同步调用
		Stat setData = zookeeper.setData("/node_6", "123".getBytes(), -1);
		System.out.println("stat:" +setData);
		//异步调用
		zookeeper.setData("/node_6", "123".getBytes(), -1, new IStatCallBack(), null);
		//权限
		/**
		 * 权限模式scheme：ip， digest
		 * 授权对象ID：
		 * 		权限模式为IP 此处对应具体的ip地址
		 *      digest权限模式下：username:Base64(SHA-1(username:password))
		 * 权限（permission）:
		 *      create(C) delete(D) read(R) write(W) admin(A)
		 *  权限组合：scheme+ID+permisson
		 */
		ACL aclIp = new ACL(Perms.READ | Perms.WRITE|Perms.CREATE,new Id("ip", "127.0.0.1"));//符合权限
	}
	static class IStatCallBack implements AsyncCallback.StatCallback{

		public void processResult(int rc, String path, Object ctx, Stat stat) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
