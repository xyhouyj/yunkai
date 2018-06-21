package com.hyj.zookeeper.queue;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by houyunjuan on 2018/6/21.
 * 组赛分布式队列
 */
public class DistributedBlockingQueue<T> extends DistributedSimpleQueue<T> {


    public DistributedBlockingQueue(ZkClient zkClient, String root) {
        super(zkClient, root);

    }


    @Override
    public T poll() throws Exception {

        while (true) { // 结束在latch上的等待后，再来一次

            final CountDownLatch latch = new CountDownLatch(1);
            final IZkChildListener childListener = new IZkChildListener() {
                public void handleChildChange(String parentPath, List<String> currentChilds)
                        throws Exception {
                    latch.countDown(); // 队列有变化，结束latch上的等待
                }
            };
            zkClient.subscribeChildChanges(root, childListener);
            try {
                T node = super.poll(); // 获取队列数据
                if (node != null) {
                    return node;
                } else {
                    latch.await(); // 拿不到队列数据，则在latch上await
                }
            } finally {
                zkClient.unsubscribeChildChanges(root, childListener);
            }

        }
    }
}