package com.hyj.zookeeper.test;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * Created by houyunjuan on 2018/6/21.
 */
public class CreateGroup implements Watcher {
    private static final int SESSION_TIMEOUT=10000;

    private ZooKeeper zk;
    private CountDownLatch connectedSignal=new CountDownLatch(1);
    @Override
    public void process(WatchedEvent event) {
        if(event.getState()== Event.KeeperState.SyncConnected){
            System.out.println("触发。。。。。。。。。");
            connectedSignal.countDown();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        CreateGroup createGroup = new CreateGroup();
        createGroup.connect("127.0.0.1:2181");
        createGroup.create("home");
        createGroup.close();
    }

    private void close() throws InterruptedException {
        zk.close();
    }

    private void create(String groupName) throws KeeperException, InterruptedException {
        String path="/"+groupName;
        if(zk.exists(path, false)== null){
            zk.create(path, null/*data*/, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        System.out.println("Created:"+path);
    }

    private void connect(String hosts) throws IOException, InterruptedException {
        zk = new ZooKeeper(hosts, SESSION_TIMEOUT, this);
        connectedSignal.await();
    }
    /**
     * 在这个例子中，CreateGroup是一个Watcher对象，因此我们将它传递给ZooKeeper的构造函数。
     当一个ZooKeeper的实例被创建时，会启动一个线程连接到ZooKeeper服务。
     由于对构造函数的调用是立即返回的，因此在使用新建的ZooKeeper对象之前一定要等待其与ZooKeeper服务之间的连接建立成功。
     我们使用Java的CountDownLatch类来阻止使用新建的ZooKeeper对象，直到这个ZooKeeper对象已经准备就绪。
     这就是Watcher类的用途，在它的接口中只有一个方法：
     　　　　public void process(WatcherEvent event);
     客 户端已经与ZooKeeper建立连接后，Watcher的process()方法会被调用，参数是一个表示该连接的事件。
     在接收到一个连接事件（由 Watcher.Event.KeeperState的枚举型值SyncConnected来表示）时，我们通过调用CountDownLatch的countDown()方法来递减它的计数器。
     锁存器(latch)被创建时带有一个值为1的计数器，用于表示在它释放所有等待线程之前需要发生的事件数。
     在调用一欢countDown()方法之后，计数器的值变为0，则await()方法返回。
     */
}
