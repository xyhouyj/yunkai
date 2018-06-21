package com.hyj.zookeeper.lock;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.apache.zookeeper.CreateMode;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by houyunjuan on 2018/6/20.
 * 与zookeeper交互
 */
public class BaseDistributedLock {

    private final ZkClientExt client;
    private final String path;
    private final String basePath;
    private final String lockName;
    private static final Integer MAX_RETRY_COUNT = 10;

    public BaseDistributedLock(ZkClientExt client, String path, String lockName) {
        this.client = client;
        this.path = path.concat("/").concat(lockName);
        this.basePath = path;
        this.lockName = lockName;
        client.createPersistent(path, true);
    }
    //删除成功获取锁之后 所创建的按个顺序节点
    private void deleteOurPath(String ourPath){
        client.delete(ourPath);
    }
    //创建临时顺序节点
    private String createLockNode(ZkClient client, String path) throws Exception{
//        return client.create(path,null, CreateMode.EPHEMERAL_SEQUENTIAL);
        return client.createEphemeralSequential(path,null);
    }

    // 等待比自己次小的顺序节点的删除
    private boolean waitToLock(long startMillis, Long millisToWait, String ourPath) throws Exception{

        boolean  haveTheLock = false;
        boolean  doDelete = false;

        try {

            while ( !haveTheLock ) {
                // 获取/locker下的经过排序的子节点列表
                List<String> children = getSortedChildren();

                // 获取刚才自己创建的那个顺序节点名
                String sequenceNodeName = ourPath.substring(basePath.length()+1);

                // 判断自己排第几个
                int  ourIndex = children.indexOf(sequenceNodeName);

                 /*如果在getSortedChildren中没有找到之前创建的[临时]顺序节点，这表示可能由于网络闪断而导致
                 *Zookeeper认为连接断开而删除了我们创建的节点，此时需要抛出异常，让上一级去处理
                 *上一级的做法是捕获该异常，并且执行重试指定的次数 见后面的 attemptLock方法  */
                if (ourIndex < 0){ // 网络抖动，获取到的子节点列表里可能已经没有自己了
                    throw new ZkNoNodeException("节点没有找到: " + sequenceNodeName);
                }

                //如果当前客户端创建的节点在locker子节点列表中位置大于0，表示其它客户端已经获取了锁
                //此时当前客户端需要等待其它客户端释放锁，
                boolean isGetTheLock = ourIndex == 0;

                //如何判断其它客户端是否已经释放了锁？从子节点列表中获取到比自己次小的哪个节点，并对其建立监听
                String  pathToWatch = isGetTheLock ? null : children.get(ourIndex - 1);

                if ( isGetTheLock ){//如果获取锁啦
                    haveTheLock = true;

                } else {
                    //没有获取到锁的情况
                    // 订阅比自己次小顺序节点的删除事件
                    String  previousSequencePath = basePath .concat( "/" ) .concat( pathToWatch );
                    final CountDownLatch    latch = new CountDownLatch(1);
                    final IZkDataListener previousListener = new IZkDataListener() {
                        //次小节点删除事件发生时，让countDownLatch结束等待
                        //此时还需要重新让程序回到while，重新判断一次！
                        public void handleDataDeleted(String dataPath) throws Exception {
                            latch.countDown(); // 删除后结束latch上的await
                        }
                        public void handleDataChange(String dataPath, Object data) throws Exception {
                            // ignore
                        }
                    };

                    try {
                        //订阅次小顺序节点的删除事件，如果节点不存在会出现异常
                        client.subscribeDataChanges(previousSequencePath, previousListener);

                        if ( millisToWait != null ) {
                            millisToWait -= (System.currentTimeMillis() - startMillis);
                            System.out.println("millisToWait =====" + millisToWait);
                            startMillis = System.currentTimeMillis();
                            if ( millisToWait <= 0 ) {
                                doDelete = true;    // timed out - delete our node
                                break;
                            }

//                            latch.await(millisToWait, TimeUnit.MICROSECONDS); // 在latch上await
                            latch.await(millisToWait, TimeUnit.MICROSECONDS); // 在latch上await
                        } else {
                            latch.await(); // 在latch上await
                        }

                        // 结束latch上的等待后，继续while重新来过判断自己是否第一个顺序节点
                    }
                    catch ( ZkNoNodeException e ) {
                        //ignore
                    } finally {
                        client.unsubscribeDataChanges(previousSequencePath, previousListener);
                    }

                }
            }
        }catch ( Exception e ) {
            //发生异常需要删除节点
            doDelete = true;
            throw e;
        } finally {
            //如果需要删除节点
            if ( doDelete ) {
                deleteOurPath(ourPath);
            }
        }
        return haveTheLock;
    }

    private String getLockNodeNumber(String str, String lockName) {
        int index = str.lastIndexOf(lockName);
        if ( index >= 0 ) {
            index += lockName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    // 获取/locker下的经过排序的子节点列表
    List<String> getSortedChildren() throws Exception {
        try{
            List<String> children = client.getChildren(basePath);
            Collections.sort(
                    children, new Comparator<String>() {
                        public int compare(String lhs, String rhs) {
                            return getLockNodeNumber(lhs, lockName).compareTo(getLockNodeNumber(rhs, lockName));
                        }
                    }
            );
            return children;

        } catch (ZkNoNodeException e){
            client.createPersistent(basePath, true);
            return getSortedChildren();
        }
    }

    protected void releaseLock(String lockPath) throws Exception{
        deleteOurPath(lockPath);
    }

    protected String attemptLock(long time, TimeUnit unit) throws Exception {

        final long      startMillis = System.currentTimeMillis();
        final Long      millisToWait = (unit != null) ? unit.toMillis(time) : null;

        String          ourPath = null;
        boolean         hasTheLock = false;
        boolean         isDone = false;
        int             retryCount = 0;

        //网络闪断需要重试一试
        while ( !isDone ) {
            isDone = true;

            try {
                // 在/locker下创建临时的顺序节点
                ourPath = createLockNode(client, path);
                System.out.println("zookeeper返回的 创建临时节点的路径===" + ourPath);
                // 判断自己是否获得了锁，如果没有获得那么等待直到获得锁或者超时
                hasTheLock = waitToLock(startMillis, millisToWait, ourPath);
            } catch ( ZkNoNodeException e ) { // 捕获这个异常
                if ( retryCount++ < MAX_RETRY_COUNT ) { // 重试指定次数
                    isDone = false;
                } else {
                    throw e;
                }
            }
        }
        if ( hasTheLock ) {
            return ourPath;
        }

        return null;
    }
}
