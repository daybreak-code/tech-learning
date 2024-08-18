package com.llm.zookeeper;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.zookeeper.CreateMode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class ZookeeperDemoApplicationTests {

    private final static String NODE_NAME = "/curator-node";
    private final static String EPHEMERAL_NODE_NAME = "/curator-ephemeral-node-";
    private final static String PARENT_NODE_NAME = "/animal/dog/whiteDog";
    private final static String WATCH_NODE_NAME = "/curator-watch-node";
    private final static byte[] VALUE_BYTES = "dawn".getBytes();
    private final static byte[] NEW_VALUE_BYTES = "dawn-new".getBytes();
    private final static Gson GSON = new Gson();

    @Resource
    private CuratorFramework curatorFramework;

    @Test
    void createNode() throws Throwable {
        String path = curatorFramework.create().forPath(NODE_NAME, VALUE_BYTES);
        log.info("createNode success! path={}", path);
    }

    @SneakyThrows
    @Test
    void createEphemeralSeqNode(){
        String path = curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).
                forPath(EPHEMERAL_NODE_NAME, VALUE_BYTES);
        log.info("createEphemeralSeqNode success! path={}", path);
        Thread.sleep(5000);
    }

    @Test
    @SneakyThrows
    void createWithParent() {
        String path = curatorFramework.create().creatingParentsIfNeeded().forPath(PARENT_NODE_NAME, VALUE_BYTES);
        log.info("createWithParent success! path={}", path);
    }


    /**
     * 获取节点/curator-node上存储的值
     */
    @Test
    @SneakyThrows
    void getData() {
        byte[] valueByte = curatorFramework.getData().forPath(NODE_NAME);
        log.info("getData success! valueByte={}", new String(valueByte));
    }

    /**
     * 修改节点/curator-node的值为“muse-new”
     */
    @Test
    @SneakyThrows
    void setData() {
        curatorFramework.setData().forPath(NODE_NAME, NEW_VALUE_BYTES);
    }

    /**
     * 删除节点/curator-node及其包含的子节点
     */
    @Test
    @SneakyThrows
    void deleteNode() {
        curatorFramework.delete().guaranteed().deletingChildrenIfNeeded().forPath(NODE_NAME);
    }


    //读锁
    @Test
    @SneakyThrows
    void getReadLock(){
        InterProcessReadWriteLock rwlock = new InterProcessReadWriteLock(curatorFramework, "/read-lock");
        InterProcessMutex readLock = rwlock.readLock();
        for (int i = 0; i < 10; i++){
            new Thread(() -> {
                String threadName = Thread.currentThread().getName();
                try {
                    readLock.acquire();
                    log.info("Thread is: {}, wait for got read lock success! start to execute business code!", threadName);
                    Thread.sleep(1000);
                } catch (Throwable e){
                    e.printStackTrace();
                } finally {
                    try {
                        readLock.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
    }

    @Test
    @SneakyThrows
    void getWriteLock(){
        InterProcessReadWriteLock rwlock = new InterProcessReadWriteLock(curatorFramework, "/write-lock");
        InterProcessMutex writeLock = rwlock.writeLock();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    String threadName = Thread.currentThread().getName();
                    writeLock.acquire();
                    log.info("Thread = {}, wait for got write lock success! start to execute business code.", threadName);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    try {
                        writeLock.release();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        }
    }

    @Test
    @SneakyThrows
    void watch(){
        if (null == curatorFramework.checkExists().forPath(NODE_NAME)){
            curatorFramework.create().forPath(WATCH_NODE_NAME);
        }
        CuratorCache curatorCache = CuratorCache.builder(curatorFramework, WATCH_NODE_NAME).build();
        CuratorCacheListener listener = CuratorCacheListener.builder().forNodeCache(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("-----forNodeCache-----{} node is changed!", WATCH_NODE_NAME);
            }
        }).forAll(new CuratorCacheListener() {
            @Override
            public void event(Type type, ChildData oldData, ChildData data) {
                log.info("-----forAll-----{} node is changed！type={} oldDate={} date={}", WATCH_NODE_NAME,
                        GSON.toJson(type), GSON.toJson(oldData), GSON.toJson(data));
            }
        }).build();

        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

}
